package online.draughts.rus.client.application.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import online.draughts.rus.client.application.play.PlayView;
import online.draughts.rus.client.application.play.messanger.MessengerPresenter;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.application.widget.NotationPanel;
import online.draughts.rus.client.application.widget.dialog.ErrorDialogBox;
import online.draughts.rus.client.application.widget.dialog.InfoDialogBox;
import online.draughts.rus.client.application.widget.growl.Growl;
import online.draughts.rus.client.channel.ClientChannel;
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.event.*;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.draughts.Board;
import online.draughts.rus.draughts.MoveFactory;
import online.draughts.rus.draughts.Stroke;
import online.draughts.rus.draughts.StrokeFactory;
import online.draughts.rus.shared.dto.*;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.resource.FriendsResource;
import online.draughts.rus.shared.resource.GameMessagesResource;
import online.draughts.rus.shared.resource.GamesResource;
import online.draughts.rus.shared.resource.PlayersResource;

import java.util.*;


public class PlayComponentPresenter extends PresenterWidget<PlayComponentPresenter.MyView>
    implements PlayComponentUiHandlers {

  private final MessengerPresenter.Factory messengerFactory;
  private final PlayView playView;
  private final ResourceDelegate<GameMessagesResource> gameMessagesDelegate;
  private final CurrentSession currentSession;
  private int DRAUGHTS_ON_DESK_INIT = 12;

  private final ClientChannel clientChannel;
  private final PlaySession playSession;
  private final DraughtsMessages messages;
  private final ResourceDelegate<GamesResource> gamesDelegate;
  private final ResourceDelegate<PlayersResource> playersDelegate;
  private final ResourceDelegate<FriendsResource> friendsDelegate;
  private MessengerPresenter currentMessenger;

  @Inject
  PlayComponentPresenter(
      EventBus eventBus,
      MyView view,
      PlaySession playSession,
      CurrentSession currentSession,
      DraughtsMessages messages,
      ResourceDelegate<GamesResource> gamesDelegate,
      ResourceDelegate<PlayersResource> playersDelegate,
      ResourceDelegate<FriendsResource> friendsDelegate,
      ResourceDelegate<GameMessagesResource> gameMessagesDelegate,
      MessengerPresenter.Factory messengerFactory,
      ClientChannel clientChannel,
      PlayView playView) {
    super(eventBus, view);

    this.messages = messages;
    this.clientChannel = clientChannel;
    this.playSession = playSession;
    this.currentSession = currentSession;
    this.gamesDelegate = gamesDelegate;
    this.playersDelegate = playersDelegate;
    this.friendsDelegate = friendsDelegate;
    this.messengerFactory = messengerFactory;
    this.gameMessagesDelegate = gameMessagesDelegate;
    this.playView = playView;

    getView().setUiHandlers(this);
  }

  @Override
  protected void onBind() {
    super.onBind();

    bindEvents();
  }

  @Override
  public void startPlayWith(final PlayerDto opponent) {
    if (opponent == null) {
      Growl.growlNotif(messages.selectPlayer());
      return;
    }
    if (opponent.getId() == playSession.getPlayer().getId()) {
      Growl.growlNotif(messages.selectAnotherPlayerItsYou());
      return;
    }
    if (!opponent.isOnline()) {
      Growl.growlNotif(messages.playerOffline());
      return;
    }
    if (opponent.isPlaying()) {
      Growl.growlNotif(messages.playAlreadyPlaying(opponent.getPublicName()));
      return;
    }
    getView().setOpponent(opponent);

    getView().showInviteDialog(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        GameMessageDto gameMessage = createGameMessage();
        gameMessage.setMessageType(GameMessageDto.MessageType.PLAY_INVITE);
        gameMessage.setReceiver(opponent);

        final boolean white = getView().opponentColor();
        gameMessage.setMessage(messages.inviteMessage(playSession.getPlayer().getPublicName(),
            String.valueOf(white ? messages.white() : messages.black())));
        gameMessage.setData(String.valueOf(white));

        fireEvent(new GameMessageEvent(gameMessage));
      }
    });
  }

  @Override
  public void refreshConnectionToServer() {
    if (playSession.isConnected()) {
      Growl.growlNotif(messages.alreadyConnected());
      return;
    }
    clientChannel.connect();
  }

  private void updateUnreadMessages() {
    gameMessagesDelegate.withCallback(new AbstractAsyncCallback<Map<Long, Integer>>() {
      @Override
      public void onSuccess(Map<Long, Integer> result) {
        getView().setUnreadMessagesMap(result);
      }
    }).findUnreadMessages(currentSession.getPlayer().getId());
  }

  @Override
  public boolean isMyTurn() {
    return playSession.getGame().getPlayerWhite().getId() == playSession.getPlayer().getId();
  }

  @Override
  public void proposeDraw() {
    GameMessageDto gameMessage = createGameMessage();
    gameMessage.setMessageType(GameMessageDto.MessageType.PLAY_PROPOSE_DRAW);
    fireEvent(new GameMessageEvent(gameMessage));
  }

  @Override
  public void playerSurrendered() {
    GameMessageDto gameMessage = createGameMessage();
    gameMessage.setMessageType(GameMessageDto.MessageType.PLAY_SURRENDER);
    fireEvent(new GameMessageEvent(gameMessage));
    fireEvent(new ClearPlayComponentEvent());
  }

  @Override
  public void proposeCancelMove(Stroke stroke) {
    GameMessageDto gameMessage = createGameMessage();
    gameMessage.setMessageType(GameMessageDto.MessageType.PLAY_CANCEL_MOVE);
    if (stroke.isContinueBeat()) {
      stroke.flip();
    }
    stroke.setOnCancelMove();
    MoveDto move = MoveFactory.createMoveFromStroke(stroke);
    gameMessage.setMove(move);

    fireEvent(new GameMessageEvent(gameMessage));
  }

  @Override
  public void saveFriend(final FriendDto friend) {
    friendsDelegate.withCallback(new AbstractAsyncCallback<FriendDto>() {
      @Override
      public void onSuccess(FriendDto result) {
        GameMessageDto gameMessage = createGameMessage();
        gameMessage.setReceiver(friend.getFriendOf());
        gameMessage.setMessageType(GameMessageDto.MessageType.NOTIFICATION_ADDED_TO_FAVORITE);
        if (friend.isFavorite()) {
          gameMessage.setMessage(messages.youHasBeenAddedToFavorite(playSession.getPlayer().getPublicName()));
          fireEvent(new GameMessageEvent(gameMessage));
        }
      }
    }).save(friend);
  }

  @Override
  public void checkWinner() {
    fireEvent(new CheckWinnerEvent());
  }

  @Override
  public void addNotationStroke(Stroke strokeForNotation) {
    fireEvent(new NotationStrokeEvent(strokeForNotation));
  }

  @Override
  public void toggleTurn(boolean turn) {
    fireEvent(new TurnChangeEvent(turn));
  }

  @Override
  public void doPlayerMove(MoveDto move) {
    fireEvent(new PlayMovePlayerMessageEvent(move));
  }

  @Override
  public void writeToFriend(PlayerDto friend) {
    currentMessenger = messengerFactory.create(playView, friend);
    addToPopupSlot(currentMessenger);
    playersDelegate.withCallback(new AbstractAsyncCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        updateUnreadMessages();
      }
    }).resetUnreadMessages(currentSession.getPlayer().getId(), friend.getId());
  }

  @Override
  public List<PlayerDto> getSortedPlayerList(Set<Long> playerIds, List<PlayerDto> playerList) {
    List<PlayerDto> workingList = new ArrayList<>(playerList);
    List<Long> playerIdList = new ArrayList<>(playerIds);
    Collections.sort(playerIdList, Collections.reverseOrder());
    List<PlayerDto> playerDtos = new ArrayList<>(workingList.size());
    for (Long aLong : playerIdList) {
      for (PlayerDto playerDto : workingList) {
        if (aLong.equals(playerDto.getId())) {
          playerDtos.add(playerDto);
          break;
        }
      }
    }
    List<PlayerDto> result = new ArrayList<>(workingList.size());
    workingList.removeAll(playerDtos);
    result.addAll(playerDtos);
    result.addAll(workingList);
    return result;
  }

  private GameMessageDto createGameMessage() {
    GameMessageDto gameMessage = GWT.create(GameMessageDto.class);
    gameMessage.setSender(playSession.getPlayer());
    gameMessage.setReceiver(playSession.getOpponent());
    gameMessage.setGame(playSession.getGame());
    return gameMessage;
  }

  public void bindEvents() {
    addRegisteredHandler(ReceivedPlayerListEvent.TYPE, new ReceivedPlayerListEventHandler() {
      @Override
      public void onReceivedPlayerList(ReceivedPlayerListEvent event) {
        getView().setPlayerList(event.getPlayerList());
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
          @Override
          public void execute() {
            updatePlayerFriendList();
          }
        });
      }
    });

    addRegisteredHandler(ConnectedToPlayEvent.TYPE, new ConnectedToPlayEventHandler() {
      @Override
      public void onConnectedToPlay(ConnectedToPlayEvent event) {
        getView().toggleInPlayButton();
        updateUnreadMessages();
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
      public void onStartPlay(final StartPlayEvent event) {
        getView().hideInviteDialog();
        getView().startPlay(event.isWhite());

        GameDto game = playSession.getGame();

        // isMyTurn вызывает Game и Player из текущей сессии
        getView().updateTurn(isMyTurn());
        getView().initNotationPanel(game.getId());

        if (event.isInviter()) {
          final Set<DraughtDto> initialPosition = getView().getBoard().getInitialPosition();
          game.setPlayStartDate(new Date());
          game.setInitialPos(initialPosition);
          gamesDelegate.withoutCallback().save(game);
        }

        playSession.getPlayer().setPlaying(true);
        playersDelegate.withCallback(new AbstractAsyncCallback<PlayerDto>() {
          @Override
          public void onSuccess(PlayerDto result) {
            fireEvent(new UpdatePlayerListEvent());
          }
        }).save(playSession.getPlayer());
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
        final GameDto endGame = playSession.getGame();
        GameDto.GameEnds gameEnd = null;
        if (0 == getView().getMyDraughtsSize()) {
          InfoDialogBox.setMessage(messages.youLose()).show();
          if ((getView().isWhite())) {
            gameEnd = GameDto.GameEnds.BLACK_WIN;
          } else {
            gameEnd = GameDto.GameEnds.WHITE_WIN;
          }
        }
        if (0 == getView().getOpponentDraughtsSize()) {
          InfoDialogBox.setMessage(messages.youWon()).show();
          if (getView().isWhite()) {
            gameEnd = GameDto.GameEnds.WHITE_WIN;
          } else {
            gameEnd = GameDto.GameEnds.BLACK_WIN;
          }
        }
        if (gameEnd == null) {
          return;
        }
        fireEvent(new GameOverEvent(endGame, gameEnd, new AsyncCallback<GameDto>() {
          @Override
          public void onFailure(Throwable caught) {
            ErrorDialogBox.setMessage(messages.errorWhileSavingGame(), caught).show();
          }

          @Override
          public void onSuccess(GameDto result) {
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

        playSession.setOpponent(null);
        getView().setOpponent(null);
        playSession.setGame(null);

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
        GameDto game = event.getGame();
        game.setPlayEndStatus(event.getGameEnd());
        game.setPlayFinishDate(new Date());
        final String notation = NotationPanel.getNotation();
        game.setNotation(notation);
        game.setEndGameScreenshot(getView().takeScreenshot());
        gamesDelegate.withCallback(event.getAsyncCallback()).save(game);

        fireEvent(new ClearPlayComponentEvent());
      }
    });

    addRegisteredHandler(PlayMoveOpponentEvent.TYPE, new PlayMoveOpponentEventHandler() {
      @Override
      public void onPlayMoveOpponent(PlayMoveOpponentEvent event) {
        final MoveDto move = event.getMove();
        final Stroke stroke = StrokeFactory.createStrokeFromMove(move);
        final Stroke mirror = stroke.flip();
        getView().getBoard().moveOpponent(mirror);
      }
    });

    addRegisteredHandler(PlayMoveCancelEvent.TYPE, new PlayMoveCancelEventHandler() {
      @Override
      public void onPlayMove(PlayMoveCancelEvent event) {
        final MoveDto move = event.getMove();
        final Stroke stroke = StrokeFactory.createStrokeFromMove(move);
        final Stroke mirror = stroke.flip();
        fireEvent(new NotationCancelStrokeEvent(stroke));
        getView().getBoard().moveMyCanceled(mirror);
      }
    });

    addRegisteredHandler(PlayMoveOpponentCancelEvent.TYPE, new PlayMoveOpponentCancelEventHandler() {
      @Override
      public void onPlayMoveOpponentCancel(PlayMoveOpponentCancelEvent event) {
        final MoveDto move = event.getMove();
        final Stroke stroke = StrokeFactory.createStrokeFromMove(move);
        final Stroke mirror = stroke.flip();
        fireEvent(new NotationCancelStrokeEvent(mirror));
        getView().getBoard().moveOpponentCanceled(stroke);
      }
    });

    addRegisteredHandler(ChatMessageEvent.TYPE, new ChatMessageEventHandler() {
      @Override
      public void onChatMessage(ChatMessageEvent event) {
        if (currentMessenger == null || !currentMessenger.isVisible()) {
          Growl.growlNotif(messages.newMessageFrom(event.getMessage().getSender().getPublicName()));
          updateUnreadMessages();
        }
      }
    });
  }

  private void updatePlayerFriendList() {
    friendsDelegate.withCallback(new AbstractAsyncCallback<List<FriendDto>>() {
      @Override
      public void onSuccess(List<FriendDto> result) {
        getView().setPlayerFriendList(result);
      }
    }).getPlayerFriendList(playSession.getPlayer().getId());
  }

  public interface MyView extends View, HasUiHandlers<PlayComponentUiHandlers> {
    void setPlayerFriendList(List<FriendDto> playerList);

    void setPlayerList(List<PlayerDto> playerList);

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

    void setOpponent(PlayerDto opponent);

    Board getBoard();

    void initNotationPanel(Long gameId);

    void setUnreadMessagesMap(Map<Long, Integer> result);
  }
}
