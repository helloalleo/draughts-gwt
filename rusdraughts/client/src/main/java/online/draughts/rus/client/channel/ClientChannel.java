package online.draughts.rus.client.channel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import no.eirikb.gwtchannelapi.client.Channel;
import no.eirikb.gwtchannelapi.client.ChannelListener;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.application.widget.dialog.ConfirmPlayDialogBox;
import online.draughts.rus.client.application.widget.dialog.ConfirmeDialogBox;
import online.draughts.rus.client.application.widget.dialog.ErrorDialogBox;
import online.draughts.rus.client.application.widget.dialog.InfoDialogBox;
import online.draughts.rus.client.application.widget.growl.Growl;
import online.draughts.rus.client.event.*;
import online.draughts.rus.client.json.ChunkMapper;
import online.draughts.rus.client.json.GameMessageMapper;
import online.draughts.rus.client.util.Logger;
import online.draughts.rus.shared.channel.Chunk;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.dto.GameMessageDto;
import online.draughts.rus.shared.dto.MoveDto;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.resource.GamesResource;
import online.draughts.rus.shared.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 11:39
 */
public class ClientChannel implements ChannelListener {

  private final CurrentSession currentSession;
  private ResourceDelegate<GamesResource> gamesDelegate;
  private EventBus eventBus;
  private PlayerDto player;
  private Channel channel;
  private DraughtsMessages messages;
  private ConfirmPlayDialogBox confirmPlayDialogBox;
  private PlaySession playSession;
  private ChunkMapper chunkMapper;
  private GameMessageMapper messageMapper;
  private Map<Integer, String> gameMessageChunks = new TreeMap<>();

  @Inject
  public ClientChannel(EventBus eventBus,
                       CurrentSession currentSession,
                       PlaySession playSession,
                       ChunkMapper chunkMapper,
                       GameMessageMapper messageMapper,
                       ResourceDelegate<GamesResource> gamesDelegate,
                       DraughtsMessages messages) {
    this.currentSession = currentSession;
    this.playSession = playSession;
    this.gamesDelegate = gamesDelegate;
    this.eventBus = eventBus;
    this.messages = messages;
    this.chunkMapper = chunkMapper;
    this.messageMapper = messageMapper;

    bindEvents();

    Window.addWindowClosingHandler(new Window.ClosingHandler() {
      @Override
      public void onWindowClosing(Window.ClosingEvent event) {
        if (channel == null) {
          return;
        }
        sendSimpleMessage(GameMessageDto.MessageType.CHANNEL_CLOSE);
      }
    });
  }

  private void sendSimpleMessage(GameMessageDto.MessageType channelClose) {
    GameMessageDto gameMessage = createGameMessage();
    gameMessage.setMessageType(channelClose);
    sendGameMessage(gameMessage);
  }

  private void bindEvents() {
    eventBus.addHandler(GameMessageEvent.TYPE, new GameMessageEventHandler() {
      @Override
      public void onPlayerMessage(GameMessageEvent event) {
        GameMessageDto gameMessage = event.getGameMessage();

        sendGameMessage(gameMessage);
      }
    });

    // перемещаем шашку оппонента
    eventBus.addHandler(PlayMovePlayerMessageEvent.TYPE, new PlayMoveMessageEventHandler() {
      @Override
      public void onPlayMoveMessage(PlayMovePlayerMessageEvent event) {
        GameMessageDto message = createGameMessage();
        message.setMessageType(GameMessageDto.MessageType.PLAY_OPPONENT_MOVE);
        message.setMove(event.getMove());
        message.setGame(playSession.getGame());

        sendGameMessage(message);
      }
    });

    eventBus.addHandler(UpdatePlayerListEvent.TYPE, new UpdatePlayerListEventHandler() {
      @Override
      public void onUpdatePlayerList(UpdatePlayerListEvent event) {
        sendSimpleMessage(GameMessageDto.MessageType.USER_LIST_UPDATE);
      }
    });

    eventBus.addHandler(UpdateAllPlayerListEvent.TYPE, new UpdateAllPlayerListEventHandler() {
      @Override
      public void onUpdateAllPlayerList(UpdateAllPlayerListEvent event) {
        sendSimpleMessage(GameMessageDto.MessageType.USER_LIST_UPDATE);
      }
    });
    eventBus.addHandler(ClearPlayComponentEvent.TYPE, new ClearPlayComponentEventHandler() {
      @Override
      public void onClearPlayComponent(ClearPlayComponentEvent event) {
        playSession.setGame(null);
      }
    });
  }

  private GameMessageDto createGameMessage() {
    GameMessageDto message = GWT.create(GameMessageDto.class);
    message.setSender(playSession.getPlayer());
    message.setReceiver(playSession.getOpponent());
    return message;
  }

  private void sendGameMessage(GameMessageDto gameMessage) {
    String message = messageMapper.write(gameMessage);
    channel.send(message);
  }

  private void handleUpdatePlayerDtoList(List<PlayerDto> playerList) {
    for (PlayerDto p : playerList) {
      if (p.getId().equals(player.getId())) {
        // TODO Обновить поля?
        player = p;
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
    confirmPlayDialogBox = new ConfirmPlayDialogBox() {
      @Override
      public void submitted() {
        if (gameMessage.getSender() == null) {
          Growl.growlNotif(messages.opponentNotFound());
          return;
        }

        playSession.setOpponent(gameMessage.getSender());

        GameDto game = GWT.create(GameDto.class);
        game.setPlayStartDate(new Date());
        game.setPlayerWhite(isWhite() ? playSession.getPlayer() : playSession.getOpponent());
        game.setPlayerBlack(isWhite() ? playSession.getOpponent() : playSession.getPlayer());

        gamesDelegate.withCallback(new AsyncCallback<GameDto>() {
          @Override
          public void onFailure(Throwable throwable) {
            ErrorDialogBox.setMessage(messages.failToStartGame(), throwable).show();
          }

          @Override
          public void onSuccess(GameDto game) {
            GameMessageDto message = createGameMessage(gameMessage);
            message.setMessageType(GameMessageDto.MessageType.PLAY_START);
            message.setGame(game);
            message.setData(String.valueOf(!isWhite()));

            sendGameMessage(message);

            playSession.setGame(game);
            eventBus.fireEvent(new StartPlayEvent(isWhite()));
          }
        }).save(game);
      }

      @Override
      public void canceled() {
        GameMessageDto message = createGameMessage(gameMessage);
        message.setMessageType(GameMessageDto.MessageType.PLAY_REJECT_INVITE);

        sendGameMessage(message);
      }
    };
    confirmPlayDialogBox.show(gameMessage.getMessage(), Boolean.valueOf(gameMessage.getData()));
  }

  @Override
  public void onOpen() {
    if (player == null) {
      InfoDialogBox.setMessage(messages.failToConnectToServer()).show();
      return;
    }
    GameMessageDto gameMessage = GWT.create(GameMessageDto.class);
    gameMessage.setSender(player);
    gameMessage.setMessageType(GameMessageDto.MessageType.PLAYER_REGISTER);

    sendGameMessage(gameMessage);

    playSession.setConnected(true);
    eventBus.fireEvent(new ConnectedToPlayEvent());
  }

  @Override
  public void onError(int code, String description) {

  }

  @Override
  public void onClose() {
    sendSimpleMessage(GameMessageDto.MessageType.CHANNEL_CLOSE);

    playSession.setConnected(false);
    eventBus.fireEvent(new DisconnectFromPlayEvent());
  }

  @Override
  public void onMessage(String message) {
    if (StringUtils.isEmpty(message)) {
      return;
    }
    Logger.debug(message);
    Chunk chunk = chunkMapper.read(message);
    GameMessageDto gameMessage;
    if (chunk.getChunksInMessage() == 1) {
      gameMessage = messageMapper.read(chunk.getMessage());
    } else {
      gameMessageChunks.put(chunk.getNumber(), chunk.getMessage());
      if (chunk.getNumber() == chunk.getChunksInMessage()) {
        StringBuilder gameMessageStr = new StringBuilder();
        for (String msg : gameMessageChunks.values()) {
          gameMessageStr.append(msg);
        }
        Logger.debug(gameMessageStr.toString());
        gameMessage = messageMapper.read(gameMessageStr.toString());
      } else {
        return;
      }
    }

    switch (gameMessage.getMessageType()) {
      case USER_LIST_UPDATE:
        handleUpdatePlayerDtoList(gameMessage.getPlayerList());
        break;
      case PLAY_INVITE:
        handlePlayInvite(gameMessage);
        break;
      case PLAY_START:
        handlePlayStart(gameMessage);
        break;
      case PLAY_REJECT_INVITE:
        handlePlayRejectInvite(gameMessage);
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
      case PLAY_CANCEL_MOVE:
        handlePlayCancelMove(gameMessage);
        break;
      case PLAY_CANCEL_MOVE_RESPONSE:
        handlePlayCancelMoveResponse(gameMessage);
        break;
      case PLAY_END:
        handlePlayEndGame(gameMessage);
        break;
      case CHAT_PRIVATE_MESSAGE:
        handleChatPrivateMessage(gameMessage);
        break;
      case NOTIFICATION_ADDED_TO_FAVORITE:
        handleNotification(gameMessage);
        break;
    }

  }

  private void handleNotification(GameMessageDto gameMessage) {
    Growl.growlNotif(gameMessage.getMessage());
  }

  @SuppressWarnings("unused")
  private void handlePlayEndGame(GameMessageDto gameMessage) {
    if (playSession.getGame() != null) {
      GameDto game = playSession.getGame();
      final GameDto.GameEnds gameEnd = playSession.isPlayerHasWhiteColor()
          ? GameDto.GameEnds.BLACK_LEFT : GameDto.GameEnds.WHITE_LEFT;
      eventBus.fireEvent(new GameOverEvent(game, gameEnd, new AsyncCallback<GameDto>() {
        @Override
        public void onFailure(Throwable throwable) {
          ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), throwable).show();
        }

        @Override
        public void onSuccess(GameDto aVoid) {
          Growl.growlNotif(messages.opponentLeftGame());
        }
      }));
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
      InfoDialogBox.setMessage(messages.playerRejectedMoveCancel(gameMessage.getSender().getPublicName())).show();
    }
  }

  /**
   * Вопрос на строне оппонента о том, что ему предлагается отменить ход. Если он соглашается, то он двигает шашку
   * оппонента
   */
  private void handlePlayCancelMove(final GameMessageDto gameMessage) {
    new ConfirmeDialogBox(messages.playerProposesCancelMove(gameMessage.getSender().getPublicName())) {
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
      eventBus.fireEvent(new GameOverEvent(playSession.getGame(), GameDto.GameEnds.DRAW, new AsyncCallback<GameDto>() {
        @Override
        public void onFailure(Throwable throwable) {
          ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), throwable).show();
        }

        @Override
        public void onSuccess(GameDto aVoid) {
        }
      }));
    } else {
      String senderName = gameMessage.getSender().getPublicName();
      InfoDialogBox.setMessage(messages.playerRejectedDraw(senderName)).show();
    }
  }

  private void handlePlayProposeDraw(final GameMessageDto gameMessage) {
    String senderName = gameMessage.getSender().getPublicName();
    new ConfirmeDialogBox(messages.playerProposesDraw(senderName)) {
      @Override
      public void procConfirm() {
        GameMessageDto message = createGameMessage(gameMessage);
        message.setMessageType(GameMessageDto.MessageType.PLAY_ACCEPT_DRAW);

        if (isConfirmed()) {
          message.setData(Boolean.TRUE.toString());
        } else {
          message.setData(Boolean.FALSE.toString());
        }

        sendGameMessage(message);

        if (isConfirmed()) {
          eventBus.fireEvent(new ClearPlayComponentEvent());
        }
      }
    };
  }

  private GameMessageDto createGameMessage(GameMessageDto gameMessage) {
    GameMessageDto message = GWT.create(GameMessageDto.class);
    message.setSender(gameMessage.getReceiver());
    message.setReceiver(gameMessage.getSender());
    return message;
  }

  @SuppressWarnings("unused")
  private void handlePlaySurrender(GameMessageDto gameMessage) {
    GameDto game = playSession.getGame();
    // так как сохраняем на противоположной строне, игроки черный-белый переставлены
    final GameDto.GameEnds gameEnd = playSession.isPlayerHasWhiteColor() ? GameDto.GameEnds.SURRENDER_BLACK
        : GameDto.GameEnds.SURRENDER_WHITE;
    eventBus.fireEvent(new GameOverEvent(game, gameEnd, new AsyncCallback<GameDto>() {
      @Override
      public void onFailure(Throwable throwable) {
        ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), throwable).show();
      }

      @Override
      public void onSuccess(GameDto aVoid) {
        InfoDialogBox.setMessage(messages.opponentSurrendered()).show();
      }
    }));
  }

  /**
   * Обрабатываем ход оппонента
   */
  private void handlePlayMove(GameMessageDto gameMessage) {
    final MoveDto move = gameMessage.getMove();
    eventBus.fireEvent(new PlayMoveOpponentEvent(move));
  }

  /**
   * Начало игры на стороне приглашающего
   */
  private void handlePlayStart(final GameMessageDto gameMessage) {
    final GameDto game = gameMessage.getGame();
    playSession.setGame(game);
    boolean white = Boolean.valueOf(gameMessage.getData());
    playSession.setOpponent(white ? game.getPlayerBlack() : game.getPlayerWhite());
    eventBus.fireEvent(new StartPlayEvent(white));
  }

  private void handleChatPrivateMessage(GameMessageDto gameMessage) {
    eventBus.fireEvent(new ChatMessageEvent(gameMessage));
  }

  private void handlePlayRejectInvite(GameMessageDto gameMessage) {
    playSession.setOpponent(null);
    InfoDialogBox
        .setMessage(messages.playerRejectedPlayRequest(gameMessage.getSender().getPublicName()))
        .show();
    eventBus.fireEvent(new RejectPlayEvent());
  }

  public void connect() {
    player = currentSession.getPlayer();
    playSession.setPlayer(player);

    channel = new Channel(String.valueOf(player.getId()));
    channel.addChannelListener(this);
    channel.join();
  }
}
