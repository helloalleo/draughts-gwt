package online.shashki.rus.client.websocket;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.websockets.client.WebSocket;
import com.google.gwt.websockets.client.WebSocketCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import online.shashki.rus.client.application.ClientFactory;
import online.shashki.rus.client.application.widget.dialog.ConfirmPlayDialogBox;
import online.shashki.rus.client.application.widget.dialog.ConfirmeDialogBox;
import online.shashki.rus.client.application.widget.dialog.DialogBox;
import online.shashki.rus.client.event.*;
import online.shashki.rus.client.rpc.GameRpcServiceAsync;
import online.shashki.rus.client.rpc.ProfileRpcServiceAsync;
import online.shashki.rus.shared.config.ShashkiConfiguration;
import online.shashki.rus.shared.dto.GameDto;
import online.shashki.rus.shared.dto.GameMessageDto;
import online.shashki.rus.shared.locale.ShashkiConstants;
import online.shashki.rus.shared.model.*;
import online.shashki.rus.shared.websocket.message.MessageFactory;
import online.shashki.rus.shashki.dto.MoveDto;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 11:39
 */
public class GameWebsocket implements WebSocketCallback {

  private final ProfileRpcServiceAsync profileService;
  private final ShashkiConfiguration shashkiConfiguration;
  private GameRpcServiceAsync gameService;
  private WebSocket webSocket;
  private EventBus eventBus;
  private Shashist player;
  private ShashkiConstants constants;
  private ClientFactory clientFactory;
  private ConfirmPlayDialogBox confirmPlayDialogBox;

  @Inject
  private GameWebsocket(ClientFactory clientFactory, EventBus eventBus, ShashkiConstants constants,
                        GameRpcServiceAsync gameService, ProfileRpcServiceAsync profileService,
                        ShashkiConfiguration configuration) {
    this.clientFactory = clientFactory;
    this.player = clientFactory.getPlayer();

    this.eventBus = eventBus;
    this.constants = constants;
    this.gameService = gameService;
    this.profileService = profileService;
    this.shashkiConfiguration = configuration;

    handlers(clientFactory);
  }

  private void handlers(final ClientFactory clientFactory) {
    HandlerRegistration connectToPlayHR = eventBus.addHandler(ConnectToPlayEvent.TYPE, new ConnectToPlayEventHandler() {
      @Override
      public void onConnectToPlay(ConnectToPlayEvent event) {
        webSocket = new WebSocket(GameWebsocket.this);
        webSocket.connect(shashkiConfiguration.playerWebsocketUrl());
      }
    });

    HandlerRegistration gameMessageHR = eventBus.addHandler(GameMessageEvent.TYPE, new GameMessageEventHandler() {
      @Override
      public void onPlayerMessage(GameMessageEvent event) {
        GameMessage gameMessage = event.getGameMessage();

        sendGameMessage(gameMessage);
      }
    });

    HandlerRegistration playMoveHR = eventBus.addHandler(PlayMoveMessageEvent.TYPE, new PlayMoveMessageEventHandler() {
      @Override
      public void onPlayMoveMessage(PlayMoveMessageEvent event) {
        GameMessage message = createSendGameMessage(clientFactory);
        message.setMessageType(Message.MessageType.PLAY_MOVE);
        message.setMove(event.getMove());
        message.setGame(clientFactory.getGame());

        sendGameMessage(message);
      }
    });

    eventBus.addHandler(WebsocketReconnectEvent.TYPE, new WebsocketReconnectEventHandler() {
      @Override
      public void onWebsocketReconnect(WebsocketReconnectEvent event) {
        removeHandlers();
        webSocket.close();
        webSocket.connect(shashkiConfiguration.playerWebsocketUrl());
      }
    });

    HandlerRegistration updatePlayerListHR = eventBus.addHandler(UpdatePlayerListEvent.TYPE, new UpdatePlayerListEventHandler() {
      @Override
      public void onUpdatePlayerList(UpdatePlayerListEvent event) {
        GameMessage message = createSendGameMessage(clientFactory);
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

  private GameMessage createSendGameMessage(ClientFactory clientFactory) {
    GameMessage message = GWT.create(GameMessageDto.class);
    message.setSender(clientFactory.getPlayer());
    message.setReceiver(clientFactory.getOpponent());
    return message;
  }

  private void sendGameMessage(GameMessage gameMessage) {
    MessageFactory chatFactory = GWT.create(MessageFactory.class);
    AutoBean<GameMessage> bean = chatFactory.create(GameMessage.class, gameMessage);
    String message = AutoBeanCodex.encode(bean).getPayload();
    webSocket.send(message);
  }

  private void handleUpdatePlayerList(List<Shashist> playerList) {
    clientFactory.setPlayerList(playerList);
    eventBus.fireEvent(new RecivedPlayerListEvent(playerList));
  }

  private void handlePlayInvite(final GameMessage gameMessage) {
    if (confirmPlayDialogBox != null && confirmPlayDialogBox.isShowing()) {
      GameMessage message = createSendGameMessage(gameMessage);
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
            new DialogBox(constants.error(), constants.errorWhileGettingProfile()).show();
          }

          @Override
          public void onSuccess(Shashist result) {
            clientFactory.setOpponent(result);

            Game game = GWT.create(GameDto.class);
            game.setPlayStartDate(new Date());
            game.setPlayerWhite(isWhite() ? clientFactory.getPlayer() : clientFactory.getOpponent());
            game.setPlayerBlack(isWhite() ? clientFactory.getOpponent() : clientFactory.getPlayer());
            gameService.createGame(game, new AsyncCallback<Game>() {
              @Override
              public void onFailure(Throwable throwable) {
                new DialogBox(constants.error(), constants.failedToStartGame() + throwable.getMessage()).show();
                throwable.printStackTrace();
              }

              @Override
              public void onSuccess(Game game) {
                GameMessage message = createSendGameMessage(gameMessage);
                message.setMessageType(Message.MessageType.PLAY_START);

                message.setData(Boolean.TRUE.toString());
                message.setGame(game);

                sendGameMessage(message);

                clientFactory.setGame(game);
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
    GameMessage gameMessage = GWT.create(GameMessageDto.class);
    gameMessage.setSender(player);
    gameMessage.setMessageType(GameMessage.MessageType.PLAYER_REGISTER);

    sendGameMessage(gameMessage);

    clientFactory.setConnected(true);
    eventBus.fireEvent(new ConnectedToPlayEvent());
  }

  @Override
  public void onDisconnect() {
    clientFactory.setConnected(false);
    eventBus.fireEvent(new DisconnectFromPlayEvent());
  }

  @Override
  public void onMessage(String message) {
    MessageFactory messageFactory = GWT.create(MessageFactory.class);
    AutoBean<GameMessage> bean = AutoBeanCodex.decode(messageFactory, GameMessage.class, message);
    GameMessage gameMessage = bean.as();
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
    new DialogBox(constants.info(), constants.playAlreadyPlaying(gameMessage.getSender().getPublicName()));
  }

  /**
   * Обработчик ответа на отмену хода. Если оппонент подтвердил, тогда перемещаем его шашку.
   * @param gameMessage
   */
  private void handlePlayCancelMoveResponse(GameMessage gameMessage) {
    boolean isAcceptedCancelMove = Boolean.valueOf(gameMessage.getData());
    if (isAcceptedCancelMove) {
      final MoveDto move = new MoveDto(gameMessage.getMove()).mirror();
      eventBus.fireEvent(new PlayMoveCancelEvent(move));
    } else {
      new DialogBox(constants.info(), constants.playerRejectedMoveCancel(gameMessage.getSender().getPublicName()));
    }
  }

  /**
   * Вопрос на строне оппонента о том, что ему предлагается отменить ход. Если он соглашается, то он двигает шашку
   * оппонента
   * @param gameMessage
   */
  private void handlePlayCancelMove(final GameMessage gameMessage) {
    new ConfirmeDialogBox(constants.playerProposesCancelMove(gameMessage.getSender().getPublicName())) {
      @Override
      public void procConfirm() {
        GameMessage returnGameMessage = createSendGameMessage(gameMessage);
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
      Game game = clientFactory.getGame();
      game.setPlayEndDate(new Date());
      game.setPlayEndStatus(GameEnds.DRAW);
      gameService.saveGame(game, new AsyncCallback<Void>() {
        @Override
        public void onFailure(Throwable throwable) {
          new DialogBox(constants.error(), constants.errorWhileSavingGame());
        }

        @Override
        public void onSuccess(Void aVoid) {
          eventBus.fireEvent(new ClearPlayComponentEvent());
        }
      });
    } else {
      String senderName = gameMessage.getSender().getPublicName();
      new DialogBox(constants.info(), constants.playerRejectedDraw(senderName));
    }
  }

  private void handlePlayProposeDraw(final GameMessage gameMessage) {
    String senderName = gameMessage.getSender().getPublicName();
    new ConfirmeDialogBox(constants.playerProposesDraw(senderName)) {
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
    GameMessage message = GWT.create(GameMessageDto.class);
    message.setSender(gameMessage.getReceiver());
    message.setReceiver(gameMessage.getSender());
    return message;
  }

  private void handlePlaySurrender(GameMessage gameMessage) {
    Game game = clientFactory.getGame();
    game.setPlayEndDate(new Date());
    game.setPlayEndStatus(clientFactory.isPlayerHasWhiteColor() ? GameEnds.SURRENDER_WHITE
        : GameEnds.SURRENDER_BLACK);
    gameService.saveGame(game, new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable throwable) {
        new DialogBox(constants.error(), constants.error()).show();
      }

      @Override
      public void onSuccess(Void aVoid) {
        new DialogBox(constants.info(), constants.opponentSurrendered());
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
        new DialogBox(constants.error(), constants.errorWhileGettingGame()).show();
      }

      @Override
      public void onSuccess(Game game) {
        clientFactory.setGame(game);
        boolean white = Boolean.valueOf(gameMessage.getData());
        clientFactory.setOpponent(white ? game.getPlayerBlack() : game.getPlayerWhite());
        eventBus.fireEvent(new StartPlayEvent(white));
      }
    });
  }

  private void handleChatPrivateMessage(GameMessage gameMessage) {
    eventBus.fireEvent(new ChatMessageEvent(gameMessage.getMessage()));
  }

  private void handlePlayRejectInvite(GameMessage gameMessage) {
    clientFactory.setOpponent(null);
    new DialogBox(constants.info(),
        constants.playerRejectedPlayRequest(gameMessage.getSender().getPublicName()))
        .show();
    eventBus.fireEvent(new RejectPlayEvent());
  }
}
