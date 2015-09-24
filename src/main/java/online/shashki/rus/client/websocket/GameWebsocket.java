package online.shashki.rus.client.websocket;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.websockets.client.WebSocket;
import com.google.gwt.websockets.client.WebSocketCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import online.shashki.rus.client.application.widget.dialog.*;
import online.shashki.rus.client.event.*;
import online.shashki.rus.client.json.GameMessageMapper;
import online.shashki.rus.client.rpc.GameRpcServiceAsync;
import online.shashki.rus.client.rpc.ProfileRpcServiceAsync;
import online.shashki.rus.shared.config.ShashkiConfiguration;
import online.shashki.rus.shared.dto.GameDto;
import online.shashki.rus.shared.dto.GameMessageDto;
import online.shashki.rus.shared.dto.MoveDto;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.*;

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
  private ProfileRpcServiceAsync profileService;
  private ShashkiConfiguration configuration = GWT.create(ShashkiConfiguration.class);
  private GameRpcServiceAsync gameService;
  private WebSocket webSocket;
  private EventBus eventBus;
  private Shashist player;
  private ShashkiMessages messages;
  private ConfirmPlayDialogBox confirmPlayDialogBox;
  private ConnectionSession connectionSession = new ConnectionSession();
  private GameMessageMapper gameMessageMapper = GWT.create(GameMessageMapper.class);

  @Inject
  private GameWebsocket(EventBus eventBus,
                        ShashkiMessages messages,
                        GameRpcServiceAsync gameService,
//                        JsonSerialization jsonSerialization,
                        ProfileRpcServiceAsync profileService) {
    GWT.log("GAME WS");
    profileService.getCurrentProfile(new AsyncCallback<Shashist>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
      }

      @Override
      public void onSuccess(Shashist result) {
        player = result;
        if (player == null) {
          throw new RuntimeException("Player not found");
        }
      }
    });
    GWT.log(eventBus == null ? "NULL EVENT BUS" : "OK EVENT BUS");
    this.profileService = profileService;
    this.eventBus = eventBus;
    this.messages = messages;
    this.gameService = gameService;
//    this.jsonSerialization = jsonSerialization;
    this.profileService = profileService;

    bindEvents();
  }

  private void bindEvents() {
    HandlerRegistration gameMessageHR = eventBus.addHandler(GameMessageEvent.TYPE, new GameMessageEventHandler() {
      @Override
      public void onPlayerMessage(GameMessageEvent event) {
        GameMessageDto gameMessage = (GameMessageDto) event.getGameMessage();

        sendGameMessage(gameMessage);
      }
    });

    HandlerRegistration playMoveHR = eventBus.addHandler(PlayMoveMessageEvent.TYPE, new PlayMoveMessageEventHandler() {
      @Override
      public void onPlayMoveMessage(PlayMoveMessageEvent event) {
        GameMessageDto message = createSendGameMessage();
        message.setMessageType(Message.MessageType.PLAY_MOVE);
        message.setMove(event.getMove());
        message.setGame(connectionSession.getGame());

        sendGameMessage(message);
      }
    });

    HandlerRegistration updatePlayerListHR = eventBus.addHandler(UpdatePlayerListEvent.TYPE, new UpdatePlayerListEventHandler() {
      @Override
      public void onUpdatePlayerList(UpdatePlayerListEvent event) {
        GWT.log("UPDATE PLAYER LIST");
        GameMessageDto message = createSendGameMessage();
        message.setMessageType(Message.MessageType.USER_LIST_UPDATE);
        sendGameMessage(message);
      }
    });

    eventBus.addHandler(RemovePlayMoveOpponentHandlerEvent.TYPE, new RemoveWebsocketHandlersEventHandler() {
      @Override
      public void onRemovePlayMoveOpponentHandler(RemovePlayMoveOpponentHandlerEvent event) {
        removeHandlers();
      }
    });
  }

  private void removeHandlers() {
//    playMoveHR.removeHandler();
//    updatePlayerListHR.removeHandler();
//    connectToPlayHR.removeHandler();
//    gameMessageHR.removeHandler();
  }

  private GameMessageDto createSendGameMessage() {
    GameMessageDto message = GWT.create(GameMessageDto.class);
    message.setSender(connectionSession.getPlayer());
    message.setReceiver(connectionSession.getOpponent());
    return message;
  }

  private void sendGameMessage(GameMessageDto gameMessage) {
//    MessageFactory chatFactory = GWT.create(MessageFactory.class);
//    AutoBean<GameMessage> bean = chatFactory.create(GameMessage.class, gameMessage);
//    String message = AutoBeanCodex.encode(bean).getPayload();
    String message = gameMessageMapper.write(gameMessage);
//    SerializedValue message = jsonSerialization.serialize("json", null, gameMessage);
    webSocket.send(message);
  }

  private void handleUpdatePlayerList(List<Shashist> playerList) {
    connectionSession.setPlayerList(playerList);
    eventBus.fireEvent(new RecivedPlayerListEvent(playerList));
  }

  private void handlePlayInvite(final GameMessage gameMessage) {
    if (confirmPlayDialogBox != null && confirmPlayDialogBox.isShowing()) {
      GameMessageDto message = createSendGameMessage(gameMessage);
      message.setMessageType(Message.MessageType.PLAY_ALREADY_PLAYING);
      sendGameMessage(message);
      return;
    }
    confirmPlayDialogBox = new ConfirmPlayDialogBox() {
      @Override
      public void submitted() {
        profileService.getProfile(gameMessage.getSender().getId(), new AsyncCallback<Shashist>() {
          @Override
          public void onFailure(Throwable caught) {
            ErrorDialogBox.setMessage(messages.errorWhileGettingProfile(), caught).show();
          }

          @Override
          public void onSuccess(Shashist result) {
            if (result == null) {
              InfoDialogBox.setMessage(messages.opponentNotFound()).show();
              ;
              return;
            }

            connectionSession.setOpponent(result);

            Game game = GWT.create(GameDto.class);
            game.setPlayStartDate(new Date());
            game.setPlayerWhite(isWhite() ? connectionSession.getPlayer() : connectionSession.getOpponent());
            game.setPlayerBlack(isWhite() ? connectionSession.getOpponent() : connectionSession.getPlayer());
            GWT.log(game.getPlayerWhite().getPublicName() + " PLAYER WHITE");
            GWT.log(game.getPlayerBlack().getPublicName() + " PLAYER BLACK");
            gameService.createGame(game, new AsyncCallback<Game>() {
              @Override
              public void onFailure(Throwable throwable) {
                ErrorDialogBox.setMessage(messages.failToStartGame(), throwable).show();
              }

              @Override
              public void onSuccess(Game game) {
                GameMessageDto message = createSendGameMessage(gameMessage);
                message.setMessageType(Message.MessageType.PLAY_START);

                message.setData(Boolean.TRUE.toString());
                message.setGame(game);

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
        GameMessageDto message = createSendGameMessage(gameMessage);
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
    GameMessageDto gameMessage = GWT.create(GameMessageDto.class);
    gameMessage.setSender(player);
    gameMessage.setMessageType(GameMessage.MessageType.PLAYER_REGISTER);

    sendGameMessage(gameMessage);

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
    GWT.log(message);
    GameMessageDto gameMessage = gameMessageMapper.read(message);
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
    new MyDialogBox(messages.info(), messages.playAlreadyPlaying(gameMessage.getSender().getPublicName()));
  }

  /**
   * Обработчик ответа на отмену хода. Если оппонент подтвердил, тогда перемещаем его шашку.
   *
   * @param gameMessage
   */
  private void handlePlayCancelMoveResponse(GameMessage gameMessage) {
    boolean isAcceptedCancelMove = Boolean.valueOf(gameMessage.getData());
    if (isAcceptedCancelMove) {
      final MoveDto move = new MoveDto(gameMessage.getMove()).mirror();
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
        GameMessageDto returnGameMessage = createSendGameMessage(gameMessage);
        returnGameMessage.setMessageType(Message.MessageType.PLAY_CANCEL_MOVE_RESPONSE);
        returnGameMessage.setMove(gameMessage.getMove());
        if (isConfirmed()) {
          returnGameMessage.setData(Boolean.TRUE.toString());
          eventBus.fireEvent(new PlayMoveOpponentCancelEvent(new MoveDto(gameMessage.getMove())));
        } else {
          returnGameMessage.setData(Boolean.FALSE.toString());
        }
        sendGameMessage(returnGameMessage);
      }
    };
  }

  private void handlePlayAcceptDraw(GameMessage gameMessage) {
    if (Boolean.valueOf(gameMessage.getData())) {
      Game game = connectionSession.getGame();
      game.setPlayEndDate(new Date());
      game.setPlayEndStatus(GameEnds.DRAW);
      gameService.saveGame(game, new AsyncCallback<Void>() {
        @Override
        public void onFailure(Throwable throwable) {
          ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), throwable).show();
        }

        @Override
        public void onSuccess(Void aVoid) {
          eventBus.fireEvent(new ClearPlayComponentEvent());
        }
      });
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
        GameMessageDto message = createSendGameMessage(gameMessage);
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

  private GameMessageDto createSendGameMessage(GameMessage gameMessage) {
    GameMessageDto message = GWT.create(GameMessageDto.class);
    message.setSender(gameMessage.getReceiver());
    message.setReceiver(gameMessage.getSender());
    return message;
  }

  private void handlePlaySurrender(GameMessage gameMessage) {
    Game game = connectionSession.getGame();
    game.setPlayEndDate(new Date());
    game.setPlayEndStatus(connectionSession.isPlayerHasWhiteColor() ? GameEnds.SURRENDER_WHITE
        : GameEnds.SURRENDER_BLACK);
    gameService.saveGame(game, new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable throwable) {
        ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), throwable).show();
      }

      @Override
      public void onSuccess(Void aVoid) {
        new MyDialogBox(messages.info(), messages.opponentSurrendered());
        eventBus.fireEvent(new ClearPlayComponentEvent());
      }
    });
  }

  private void handlePlayMove(GameMessage gameMessage) {
    GWT.log(gameMessage.getMove().toString());
    // отправлем отраженный ход здесь
    final MoveDto moveDto = new MoveDto(gameMessage.getMove()).mirror();
    eventBus.fireEvent(new PlayMoveOpponentEvent(moveDto));
  }

  private void handlePlayStart(final GameMessage gameMessage) {
    gameService.getGame(gameMessage.getGame().getId(), new AsyncCallback<Game>() {
      @Override
      public void onFailure(Throwable throwable) {
        ErrorDialogBox.setMessage(messages.errorWhileGettingGame(), throwable).show();
      }

      @Override
      public void onSuccess(Game game) {
        connectionSession.setGame(game);
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

  public Shashist getPlayer() {
    return player;
  }

  public void setPlayer(Shashist player) {
    this.player = player;
  }

  public void setOpponent(Shashist opponent) {
    connectionSession.setOpponent(opponent);
  }

  public Shashist getOpponent() {
    return connectionSession.getOpponent();
  }

  public Game getGame() {
    return connectionSession.getGame();
  }

  public boolean isPlayerHasWhiteColor() {
    return connectionSession.isPlayerHasWhiteColor();
  }
}
