package online.draughts.rus.client.channel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.workingbit.gwtiot.client.Config;
import com.workingbit.gwtiot.client.IotChannel;
import online.draughts.rus.client.application.common.InviteData;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.application.widget.dialog.ConfirmDialogBox;
import online.draughts.rus.client.application.widget.dialog.ConfirmPlayDialogBox;
import online.draughts.rus.client.application.widget.growl.Growl;
import online.draughts.rus.client.event.*;
import online.draughts.rus.client.gin.DialogFactory;
import online.draughts.rus.client.json.GameMessageMapper;
import online.draughts.rus.client.json.InviteDataMapper;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.client.util.AudioUtil;
import online.draughts.rus.client.util.Logger;
import online.draughts.rus.client.util.TrUtils;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.dto.GameMessageDto;
import online.draughts.rus.shared.dto.MoveDto;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.locale.DraughtsMessages;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 11:39
 */
public class ClientChannel {

  private final CurrentSession currentSession;
  private EventBus eventBus;
  private PlayerDto player;
  private DraughtsMessages messages;
  private ConfirmPlayDialogBox confirmPlayDialogBox;
  private PlaySession playSession;
  private GameMessageMapper messageMapper;
  private final InviteDataMapper inviteDataMapper;
  private final AppResources resources;
  private final DialogFactory dialogFactory;
  private IotChannel<GameMessageDto> channel;
  private final ClientConfiguration config;

  @Inject
  public ClientChannel(EventBus eventBus,
                       CurrentSession currentSession,
                       final PlaySession playSession,
                       GameMessageMapper messageMapper,
                       InviteDataMapper inviteDataMapper,
                       final DraughtsMessages messages,
                       AppResources resources,
                       DialogFactory dialogFactory,
                       ClientConfiguration config) {
    this.currentSession = currentSession;
    this.playSession = playSession;
    this.eventBus = eventBus;
    this.messages = messages;
    this.messageMapper = messageMapper;
    this.inviteDataMapper = inviteDataMapper;
    this.resources = resources;
    this.dialogFactory = dialogFactory;
    this.config = config;

    bindEvents();

    Window.addWindowClosingHandler(event -> {
      if (channel == null) {
        return;
      }
      sendSimpleMessage(GameMessageDto.MessageType.CHANNEL_CLOSE);
    });

    Window.addCloseHandler(event -> {
      if (channel == null) {
        return;
      }
      sendSimpleMessage(GameMessageDto.MessageType.CHANNEL_CLOSE);
    });
  }

  private void sendSimpleMessage(GameMessageDto.MessageType channelClose) {
    GameMessageDto gameMessage = createGameMessage();
    gameMessage.setMessageType(channelClose);
    sendGameMessage(gameMessage);
  }

  private void bindEvents() {
    eventBus.addHandler(GameMessageEvent.TYPE, event -> {
      GameMessageDto gameMessage = event.getGameMessage();

      sendGameMessage(gameMessage);
    });

    eventBus.addHandler(UpdatePlayerListEvent.TYPE, event -> sendSimpleMessage(GameMessageDto.MessageType.USER_LIST_UPDATE));

    eventBus.addHandler(UpdateAllPlayerListEvent.TYPE, event -> sendSimpleMessage(GameMessageDto.MessageType.USER_LIST_UPDATE));
    eventBus.addHandler(ClearPlayComponentEvent.TYPE, event -> {
      playSession.setGame(null);
      playSession.setGameType(null);
    });
  }

  private GameMessageDto createGameMessage() {
    GameMessageDto message = GWT.create(GameMessageDto.class);
    message.setSender(playSession.getPlayer());
    message.setReceiver(playSession.getOpponent());
    return message;
  }

  private void sendGameMessage(GameMessageDto gameMessage) {
    if (gameMessage.getSender() == null) {
      gameMessage.setSender(playSession.getPlayer());
    }
    if (gameMessage.getReceiver() == null) {
      return;
    }
    channel.send(gameMessage);
  }

  private void handleUpdatePlayerList(List<PlayerDto> playerList) {
    for (PlayerDto p : playerList) {
      if (p.getId() == player.getId()) {
        player.updateSerializable(p);
        break;
      }
    }
    eventBus.fireEvent(new ReceivedPlayerListEvent(playerList));
  }

  /**
   * Начало игры на стороне приглашенного
   */
  private void handlePlayInvite(final GameMessageDto gameMessage) {
    if (confirmPlayDialogBox != null && confirmPlayDialogBox.isShowing()) {
      GameMessageDto message = createGameMessage(gameMessage);
      message.setMessageType(GameMessageDto.MessageType.PLAY_ALREADY_PLAYING);
      sendGameMessage(message);
      return;
    }
    final InviteData inviteData = inviteDataMapper.read(gameMessage.getData());
    confirmPlayDialogBox = new ConfirmPlayDialogBox() {
      @Override
      public void submitted() {
        if (gameMessage.getSender() == null) {
          Growl.growlNotif(messages.opponentNotFound());
          return;
        }

        playSession.setOpponent(gameMessage.getSender());

        final GameDto game = new GameDto();
        game.setPublish(inviteData.isPublishGame());
        game.setPlayerWhite(isWhite() ? playSession.getPlayer() : playSession.getOpponent());
        game.setPlayerBlack(isWhite() ? playSession.getOpponent() : playSession.getPlayer());
        GameMessageDto message = createGameMessage(gameMessage);
        message.setMessageType(GameMessageDto.MessageType.PLAY_START);
        message.setGame(game);
        boolean white = inviteData.isWhite();
        // инвертируем цвет
        inviteData.setWhite(!inviteData.isWhite());
        String json = inviteDataMapper.write(inviteData);
        inviteData.setWhite(white);
        message.setData(json);

        sendGameMessage(message);

        playSession.setGame(game);
        eventBus.fireEvent(new StartPlayEvent(inviteData.getGameType(), white, false,
            inviteData.getTimeOnPlay(), inviteData.getFisherTime()));
      }

      @Override
      public void canceled() {
        GameMessageDto message = createGameMessage(gameMessage);
        message.setMessageType(GameMessageDto.MessageType.PLAY_REJECTED_INVITE);

        sendGameMessage(message);
      }
    };
    final String color = inviteData.isWhite() ?
        messages.white_with_ending() : messages.black_with_ending();
    confirmPlayDialogBox.show(messages.inviteMessage(gameMessage.getMessage(),
        TrUtils.translateGameType(inviteData.getGameType()), color,
        String.valueOf(inviteData.getTimeOnPlay()), String.valueOf(inviteData.getFisherTime())), inviteData.isWhite());
    AudioUtil.playSound(resources.sounds().inviteSound());
  }

  public void onOpen() {
    if (player == null) {
      dialogFactory.createInfoDialogBox(messages.failedToConnectToServer()).show();
      return;
    }
    GameMessageDto gameMessage = GWT.create(GameMessageDto.class);
    gameMessage.setSender(player);
    gameMessage.setMessageType(GameMessageDto.MessageType.PLAYER_REGISTER);

    sendGameMessage(gameMessage);

    playSession.setConnected(true);
    eventBus.fireEvent(new ConnectedToPlayEvent());
  }

  public void onError(String description) {
    Growl.growlNotif(description);
  }

  public void onClose() {
    sendSimpleMessage(GameMessageDto.MessageType.CHANNEL_CLOSE);
    handleClose();
  }

  private void handleClose() {
    playSession.setConnected(false);
    eventBus.fireEvent(new DisconnectFromPlayEvent());
  }

  public void onMessage(GameMessageDto gameMessage) {
//    if (StringUtils.isEmpty(message)) {
//      return;
//    }
//    Chunk chunk = chunkMapper.read(message);
//    GameMessageDto gameMessage;
//    if (chunk.getChunksInMessage() == 1) {
//      gameMessage = messageMapper.read(chunk.getMessage());
//    } else {
//      gameMessageChunks.put(chunk.getNumber(), chunk.getMessage());
//      if (chunk.getNumber() == chunk.getChunksInMessage()) {
//        StringBuilder gameMessageStr = new StringBuilder();
//        for (String msg : gameMessageChunks.values()) {
//          gameMessageStr.append(msg);
//        }
//        gameMessage = messageMapper.read(gameMessageStr.toString());
//      } else {
//        return;
//      }
//    }

    switch (gameMessage.getMessageType()) {
      case USER_LIST_UPDATE:
        handleUpdatePlayerList(gameMessage.getPlayerList());
        break;
      case PLAY_INVITE:
        handlePlayInvite(gameMessage);
        break;
      case PLAY_START:
        handlePlayStart(gameMessage);
        break;
      case PLAY_REJECTED_INVITE:
        handlePlayRejectInvite(gameMessage);
        break;
      case PLAY_DID_NOT_RESPONSE:
        handleDidNotResponse();
        break;
      case PLAY_ALREADY_PLAYING:
        handlePlayAlreadyPlaying(gameMessage);
        break;
      case PLAY_OPPONENT_MOVE:
        handlePlayMove(gameMessage);
        break;
      case PLAY_SURRENDER:
        handlePlaySurrender(gameMessage);
        break;
      case PLAY_PROPOSE_DRAW:
        handlePlayProposeDraw(gameMessage);
        break;
      case PLAY_ACCEPT_DRAW:
        handlePlayAcceptDraw(gameMessage);
        break;
      case PLAY_GAME_UPDATE:
        handlePlayEndGame(gameMessage);
        break;
      case PLAY_TIMEOUT:
        handlePlayTimeout(gameMessage);
        break;
      case PLAY_CANCEL_MOVE:
        handlePlayCancelMove(gameMessage);
        break;
      case PLAY_CANCEL_MOVE_RESPONSE:
        handlePlayCancelMoveResponse(gameMessage);
        break;
      case CHAT_PRIVATE_MESSAGE:
        handleChatPrivateMessage(gameMessage);
        break;
      case NOTIFICATION_ADDED_TO_FAVORITE:
        handleNotification(gameMessage);
        break;
      case CHANNEL_CLOSE:
        handleChannelClose();
        break;
    }
  }

  private void handlePlayTimeout(GameMessageDto gameMessage) {
    if (playSession.getGame() != null) {
      GameDto game = gameMessage.getGame();
      eventBus.fireEvent(new GameOverEvent(game, new AbstractAsyncCallback<GameDto>(dialogFactory) {
        @Override
        public void onSuccess(GameDto aVoid) {
          dialogFactory.createGameResultDialogBox(messages.timeOutOpponentLose()).show();
        }
      }));
    }
  }

  private void handleDidNotResponse() {
    if (null != confirmPlayDialogBox) {
      confirmPlayDialogBox.hide();
    }
  }

  private void handleChannelClose() {
    handleClose();
  }

  private void handleNotification(GameMessageDto gameMessage) {
    Growl.growlNotif(gameMessage.getMessage());
  }

  @SuppressWarnings("unused")
  private void handlePlayEndGame(GameMessageDto gameMessage) {
    Logger.debug(gameMessage.getData());
    if (playSession.getGame() != null && GameMessageDto.GAME_END.equals(gameMessage.getData())) {
      GameDto game = playSession.getGame();
      final GameDto.GameEnd gameEnd;
      final GameDto.GameEnd remotePlayEndStatus = gameMessage.getGame().getPlayEndStatus();
      if (GameDto.GameEnd.WHITE_WIN.equals(remotePlayEndStatus)
          || GameDto.GameEnd.BLACK_WIN.equals(remotePlayEndStatus)) {
        gameEnd = remotePlayEndStatus;
      } else if (!GameDto.GameEnd.DRAW.equals(remotePlayEndStatus)) {
        gameEnd = playSession.isPlayerHasWhiteColor()
            ? GameDto.GameEnd.BLACK_LEFT : GameDto.GameEnd.WHITE_LEFT;
      } else {
        gameEnd = GameDto.GameEnd.DRAW;
      }
      game.setPlayEndStatus(gameEnd);
      Logger.debug("2 " + gameEnd);
      eventBus.fireEvent(new GameOverEvent(game, new AbstractAsyncCallback<GameDto>(dialogFactory) {
        @Override
        public void onSuccess(GameDto aVoid) {
          Growl.growlNotif(messages.opponentLeftGame());
        }
      }));
    } else if (playSession.getGame() != null && GameMessageDto.GAME_UPDATE.equals(gameMessage.getData())) {
      Logger.debug("4");
      playSession.getGame().setId(gameMessage.getGame().getId());
    } else {
      Logger.debug("5");
    }
  }

  private void handlePlayAlreadyPlaying(GameMessageDto gameMessage) {
    eventBus.fireEvent(new HideInviteDialogBoxEvent());
    Growl.growlNotif(messages.playAlreadyPlaying(gameMessage.getSender().getPublicName()));
  }

  /**
   * Обработчик ответа на отмену хода. Если оппонент подтвердил, тогда перемещаем его шашку.
   */
  private void handlePlayCancelMoveResponse(GameMessageDto gameMessage) {
    boolean isAcceptedCancelMove = Boolean.valueOf(gameMessage.getData());
    if (isAcceptedCancelMove) {
      final MoveDto move = gameMessage.getMove();
      eventBus.fireEvent(new PlayMoveCancelEvent(move));
    } else {
      dialogFactory.createInfoDialogBox(messages.playerRejectedMoveCancel(gameMessage.getSender().getPublicName())).show();
    }
  }

  /**
   * Вопрос на строне оппонента о том, что ему предлагается отменить ход. Если он соглашается, то он двигает шашку
   * оппонента
   */
  private void handlePlayCancelMove(final GameMessageDto gameMessage) {
    new ConfirmDialogBox(messages.playerProposesCancelMove(gameMessage.getSender().getPublicName())) {
      @Override
      public void procConfirm() {
        GameMessageDto returnGameMessage = createGameMessage(gameMessage);
        returnGameMessage.setMessageType(GameMessageDto.MessageType.PLAY_CANCEL_MOVE_RESPONSE);
        returnGameMessage.setMove(gameMessage.getMove());
        if (isConfirmed()) {
          returnGameMessage.setData(Boolean.TRUE.toString());
          eventBus.fireEvent(new PlayMoveOpponentCancelEvent(gameMessage.getMove()));
        } else {
          returnGameMessage.setData(Boolean.FALSE.toString());
        }
        sendGameMessage(returnGameMessage);
      }
    };
  }

  private void handlePlayAcceptDraw(GameMessageDto gameMessage) {
    if (Boolean.valueOf(gameMessage.getData())) {
      final GameDto gameDto = playSession.getGame();
//      // уведомляем оппонента об окончании игры
//      GameMessageDto gameMessageDto = createGameMessage();
//      gameMessageDto.setMessageType(GameMessageDto.MessageType.PLAY_GAME_UPDATE);
//      gameMessageDto.setData(GameMessageDto.GAME_END);
//      gameMessageDto.setGame(gameDto);
//      eventBus.fireEvent(new GameMessageEvent(gameMessageDto));

      gameDto.setPlayEndStatus(GameDto.GameEnd.DRAW);
      eventBus.fireEvent(new GameOverEvent(gameDto, new AbstractAsyncCallback<GameDto>(dialogFactory) {
        @Override
        public void onSuccess(GameDto aVoid) {
        }
      }));
    } else {
      String senderName = gameMessage.getSender().getPublicName();
      dialogFactory.createGameResultDialogBox(messages.playerRejectedDraw(senderName)).show();
    }
  }

  /**
   * Принятие ничьи на стороне клиента
   *
   * @param gameMessage
   */
  private void handlePlayProposeDraw(final GameMessageDto gameMessage) {
    String senderName = gameMessage.getSender().getPublicName();
    new ConfirmDialogBox(messages.playerProposesDraw(senderName)) {
      @Override
      public void procConfirm() {
        GameMessageDto message = createGameMessage(gameMessage);
        message.setMessageType(GameMessageDto.MessageType.PLAY_ACCEPT_DRAW);

        if (isConfirmed()) {
          message.setData(Boolean.TRUE.toString());
        } else {
          message.setData(Boolean.FALSE.toString());
        }
        message.setGame(playSession.getGame());
        message.getGame().setPlayFinishDate(new Date());

        sendGameMessage(message);

//        if (isConfirmed()) {
//          eventBus.fireEvent(new ClearPlayComponentEvent());
//        }
      }
    };
  }

  private GameMessageDto createGameMessage(GameMessageDto gameMessage) {
    GameMessageDto message = new GameMessageDto();
    message.setSender(gameMessage.getReceiver());
    message.setReceiver(gameMessage.getSender());
    return message;
  }

  @SuppressWarnings("unused")
  private void handlePlaySurrender(GameMessageDto gameMessage) {
    GameDto game = playSession.getGame();
    // так как сохраняем на противоположной стороне, игроки черный-белый переставлены
    final GameDto.GameEnd gameEnd = playSession.isPlayerHasWhiteColor() ? GameDto.GameEnd.SURRENDER_BLACK
        : GameDto.GameEnd.SURRENDER_WHITE;
    game.setPlayEndStatus(gameEnd);
    eventBus.fireEvent(new GameOverEvent(game, new AbstractAsyncCallback<GameDto>(dialogFactory) {
      @Override
      public void onSuccess(GameDto aVoid) {
        dialogFactory.createInfoDialogBox(messages.opponentSurrendered()).show();
      }
    }));
  }

  /**
   * Обрабатываем ход оппонента
   */
  private void handlePlayMove(GameMessageDto gameMessage) {
    final MoveDto move = gameMessage.getMove();
    eventBus.fireEvent(new PlayMoveOpponentEvent(move, Integer.valueOf(gameMessage.getData())));
  }

  /**
   * Начало игры на стороне приглашающего
   */
  private void handlePlayStart(final GameMessageDto gameMessage) {
    playSession.setOpponent(gameMessage.getSender());
    playSession.setGame(gameMessage.getGame());
    InviteData inviteData = inviteDataMapper.read(gameMessage.getData());
    eventBus.fireEvent(new StartPlayEvent(inviteData.getGameType(), inviteData.isWhite(), true,
        inviteData.getTimeOnPlay(), inviteData.getFisherTime()));
  }

  private void handleChatPrivateMessage(GameMessageDto gameMessage) {
    eventBus.fireEvent(new ChatMessageEvent(gameMessage));
  }

  private void handlePlayRejectInvite(GameMessageDto gameMessage) {
    playSession.setOpponent(null);
    dialogFactory.createInfoDialogBox(messages.playerRejectedPlayRequest(gameMessage.getSender().getPublicName()))
        .show();
    eventBus.fireEvent(new RejectPlayEvent());
  }

  public void connect() {
    player = currentSession.getPlayer();
    playSession.setPlayer(player);

    Config config = new Config();
    config.setLambdaEndpoint("https://tpoxu1bmxc.execute-api.us-east-1.amazonaws.com/dev/iot/keys");
    config.setIotTopic("/serverless/pubsub");

    channel = new IotChannel<>(config);
    channel.setMapper(GameMessageMapper.INSTANCE);
    channel.addConnectHandler(() -> GWT.log("Connected"));
    channel.addMessageHandler(this::onMessage);
    channel.addConnectHandler(this::onOpen);
    channel.addErrorHandler(this::onError);
    channel.connect();
  }
}
