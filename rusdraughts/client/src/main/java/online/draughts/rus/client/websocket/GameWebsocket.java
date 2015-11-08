package online.draughts.rus.client.websocket;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.websockets.client.WebSocket;
import com.google.gwt.websockets.client.WebSocketCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.application.widget.dialog.ConfirmPlayDialogBox;
import online.draughts.rus.client.application.widget.dialog.ConfirmeDialogBox;
import online.draughts.rus.client.application.widget.dialog.ErrorDialogBox;
import online.draughts.rus.client.application.widget.dialog.InfoDialogBox;
import online.draughts.rus.client.application.widget.growl.Growl;
import online.draughts.rus.client.event.*;
import online.draughts.rus.client.json.GameMessageMapper;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.model.Game;
import online.draughts.rus.shared.model.GameMessage;
import online.draughts.rus.shared.model.Move;
import online.draughts.rus.shared.model.Player;
import online.draughts.rus.shared.resource.GamesResource;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 11:39
 */
public class GameWebsocket implements WebSocketCallback {

  private final CurrentSession currentSession;
  private ClientConfiguration config;
  private ResourceDelegate<GamesResource> gamesDelegate;
  private WebSocket webSocket;
  private EventBus eventBus;
  private Player player;
  private DraughtsMessages messages;
  private ConfirmPlayDialogBox confirmPlayDialogBox;
  private PlaySession playSession;
  private GameMessageMapper messageMapper;

  @Inject
  private GameWebsocket(EventBus eventBus,
                        CurrentSession currentSession,
                        PlaySession playSession,
                        ClientConfiguration config,
                        GameMessageMapper messageMapper,
                        ResourceDelegate<GamesResource> gamesDelegate,
                        DraughtsMessages messages) {
    this.currentSession = currentSession;
    this.playSession = playSession;
    this.gamesDelegate = gamesDelegate;
    this.eventBus = eventBus;
    this.messages = messages;
    this.config = config;
    this.messageMapper = messageMapper;

    bindEvents();
  }

  private void bindEvents() {
    HandlerRegistration gameMessageHR = eventBus.addHandler(GameMessageEvent.TYPE, new GameMessageEventHandler() {
      @Override
      public void onPlayerMessage(GameMessageEvent event) {
        GameMessage gameMessage = (GameMessage) event.getGameMessage();

        sendGameMessage(gameMessage);
      }
    });

    HandlerRegistration playMoveHR = eventBus.addHandler(PlayMoveMessageEvent.TYPE, new PlayMoveMessageEventHandler() {
      @Override
      public void onPlayMoveMessage(PlayMoveMessageEvent event) {
        GameMessage message = createSendGameMessage();
        message.setMessageType(GameMessage.MessageType.PLAY_MOVE);
        message.setMove(event.getMove());
        message.setGame(playSession.getGame());

        sendGameMessage(message);
      }
    });

    HandlerRegistration updatePlayerListHR = eventBus.addHandler(UpdatePlayerListEvent.TYPE, new UpdatePlayerListEventHandler() {
      @Override
      public void onUpdatePlayerList(UpdatePlayerListEvent event) {
        updatePlayerListMessage();
      }
    });

    eventBus.addHandler(UpdateAllPlayerListEvent.TYPE, new UpdateAllPlayerListEventHandler() {
      @Override
      public void onUpdateAllPlayerList(UpdateAllPlayerListEvent event) {
        updatePlayerListMessage();
        eventBus.fireEvent(new UpdatePlayerFriendListEvent());
      }
    });

    eventBus.addHandler(RemovePlayMoveOpponentHandlerEvent.TYPE, new RemoveWebsocketHandlersEventHandler() {
      @Override
      public void onRemovePlayMoveOpponentHandler(RemovePlayMoveOpponentHandlerEvent event) {
        removeHandlers();
      }
    });

    eventBus.addHandler(ClearPlayComponentEvent.TYPE, new ClearPlayComponentEventHandler() {
      @Override
      public void onClearPlayComponent(ClearPlayComponentEvent event) {
        playSession.setGame(null);
      }
    });
  }

  private void updatePlayerListMessage() {
    GameMessage message = createSendGameMessage();
    message.setMessageType(GameMessage.MessageType.USER_LIST_UPDATE);
    sendGameMessage(message);
  }

  private void removeHandlers() {
//    playMoveHR.removeHandler();
//    updatePlayerListHR.removeHandler();
//    connectToPlayHR.removeHandler();
//    gameMessageHR.removeHandler();
  }

  private GameMessage createSendGameMessage() {
    GameMessage message = GWT.create(GameMessage.class);
    message.setSender(playSession.getPlayer());
    message.setReceiver(playSession.getOpponent());
    return message;
  }

  private void sendGameMessage(GameMessage gameMessage) {
    if (gameMessage.getMove() != null) {
    }
    String message = messageMapper.write(gameMessage);
    webSocket.send(message);
  }

  private void handleUpdatePlayerList(List<Player> playerList) {
    for (Player p : playerList) {
      if (p.getId().equals(player.getId())) {
        player.updateSerializable(p);
        break;
      }
    }
    eventBus.fireEvent(new ReceivedPlayerListEvent(playerList));
  }

  /**
   * Начало игры на стороне приглашенного
   *
   * @param gameMessage
   */
  private void handlePlayInvite(final GameMessage gameMessage) {
    if (confirmPlayDialogBox != null && confirmPlayDialogBox.isShowing()) {
      GameMessage message = createSendGameMessage(gameMessage);
      message.setMessageType(GameMessage.MessageType.PLAY_ALREADY_PLAYING);
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

        Game game = GWT.create(Game.class);
        game.setPlayStartDate(new Date());
        game.setPlayerWhite(isWhite() ? playSession.getPlayer() : playSession.getOpponent());
        game.setPlayerBlack(isWhite() ? playSession.getOpponent() : playSession.getPlayer());

        gamesDelegate.withCallback(new AsyncCallback<Game>() {
          @Override
          public void onFailure(Throwable throwable) {
            ErrorDialogBox.setMessage(messages.failToStartGame(), throwable).show();
          }

          @Override
          public void onSuccess(Game game) {
            GameMessage message = createSendGameMessage(gameMessage);
            message.setMessageType(GameMessage.MessageType.PLAY_START);
            message.setGame(game);
            message.setData(String.valueOf(!isWhite()));

            sendGameMessage(message);

            playSession.setGame(game);
            eventBus.fireEvent(new StartPlayEvent(isWhite()));
          }
        }).saveOrCreate(game);
      }

      @Override
      public void canceled() {
        GameMessage message = createSendGameMessage(gameMessage);
        message.setMessageType(GameMessage.MessageType.PLAY_REJECT_INVITE);

        sendGameMessage(message);
      }
    };
    confirmPlayDialogBox.show(gameMessage.getMessage(), gameMessage.getSender(),
        Boolean.valueOf(gameMessage.getData()));
  }

  @Override
  public void onConnect() {
    if (player == null) {
      InfoDialogBox.setMessage(messages.failToConnectToServer()).show();
      return;
    }
    GameMessage gameMessage = GWT.create(GameMessage.class);
    gameMessage.setSender(player);
    gameMessage.setMessageType(GameMessage.MessageType.PLAYER_REGISTER);

    sendGameMessage(gameMessage);

    playSession.setPlayer(player);
    playSession.setConnected(true);
    eventBus.fireEvent(new ConnectedToPlayEvent());
  }

  @Override
  public void onDisconnect() {
    playSession.setConnected(false);
    eventBus.fireEvent(new DisconnectFromPlayEvent());
  }

  @Override
  public void onMessage(String message) {
    GameMessage gameMessage = messageMapper.read(message);
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
      case PLAY_REJECT_INVITE:
        handlePlayRejectInvite(gameMessage);
        break;
      case PLAY_ALREADY_PLAYING:
        handlePlayAlreadyPlaying(gameMessage);
        break;
      case PLAY_MOVE:
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
      case PLAY_CALLBACK:
        handlePlayCallback(gameMessage);
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

  private void handleNotification(GameMessage gameMessage) {
    Growl.growlNotif(gameMessage.getMessage());
  }

  private void handlePlayEndGame(GameMessage gameMessage) {
    if (getGame() != null) {
      Game game = getGame();
      final Game.GameEnds gameEnd = isPlayerHasWhiteColor() ? Game.GameEnds.BLACK_LEFT : Game.GameEnds.WHITE_LEFT;
      eventBus.fireEvent(new GameOverEvent(game, gameEnd, new AsyncCallback<Game>() {
        @Override
        public void onFailure(Throwable throwable) {
          ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), throwable).show();
        }

        @Override
        public void onSuccess(Game aVoid) {
          Growl.growlNotif(messages.opponentLeftGame());
        }
      }));
    }
  }

  private void handlePlayCallback(GameMessage gameMessage) {
    // получается в PlayComponentPresenter
    eventBus.fireEvent(new PlayCallbackEvent());
  }

  private void handlePlayAlreadyPlaying(GameMessage gameMessage) {
    eventBus.fireEvent(new HideInviteDialogBoxEvent());
    Growl.growlNotif(messages.playAlreadyPlaying(gameMessage.getSender().getPublicName()));
  }

  /**
   * Обработчик ответа на отмену хода. Если оппонент подтвердил, тогда перемещаем его шашку.
   *
   * @param gameMessage
   */
  private void handlePlayCancelMoveResponse(GameMessage gameMessage) {
    boolean isAcceptedCancelMove = Boolean.valueOf(gameMessage.getData());
    if (isAcceptedCancelMove) {
      final Move move = gameMessage.getMove();
      eventBus.fireEvent(new PlayMoveCancelEvent(move));
    } else {
      InfoDialogBox.setMessage(messages.playerRejectedMoveCancel(gameMessage.getSender().getPublicName())).show();
    }
  }

  /**
   * Вопрос на строне оппонента о том, что ему предлагается отменить ход. Если он соглашается, то он двигает шашку
   * оппонента
   *
   * @param gameMessage
   */
  private void handlePlayCancelMove(final GameMessage gameMessage) {
    new ConfirmeDialogBox(messages.playerProposesCancelMove(gameMessage.getSender().getPublicName())) {
      @Override
      public void procConfirm() {
        GameMessage returnGameMessage = createSendGameMessage(gameMessage);
        returnGameMessage.setMessageType(GameMessage.MessageType.PLAY_CANCEL_MOVE_RESPONSE);
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

  private void handlePlayAcceptDraw(GameMessage gameMessage) {
    if (Boolean.valueOf(gameMessage.getData())) {
      eventBus.fireEvent(new GameOverEvent(playSession.getGame(), Game.GameEnds.DRAW, new AsyncCallback<Game>() {
        @Override
        public void onFailure(Throwable throwable) {
          ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), throwable).show();
        }

        @Override
        public void onSuccess(Game aVoid) {
        }
      }));
    } else {
      String senderName = gameMessage.getSender().getPublicName();
      InfoDialogBox.setMessage(messages.playerRejectedDraw(senderName)).show();
    }
  }

  private void handlePlayProposeDraw(final GameMessage gameMessage) {
    String senderName = gameMessage.getSender().getPublicName();
    new ConfirmeDialogBox(messages.playerProposesDraw(senderName)) {
      @Override
      public void procConfirm() {
        GameMessage message = createSendGameMessage(gameMessage);
        message.setMessageType(GameMessage.MessageType.PLAY_ACCEPT_DRAW);

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

  private GameMessage createSendGameMessage(GameMessage gameMessage) {
    GameMessage message = GWT.create(GameMessage.class);
    message.setSender(gameMessage.getReceiver());
    message.setReceiver(gameMessage.getSender());
    return message;
  }

  private void handlePlaySurrender(GameMessage gameMessage) {
    Game game = playSession.getGame();
    // так как сохраняем на противоположной строне, игроки черный-белый переставлены
    final Game.GameEnds gameEnd = playSession.isPlayerHasWhiteColor() ? Game.GameEnds.SURRENDER_BLACK
        : Game.GameEnds.SURRENDER_WHITE;
    eventBus.fireEvent(new GameOverEvent(game, gameEnd, new AsyncCallback<Game>() {
      @Override
      public void onFailure(Throwable throwable) {
        ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), throwable).show();
      }

      @Override
      public void onSuccess(Game aVoid) {
        InfoDialogBox.setMessage(messages.opponentSurrendered()).show();
      }
    }));
  }

  /**
   * Обрабатываем ход оппонента
   *
   * @param gameMessage
   */
  private void handlePlayMove(GameMessage gameMessage) {
    // отправлем отраженный ход здесь
    final Move move = gameMessage.getMove();
    eventBus.fireEvent(new PlayMoveOpponentEvent(move));
  }

  /**
   * Начало игры на стороне приглашающего
   *
   * @param gameMessage
   */
  private void handlePlayStart(final GameMessage gameMessage) {
    final Game game = gameMessage.getGame();
    playSession.setGame(game);
    boolean white = Boolean.valueOf(gameMessage.getData());
    playSession.setOpponent(white ? game.getPlayerBlack() : game.getPlayerWhite());
    eventBus.fireEvent(new StartPlayEvent(white));
  }

  private void handleChatPrivateMessage(GameMessage gameMessage) {
    eventBus.fireEvent(new ChatMessageEvent(gameMessage.getMessage()));
  }

  private void handlePlayRejectInvite(GameMessage gameMessage) {
    playSession.setOpponent(null);
    Growl.growlNotif(messages.playerRejectedPlayRequest(gameMessage.getSender().getPublicName()));
    eventBus.fireEvent(new RejectPlayEvent());
  }

  public void connect() {
    webSocket = new WebSocket(GameWebsocket.this);
    webSocket.connect(config.playerWebsocketUrl());
    player = currentSession.getPlayer();
  }

  public void reconnect() {
    Window.alert("RECONNECT");
    removeHandlers();
    webSocket.close();
    webSocket.connect(config.playerWebsocketUrl());
  }

  public boolean isConnected() {
    return playSession.isConnected();
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public Player getOpponent() {
    return playSession.getOpponent();
  }

  public Game getGame() {
    return playSession.getGame();
  }

  public boolean isPlayerHasWhiteColor() {
    return playSession.isPlayerHasWhiteColor();
  }

  public void setOpponent(Player opponent) {
    playSession.setOpponent(opponent);
  }

  public void setGame(Game game) {
    playSession.setGame(game);
  }
}
