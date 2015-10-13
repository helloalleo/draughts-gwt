package online.shashki.rus.client.websocket;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.websockets.client.WebSocket;
import com.google.gwt.websockets.client.WebSocketCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import online.shashki.rus.client.application.widget.dialog.*;
import online.shashki.rus.client.event.*;
import online.shashki.rus.client.json.GameMessageMapper;
import online.shashki.rus.client.util.SHLog;
import online.shashki.rus.shared.config.ShashkiConfiguration;
import online.shashki.rus.shared.dispatch.FetchCurrentPlayerAction;
import online.shashki.rus.shared.dispatch.FetchCurrentPlayerResult;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.*;
import online.shashki.rus.shared.service.GameService;
import online.shashki.rus.shared.service.GameServiceAsync;
import online.shashki.rus.shared.service.PlayerServiceAsync;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 11:39
 */
public class GameWebsocket implements WebSocketCallback {

//  private final JsonSerialization jsonSerialization;
  private PlayerServiceAsync playrService;
  private ShashkiConfiguration configuration = GWT.create(ShashkiConfiguration.class);
  private GameServiceAsync gameService;
  private WebSocket webSocket;
  private EventBus eventBus;
  private Player player;
  private ShashkiMessages messages;
  private ConfirmPlayDialogBox confirmPlayDialogBox;
  private ConnectionSession connectionSession = new ConnectionSession();
  private GameMessageMapper gameMessageMapper = GWT.create(GameMessageMapper.class);

  @Inject
  private GameWebsocket(EventBus eventBus,
                        DispatchAsync dispatcher,
                        ShashkiMessages messages) {
    SHLog.debug("GAME WS");
    dispatcher.execute(new FetchCurrentPlayerAction(), new AsyncCallback<FetchCurrentPlayerResult>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
      }

      @Override
      public void onSuccess(FetchCurrentPlayerResult result) {
        player = result.getPlayer();
      }
    });
//    this.playrService = PlayerService.App.getInstance();
//    playrService.getCurrentProfile(new AsyncCallback<Player>() {
//      @Override
//      public void onFailure(Throwable caught) {
//        ErrorDialogBox.setMessage(caught).show();
//      }
//
//      @Override
//      public void onSuccess(Player result) {
//        player = result;
//      }
//    });
    SHLog.debug(eventBus == null ? "NULL EVENT BUS" : "OK EVENT BUS");
    this.eventBus = eventBus;
    this.messages = messages;
    this.gameService = GameService.App.getInstance();

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
        message.setGame(connectionSession.getGame());

        sendGameMessage(message);
      }
    });

    HandlerRegistration updatePlayerListHR = eventBus.addHandler(UpdatePlayerListEvent.TYPE, new UpdatePlayerListEventHandler() {
      @Override
      public void onUpdatePlayerList(UpdatePlayerListEvent event) {
        SHLog.debug("UPDATE PLAYER LIST");
        GameMessage message = createSendGameMessage();
        message.setMessageType(GameMessage.MessageType.USER_LIST_UPDATE);
        sendGameMessage(message);
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
        connectionSession.setGame(null);
      }
    });
  }

  private void removeHandlers() {
//    playMoveHR.removeHandler();
//    updatePlayerListHR.removeHandler();
//    connectToPlayHR.removeHandler();
//    gameMessageHR.removeHandler();
  }

  private GameMessage createSendGameMessage() {
    GameMessage message = GWT.create(GameMessage.class);
    message.setSender(connectionSession.getPlayer());
    message.setReceiver(connectionSession.getOpponent());
    return message;
  }

  private void sendGameMessage(GameMessage gameMessage) {
    if (gameMessage.getMove() != null) {
      SHLog.debug(gameMessage.getMove().toString() + " SEND MOVE");
    }
    String message = gameMessageMapper.write(gameMessage);
    webSocket.send(message);
  }

  private void handleUpdatePlayerList(List<Player> playerList) {
    SHLog.debug(playerList.size() + " PLAYER LIST SIZE");
    eventBus.fireEvent(new ReceivedPlayerListEvent(playerList));
  }

  /**
   * Начало игры на стороне приглашенного
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
        playrService.find(gameMessage.getSender().getId(), new AsyncCallback<Player>() {
          @Override
          public void onFailure(Throwable caught) {
            ErrorDialogBox.setMessage(messages.errorWhileGettingProfile(), caught).show();
          }

          @Override
          public void onSuccess(Player result) {
            if (result == null) {
              InfoDialogBox.setMessage(messages.opponentNotFound()).show();
              return;
            }
            SHLog.debug(connectionSession.getPlayer().toString());

            connectionSession.setOpponent(result);

            Game game = GWT.create(Game.class);
            game.setPlayStartDate(new Date());
            game.setPlayerWhite(isWhite() ? connectionSession.getPlayer() : connectionSession.getOpponent());
            game.setPlayerBlack(isWhite() ? connectionSession.getOpponent() : connectionSession.getPlayer());
            SHLog.debug(game.getPlayerWhite().getPublicName() + " PLAYER WHITE");
            SHLog.debug(game.getPlayerBlack().getPublicName() + " PLAYER BLACK");
            gameService.save(game, new AsyncCallback<Game>() {
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

                connectionSession.setGame(game);
                eventBus.fireEvent(new StartPlayEvent(isWhite()));
              }
            });
          }
        });
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

    connectionSession.setPlayer(player);
    connectionSession.setConnected(true);
    eventBus.fireEvent(new ConnectedToPlayEvent());
  }

  @Override
  public void onDisconnect() {
    connectionSession.setConnected(false);
    eventBus.fireEvent(new DisconnectFromPlayEvent());
  }

  @Override
  public void onMessage(String message) {
//    MessageFactory messageFactory = GWT.create(MessageFactory.class);
//    AutoBean<GameMessage> bean = AutoBeanCodex.decode(messageFactory, GameMessage.class, message);
//    GameMessage gameMessage = bean.as();
    SHLog.debug(message);
    GameMessage gameMessage = gameMessageMapper.read(message);
//    GameMessage gameMessage = jsonSerialization.deserialize("json", null, message);
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
      case CHAT_PRIVATE_MESSAGE:
        handleChatPrivateMessage(gameMessage);
        break;
    }
  }

  private void handlePlayAlreadyPlaying(GameMessage gameMessage) {
    eventBus.fireEvent(new HideInviteDialogBoxEvent());
    InfoDialogBox.setMessage(messages.playAlreadyPlaying(gameMessage.getSender().getPublicName())).show();
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
      new MyDialogBox(messages.info(), messages.playerRejectedMoveCancel(gameMessage.getSender().getPublicName()));
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
      eventBus.fireEvent(new GameOverEvent(connectionSession.getGame(), GameEnds.DRAW, new AsyncCallback<Game>() {
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
      new MyDialogBox(messages.info(), messages.playerRejectedDraw(senderName));
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
    Game game = connectionSession.getGame();
    final GameEnds gameEnd = connectionSession.isPlayerHasWhiteColor() ? GameEnds.SURRENDER_WHITE
        : GameEnds.SURRENDER_BLACK;
    eventBus.fireEvent(new GameOverEvent(game, gameEnd, new AsyncCallback<Game>() {
      @Override
      public void onFailure(Throwable throwable) {
        ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), throwable).show();
      }

      @Override
      public void onSuccess(Game aVoid) {
        new MyDialogBox(messages.info(), messages.opponentSurrendered());
      }
    }));
  }

  /**
   * Обрабатываем ход оппонента
   * @param gameMessage
   */
  private void handlePlayMove(GameMessage gameMessage) {
    SHLog.debug(gameMessage.getMove().toString());
    // отправлем отраженный ход здесь
    final Move move = gameMessage.getMove();
    eventBus.fireEvent(new PlayMoveOpponentEvent(move));
  }

  /**
   * Начало игры на стороне приглашающего
   * @param gameMessage
   */
  private void handlePlayStart(final GameMessage gameMessage) {
    gameService.find(gameMessage.getGame().getId(), new AsyncCallback<Game>() {
      @Override
      public void onFailure(Throwable throwable) {
        ErrorDialogBox.setMessage(messages.errorWhileGettingGame(), throwable).show();
      }

      @Override
      public void onSuccess(Game game) {
        connectionSession.setGame(game);
        SHLog.debug(gameMessage.getData() + " RECEIVED DATA");
        boolean white = Boolean.valueOf(gameMessage.getData());
        connectionSession.setOpponent(white ? game.getPlayerBlack() : game.getPlayerWhite());
        eventBus.fireEvent(new StartPlayEvent(white));
      }
    });
  }

  private void handleChatPrivateMessage(GameMessage gameMessage) {
    eventBus.fireEvent(new ChatMessageEvent(gameMessage.getMessage()));
  }

  private void handlePlayRejectInvite(GameMessage gameMessage) {
    connectionSession.setOpponent(null);
    new MyDialogBox(messages.info(),
        messages.playerRejectedPlayRequest(gameMessage.getSender().getPublicName()))
        .show();
    eventBus.fireEvent(new RejectPlayEvent());
  }

  public void connect() {
    webSocket = new WebSocket(GameWebsocket.this);
    webSocket.connect(configuration.playerWebsocketUrl());
  }

  public void reconnect() {
    removeHandlers();
    webSocket.close();
    webSocket.connect(configuration.playerWebsocketUrl());
  }

  public boolean isConnected() {
    return connectionSession.isConnected();
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public Player getOpponent() {
    return connectionSession.getOpponent();
  }

  public Game getGame() {
    return connectionSession.getGame();
  }

  public boolean isPlayerHasWhiteColor() {
    return connectionSession.isPlayerHasWhiteColor();
  }

  public void setOpponent(Player opponent) {
    connectionSession.setOpponent(opponent);
  }

  public void setGame(Game game) {
    connectionSession.setGame(game);
  }
}
