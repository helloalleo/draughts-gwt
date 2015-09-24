package online.shashki.rus.client.application.component.play;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import online.shashki.rus.client.application.widget.NotationPanel;
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;
import online.shashki.rus.client.application.widget.dialog.InfoDialogBox;
import online.shashki.rus.client.application.widget.dialog.InviteDialogBox;
import online.shashki.rus.client.application.widget.dialog.MyDialogBox;
import online.shashki.rus.client.event.*;
import online.shashki.rus.client.rpc.GameRpcServiceAsync;
import online.shashki.rus.client.websocket.GameWebsocket;
import online.shashki.rus.shared.dto.GameMessageDto;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.GameEnds;
import online.shashki.rus.shared.model.GameMessage;
import online.shashki.rus.shared.model.Shashist;

import java.util.Date;
import java.util.List;


public class PlayComponentPresenter extends PresenterWidget<PlayComponentPresenter.MyView>
    implements PlayComponentUiHandlers {

  private int CHECKERS_ON_DESK_INIT = 12;

  private final GameWebsocket gameWebsocket;
  private final ShashkiMessages messages;
  private final GameRpcServiceAsync gameService;
  private EventBus eventBus;
  private InviteDialogBox inviteDialogBox;

  @Inject
  PlayComponentPresenter(
      EventBus eventBus,
      MyView view,
      ShashkiMessages messages,
      GameRpcServiceAsync gameService,
      GameWebsocket gameWebsocket) {
    super(eventBus, view);

    this.eventBus = eventBus;
    this.messages = messages;
    this.gameWebsocket = gameWebsocket;
    this.gameService = gameService;
    getView().setUiHandlers(this);
    getView().setPlayer(gameWebsocket.getPlayer());
    addVisibleHandler(RecivedPlayerListEvent.TYPE, new RecivedPlayerListEventHandler() {
          @Override
          public void onRecivedPlayerList(RecivedPlayerListEvent event) {
            getView().setPlayerList(event.getPlayerList());
          }
        }
    );
  }

  @Override
  protected void onBind() {
    super.onBind();

    bindEvents();
  }

  @Override
  public void startPlayWith(final Shashist opponent) {
    if (opponent == null) {
      InfoDialogBox.setMessage(messages.selectPlayer()).show();
      return;
    }
    if (opponent.getId().equals(gameWebsocket.getPlayer().getId())) {
      InfoDialogBox.setMessage(messages.selectAnotherPlayerItsYou()).show();
      return;
    }
    gameWebsocket.setOpponent(opponent);

    inviteDialogBox = new InviteDialogBox() {
      @Override
      public void submitted(boolean white) {
        GameMessage gameMessage = createSendGameMessage(gameWebsocket);
        gameMessage.setMessageType(GameMessage.MessageType.PLAY_INVITE);

        gameMessage.setMessage(messages.inviteMessage(gameWebsocket.getPlayer().getPublicName(),
            String.valueOf(white ? messages.black() : messages.white())));
        gameMessage.setData(String.valueOf(!white));

        eventBus.fireEvent(new GameMessageEvent(gameMessage));
      }
    };
    inviteDialogBox.show(messages.inviteToPlay(gameWebsocket.getOpponent().getPublicName(),
        messages.draughts()));
  }

  @Override
  public void refreshConnectionToServer() {
    if (gameWebsocket.isConnected()) {
      gameWebsocket.reconnect();
    } else {
      gameWebsocket.connect();
    }
  }

  @Override
  public boolean isMyTurn() {
    return gameWebsocket.getGame().getPlayerWhite().getId()
        .equals(gameWebsocket.getPlayer().getId());
  }

  private GameMessage createSendGameMessage(GameWebsocket gameWebsocket) {
    GameMessage gameMessage = GWT.create(GameMessageDto.class);
    gameMessage.setSender(gameWebsocket.getPlayer());
    gameMessage.setReceiver(gameWebsocket.getOpponent());
    gameMessage.setGame(gameWebsocket.getGame());
    return gameMessage;
  }

  public void bindEvents() {
    eventBus.addHandler(RecivedPlayerListEvent.TYPE, new RecivedPlayerListEventHandler() {
      @Override
      public void onRecivedPlayerList(RecivedPlayerListEvent event) {
        if (!event.getPlayerList().contains(gameWebsocket.getOpponent()) && gameWebsocket.getGame() != null) {
          Game game = gameWebsocket.getGame();
          game.setPlayEndStatus(gameWebsocket.isPlayerHasWhiteColor() ? GameEnds.BLACK_LEFT : GameEnds.WHITE_LEFT);
          game.setPlayEndDate(new Date());
          gameService.saveGame(game, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
              ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), throwable).show();
            }

            @Override
            public void onSuccess(Void aVoid) {
              new MyDialogBox(messages.info(), messages.opponentLeftGame()) {
                @Override
                public void submit() {
                  eventBus.fireEvent(new ClearPlayComponentEvent());
                }
              };
            }
          });
          return;
        }
        getView().setPlayerList(event.getPlayerList());
      }
    });

    eventBus.addHandler(ConnectedToPlayEvent.TYPE, new ConnectedToPlayEventHandler() {
      @Override
      public void onConnectedToPlay(ConnectedToPlayEvent event) {
        getView().toggleInPlayButton();
      }
    });

    eventBus.addHandler(DisconnectFromPlayEvent.TYPE, new DisconnectFromPlayEventHandler() {
      @Override
      public void onDisconnectFromPlay(DisconnectFromPlayEvent event) {
        getView().setUpViewOnDisconnectFromServer();
      }
    });

    eventBus.addHandler(StartPlayEvent.TYPE, new StartPlayEventHandler() {
      @Override
      public void onStartPlay(StartPlayEvent event) {
        if (inviteDialogBox != null) {
          inviteDialogBox.hide();
        }
        getView().startPlay(event.isWhite());
      }
    });

    eventBus.addHandler(RejectPlayEvent.TYPE, new RejectPlayEventHandler() {
      @Override
      public void onRejectPlay(RejectPlayEvent event) {
        inviteDialogBox.hide();
      }
    });

    eventBus.addHandler(TurnChangeEvent.TYPE, new TurnChangeEventHandler() {
      @Override
      public void onTurnChange(TurnChangeEvent event) {
        getView().updateTurn(event.isMyTurn());
      }
    });

    eventBus.addHandler(CheckWinnerEvent.TYPE, new CheckWinnerEventHandler() {
      @Override
      public void onCheckWinner(CheckWinnerEvent event) {
        getView().setBeatenMy(CHECKERS_ON_DESK_INIT - getView().getMyDraughtsSize());
        getView().setBeatenOpponent(CHECKERS_ON_DESK_INIT - getView().getOpponentDraughtsSize());
        final Game endGame = gameWebsocket.getGame();
        if (0 == getView().getMyDraughtsSize()) {
          InfoDialogBox.setMessage(messages.youLose()).show();
          if ((getView().isWhite())) {
            endGame.setPlayEndStatus(GameEnds.BLACK_WON);
          } else {
            endGame.setPlayEndStatus(GameEnds.WHITE_WON);
          }
        }
        if (0 == getView().getOpponentDraughtsSize()) {
          InfoDialogBox.setMessage(messages.youWon()).show();
          if (getView().isWhite()) {
            endGame.setPlayEndStatus(GameEnds.WHITE_WON);
          } else {
            endGame.setPlayEndStatus(GameEnds.BLACK_WON);
          }
        }
        if (endGame.getPlayEndStatus() == null) {
          return;
        }
        gameService.getGame(endGame.getId(), new AsyncCallback<Game>() {
          @Override
          public void onFailure(Throwable throwable) {
            ErrorDialogBox.setMessage(messages.errorWhileGettingGame(), throwable).show();
          }

          @Override
          public void onSuccess(Game game) {
            if (game.getPlayEndStatus() == null) {
              endGame.setPartyNotation(NotationPanel.getNotation());
              endGame.setPlayEndDate(new Date());
              gameService.saveGame(endGame, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                  ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), caught).show();
                }

                @Override
                public void onSuccess(Void result) {
                  eventBus.fireEvent(new ClearPlayComponentEvent());
                }
              });
            }
          }
        });
      }
    });

    eventBus.addHandler(ClearPlayComponentEvent.TYPE, new ClearPlayComponentEventHandler() {
      @Override
      public void onClearPlayComponent(ClearPlayComponentEvent event) {
        getView().clearPlayComponent();
        getView().hidePlayingButtonsAndShowPlayButton();
      }
    });

    eventBus.addHandler(HideInviteDialogBoxEvent.TYPE, new HideInviteDialogBoxEventHandler() {
      @Override
      public void onHideInviteDialogBox(HideInviteDialogBoxEvent event) {
        if (inviteDialogBox != null && inviteDialogBox.isVisible()) {
          inviteDialogBox.hide();
        }
      }
    });
  }

  interface MyView extends View, HasUiHandlers<PlayComponentUiHandlers> {
    void setPlayerList(List<Shashist> shashistList);

    void setPlayer(Shashist player);

    void toggleInPlayButton();

    void setUpViewOnDisconnectFromServer();

    void hidePlayButtonAndShowPlayingButtons();

    void clearPlayComponent();

    void hidePlayingButtonsAndShowPlayButton();

    void setBeatenMy(int num);

    void setBeatenOpponent(int num);

    void startPlay(boolean white);

    void updateTurn(boolean myTurn);

    int getMyDraughtsSize();

    int getOpponentDraughtsSize();

    boolean isWhite();
  }
}
