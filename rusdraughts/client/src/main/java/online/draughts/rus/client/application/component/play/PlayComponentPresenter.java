package online.draughts.rus.client.application.component.play;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import online.draughts.rus.client.application.widget.NotationPanel;
import online.draughts.rus.client.application.widget.dialog.ErrorDialogBox;
import online.draughts.rus.client.application.widget.dialog.InfoDialogBox;
import online.draughts.rus.client.application.widget.growl.Growl;
import online.draughts.rus.client.event.*;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.client.util.DTLog;
import online.draughts.rus.client.websocket.GameWebsocket;
import online.draughts.rus.draughts.MoveFactory;
import online.draughts.rus.draughts.Stroke;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.model.*;
import online.draughts.rus.shared.rest.FriendsResource;
import online.draughts.rus.shared.rest.GamesResource;
import online.draughts.rus.shared.rest.PlayersResource;

import java.util.Date;
import java.util.List;


public class PlayComponentPresenter extends PresenterWidget<PlayComponentPresenter.MyView>
    implements PlayComponentUiHandlers {

  private int DRAUGHTS_ON_DESK_INIT = 12;

  private final GameWebsocket gameWebsocket;
  private final DraughtsMessages messages;
  private final ResourceDelegate<GamesResource> gamesDelegate;
  private final ResourceDelegate<PlayersResource> playersDelegate;
  private final ResourceDelegate<FriendsResource> friendsDelegate;
  private final EventBus eventBus;

  @Inject
  PlayComponentPresenter(
      EventBus eventBus,
      MyView view,
      DraughtsMessages messages,
      ResourceDelegate<GamesResource> gamesDelegate,
      ResourceDelegate<PlayersResource> playersDelegate,
      ResourceDelegate<FriendsResource> friendsDelegate,
      GameWebsocket gameWebsocket) {
    super(eventBus, view);

    this.eventBus = eventBus;
    this.messages = messages;
    this.gameWebsocket = gameWebsocket;
    this.gamesDelegate = gamesDelegate;
    this.playersDelegate = playersDelegate;
    this.friendsDelegate = friendsDelegate;

    getView().setUiHandlers(this);
    getView().initNotationPanel(eventBus);
  }

  @Override
  protected void onBind() {
    super.onBind();

    bindEvents();
  }

  @Override
  public void startPlayWith(final Player opponent) {
    if (opponent == null) {
      Growl.growlNotif(messages.selectPlayer());
      return;
    }
    if (opponent.getId().equals(gameWebsocket.getPlayer().getId())) {
      Growl.growlNotif(messages.selectAnotherPlayerItsYou());
      return;
    }
    if (!opponent.isOnline()) {
      Growl.growlNotif(messages.playerOffline());
      return;
    }
    getView().setOpponent(opponent);

    getView().showInviteDialog(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        GameMessage gameMessage = createGameMessage();
        gameMessage.setMessageType(GameMessage.MessageType.PLAY_INVITE);
        gameMessage.setReceiver(opponent);

        final boolean white = getView().opponentColor();
        DTLog.debug(white + " OPPONENT COLOR");
        gameMessage.setMessage(messages.inviteMessage(gameWebsocket.getPlayer().getPublicName(),
            String.valueOf(white ? messages.white() : messages.black())));
        gameMessage.setData(String.valueOf(white));

        fireEvent(new GameMessageEvent(gameMessage));
      }
    });
  }

  @Override
  public void refreshConnectionToServer() {
    if (gameWebsocket.isConnected()) {
      gameWebsocket.reconnect();
    } else {
      gameWebsocket.connect();
    }
    getView().setPlayer(gameWebsocket.getPlayer());
    DTLog.debug("Set player for Play View: " + gameWebsocket.getPlayer());
  }

  @Override
  public boolean isMyTurn() {
    return gameWebsocket.getGame().getPlayerWhite().getId()
        .equals(gameWebsocket.getPlayer().getId());
  }

  @Override
  public EventBus getPlayEventBus() {
    return eventBus;
  }

  @Override
  public void proposeDraw() {
    GameMessage gameMessage = createGameMessage();
    gameMessage.setMessageType(GameMessage.MessageType.PLAY_PROPOSE_DRAW);
    fireEvent(new GameMessageEvent(gameMessage));
  }

  @Override
  public void playerSurrendered() {
    GameMessage gameMessage = createGameMessage();
    gameMessage.setMessageType(GameMessage.MessageType.PLAY_SURRENDER);
    fireEvent(new GameMessageEvent(gameMessage));
    fireEvent(new ClearPlayComponentEvent());
  }

  @Override
  public void proposeCancelMove(Stroke stroke) {
    GameMessage gameMessage = createGameMessage();
    gameMessage.setMessageType(GameMessage.MessageType.PLAY_CANCEL_MOVE);
    if (stroke.isContinueBeat()) {
      stroke.flip();
    }
    stroke.setOnCancelMove();
    Move move = MoveFactory.createMoveFromStroke(stroke);
    gameMessage.setMove(move);

    fireEvent(new GameMessageEvent(gameMessage));
  }

  @Override
  public void saveFriend(final Friend friend) {
    friendsDelegate.withCallback(new AbstractAsyncCallback<Friend>() {
      @Override
      public void onSuccess(Friend result) {
        GameMessage gameMessage = createGameMessage();
        gameMessage.setReceiver(friend.getPk().getFriend());
        gameMessage.setMessageType(GameMessage.MessageType.NOTIFICATION_ADDED_TO_FAVORITE);
        if (friend.isFavorite()) {
          gameMessage.setMessage(messages.youHasBeenAddedToFavorite(gameWebsocket.getPlayer().getPublicName()));
        } else {
          gameMessage.setMessage(messages.youHasBeenRemovedFromFavorite(gameWebsocket.getPlayer().getPublicName()));
        }
        fireEvent(new GameMessageEvent(gameMessage));
      }
    }).saveOrCreate(friend);
  }

  @Override
  public boolean isPlaying() {
    return gameWebsocket.getOpponent() != null;
  }

  private GameMessage createGameMessage() {
    GameMessage gameMessage = GWT.create(GameMessage.class);
    gameMessage.setSender(gameWebsocket.getPlayer());
    gameMessage.setReceiver(gameWebsocket.getOpponent());
    gameMessage.setGame(gameWebsocket.getGame());
    return gameMessage;
  }

  public void bindEvents() {
    addRegisteredHandler(ReceivedPlayerListEvent.TYPE, new ReceivedPlayerListEventHandler() {
      @Override
      public void onReceivedPlayerList(ReceivedPlayerListEvent event) {
        getView().setPlayerList(event.getPlayerList());
        updatePlayerFriendList();
      }
    });

    addRegisteredHandler(ConnectedToPlayEvent.TYPE, new ConnectedToPlayEventHandler() {
      @Override
      public void onConnectedToPlay(ConnectedToPlayEvent event) {
        getView().toggleInPlayButton();
      }
    });

    addRegisteredHandler(DisconnectFromPlayEvent.TYPE, new DisconnectFromPlayEventHandler() {
      @Override
      public void onDisconnectFromPlay(DisconnectFromPlayEvent event) {
        getView().setUpViewOnDisconnectFromServer();
      }
    });

    addRegisteredHandler(StartPlayEvent.TYPE, new StartPlayEventHandler() {
      @Override
      public void onStartPlay(StartPlayEvent event) {
        getView().hideInviteDialog();
        getView().startPlay(event.isWhite());
        gameWebsocket.getPlayer().setPlaying(true);
        playersDelegate.withCallback(new AsyncCallback<Player>() {
          @Override
          public void onFailure(Throwable caught) {
            ErrorDialogBox.setMessage(caught).show();
          }

          @Override
          public void onSuccess(Player result) {
            fireEvent(new UpdatePlayerListEvent());
          }
        }).saveOrCreate(gameWebsocket.getPlayer());
      }
    });

    addRegisteredHandler(RejectPlayEvent.TYPE, new RejectPlayEventHandler() {
      @Override
      public void onRejectPlay(RejectPlayEvent event) {
        getView().hideInviteDialog();
      }
    });

    addRegisteredHandler(TurnChangeEvent.TYPE, new TurnChangeEventHandler() {
      @Override
      public void onTurnChange(TurnChangeEvent event) {
        getView().updateTurn(event.isMyTurn());
      }
    });

    addRegisteredHandler(CheckWinnerEvent.TYPE, new CheckWinnerEventHandler() {
      @Override
      public void onCheckWinner(CheckWinnerEvent event) {
        getView().setBeatenMy(DRAUGHTS_ON_DESK_INIT - getView().getMyDraughtsSize());
        getView().setBeatenOpponent(DRAUGHTS_ON_DESK_INIT - getView().getOpponentDraughtsSize());
        final Game endGame = gameWebsocket.getGame();
        Game.GameEnds gameEnd = null;
        if (0 == getView().getMyDraughtsSize()) {
          InfoDialogBox.setMessage(messages.youLose()).show();
          if ((getView().isWhite())) {
            gameEnd = Game.GameEnds.BLACK_WIN;
          } else {
            gameEnd = Game.GameEnds.WHITE_WIN;
          }
        }
        if (0 == getView().getOpponentDraughtsSize()) {
          InfoDialogBox.setMessage(messages.youWon()).show();
          if (getView().isWhite()) {
            gameEnd = Game.GameEnds.WHITE_WIN;
          } else {
            gameEnd = Game.GameEnds.BLACK_WIN;
          }
        }
        if (gameEnd == null) {
          return;
        }
        fireEvent(new GameOverEvent(endGame, gameEnd, new AsyncCallback<Game>() {
          @Override
          public void onFailure(Throwable caught) {
            ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), caught).show();
          }

          @Override
          public void onSuccess(Game result) {
          }
        }));
      }
    });

    addRegisteredHandler(ClearPlayComponentEvent.TYPE, new ClearPlayComponentEventHandler() {
      @Override
      public void onClearPlayComponent(ClearPlayComponentEvent event) {
        fireEvent(new ClearNotationEvent());
        fireEvent(new UpdateAllPlayerListEvent());
        fireEvent(new RemovePlayMoveOpponentHandlerEvent());

        gameWebsocket.setOpponent(null);
        getView().setOpponent(null);
        gameWebsocket.setGame(null);

        getView().clearPlayComponent();
        getView().hidePlayingButtonsAndShowPlayButton();
      }
    });

    addRegisteredHandler(HideInviteDialogBoxEvent.TYPE, new HideInviteDialogBoxEventHandler() {
      @Override
      public void onHideInviteDialogBox(HideInviteDialogBoxEvent event) {
        getView().hideInviteDialog();
      }
    });

    addRegisteredHandler(GameOverEvent.TYPE, new GameOverEventHandler() {
      @Override
      public void onGameOver(GameOverEvent event) {
        Game game = event.getGame();
        game.setPlayEndStatus(event.getGameEnd());
        game.setPlayFinishDate(new Date());
        final String notation = NotationPanel.getNotation();
        final String notationInDiv = "<div id='" + game.getId() + "'>" + notation + "</div>";
        game.setNotation(notationInDiv);
        game.setEndGameScreenshot(getView().takeScreenshot());
        gamesDelegate.withCallback(event.getAsyncCallback()).saveOrCreate(game);

        GameMessage gameMessage = new GameMessage();
        gameMessage.setSender(gameWebsocket.getPlayer());
        gameMessage.setReceiver(gameWebsocket.getOpponent());
        DTLog.debug("CALLBACK MESSAGE " + gameMessage);
        gameMessage.setMessageType(GameMessage.MessageType.PLAY_CALLBACK);
        fireEvent(new GameMessageEvent(gameMessage));

        fireEvent(new ClearPlayComponentEvent());
        fireEvent(new UpdatePlayShowPanelEvent());
      }
    });

    addRegisteredHandler(PlayCallbackEvent.TYPE, new PlayCallbackEventHandler() {
      @Override
      public void onPlayCallback(PlayCallbackEvent event) {
        fireEvent(new UpdatePlayShowPanelEvent());
      }
    });

    addRegisteredHandler(UpdatePlayerFriendListEvent.TYPE, new UpdatePlayerFriendListEventHandler() {
      @Override
      public void onUpdatePlayerFriendList(UpdatePlayerFriendListEvent event) {
        updatePlayerFriendList();
      }
    });
  }

  private void updatePlayerFriendList() {
    playersDelegate.withCallback(new AsyncCallback<List<Friend>>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
      }

      @Override
      public void onSuccess(List<Friend> result) {
        getView().setPlayerFriendList(result);
      }
    }).getPlayerFriendList(gameWebsocket.getPlayer().getId());
  }

  interface MyView extends View, HasUiHandlers<PlayComponentUiHandlers> {
    void initNotationPanel(EventBus eventBus);

    void setPlayerFriendList(List<Friend> playerList);

    void setPlayerList(List<Player> playerList);

    void setPlayer(Player player);

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

    void hideInviteDialog();

    void showInviteDialog(ClickHandler inviteClickHandler);

    boolean opponentColor();

    String takeScreenshot();

    void setOpponent(Player opponent);
  }
}
