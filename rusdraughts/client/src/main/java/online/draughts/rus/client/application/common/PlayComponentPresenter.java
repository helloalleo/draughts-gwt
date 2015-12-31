package online.draughts.rus.client.application.common;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
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
import online.draughts.rus.client.application.widget.dialog.InfoDialogBox;
import online.draughts.rus.client.application.widget.growl.Growl;
import online.draughts.rus.client.channel.ClientChannel;
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.event.*;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.client.util.AudioUtil;
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

  public final static String INVITE_TIME_DELIMITER = ":";
  private final static int DRAUGHTS_ON_DESK_INIT = 12;
  private final MessengerPresenter.Factory messengerFactory;
  private final PlayView playView;
  private final ResourceDelegate<GameMessagesResource> gameMessagesDelegate;
  private final CurrentSession currentSession;

  private final ClientChannel clientChannel;
  private final PlaySession playSession;
  private final DraughtsMessages messages;
  private final ResourceDelegate<GamesResource> gamesDelegate;
  private final ResourceDelegate<PlayersResource> playersDelegate;
  private final ResourceDelegate<FriendsResource> friendsDelegate;
  private final AppResources resources;
  private MessengerPresenter currentMessenger;
  private int fisherCounter;
  private int timeCounter;
  private Timer timeTimer;
  private Timer fisherTimer;

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
      PlayView playView,
      AppResources resources) {
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
    this.resources = resources;

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

        final boolean white = getView().getOpponentColor();
        final String timeOnPlay = getView().getTimeOnPlay();
        final String fisherTime = getView().getFisherTime();
        gameMessage.setMessage(messages.inviteMessage(playSession.getPlayer().getPublicName(),
            String.valueOf(white ? messages.white() : messages.black())));
        gameMessage.setData(String.valueOf(white) + INVITE_TIME_DELIMITER + timeOnPlay + INVITE_TIME_DELIMITER + fisherTime);

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
        getView().setUnreadMessagesMapForPlayers(result);
        getView().setUnreadMessagesMapForFriends(result);
      }
    }).findUnreadMessages(currentSession.getPlayer().getId());
  }

  @Override
  public boolean isMyTurn() {
    return getView().getBoard().isMyTurn();
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
    resetUnreadMessages(friend);
  }

  private void resetUnreadMessages(PlayerDto friend) {
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

  @Override
  public List<FriendDto> getSortedFriendList(Set<Long> playerIds, List<FriendDto> friendList) {
    List<FriendDto> workingList = new ArrayList<>(friendList);
    List<Long> playerIdList = new ArrayList<>(playerIds);
    Collections.sort(playerIdList, Collections.reverseOrder());
    List<FriendDto> friendDtos = new ArrayList<>(workingList.size());
    for (Long aLong : playerIdList) {
      for (FriendDto friendDto : workingList) {
        if (aLong.equals(friendDto.getFriendOf().getId())) {
          friendDtos.add(friendDto);
          break;
        }
      }
    }
    List<FriendDto> result = new ArrayList<>(workingList.size());
    workingList.removeAll(friendDtos);
    result.addAll(friendDtos);
    result.addAll(workingList);
    return result;
  }

  @Override
  public void didNotResponse(PlayerDto opponent) {
    GameMessageDto gameMessageDto = new GameMessageDto();
    gameMessageDto.setSender(playSession.getPlayer());
    gameMessageDto.setReceiver(opponent);
    gameMessageDto.setMessageType(GameMessageDto.MessageType.PLAY_DID_NOT_RESPONSE);
    fireEvent(new GameMessageEvent(gameMessageDto));
  }

  @Override
  public void stopTimers() {
    timeTimer.cancel();
    fisherTimer.cancel();
  }

  private GameMessageDto createGameMessage() {
    GameMessageDto gameMessage = new GameMessageDto();
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

        initTimers(event.getTimeOnPlay(), event.getFisherTime());

        GameDto game = playSession.getGame();

        // isMyTurn вызывает Game и Player из текущей сессии
        getView().updateTurn(isMyTurn());
        getView().initNotationPanel(game.getId());

        if (event.isInviter()) {
          final Set<DraughtDto> initialPosition = getView().getBoard().getInitialPosition();
          game.setPlayStartDate(new Date());
          game.setInitialPos(initialPosition);
          gamesDelegate.withoutCallback().save(game);
          game.setInitialPos(null);
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
        fireEvent(new GameOverEvent(endGame, gameEnd, new AbstractAsyncCallback<GameDto>() {
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

        GameMessageDto gameMessageDto = createGameMessage();
        gameMessageDto.setMessageType(GameMessageDto.MessageType.PLAY_END);
        fireEvent(new GameMessageEvent(gameMessageDto));

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
          AudioUtil.playSound(resources.sounds().newMessageSound());

          updateUnreadMessages();
        }
      }
    });

    addRegisteredHandler(ResetUnreadMessagesEvent.TYPE, new ResetUnreadMessagesEventHandler() {
      @Override
      public void onResetUnreadMessages(ResetUnreadMessagesEvent event) {
        resetUnreadMessages(event.getFriend());
      }
    });
  }

  private String formatTime(int counter) {
    int min = counter / 60;
    int sec = counter - min * 60;
    return String.valueOf(min) + ":" + (sec < 10 ? "0" : "") + sec;
  }

  private void initTimers(int timeOnPlay, final int fisherTime) {
    fisherCounter = fisherTime;
    timeCounter = timeOnPlay * 60;
    timeTimer = new Timer() {
      @Override
      public void run() {
        getView().setTime(formatTime(timeCounter));
        if (isMyTurn() && 0 == fisherCounter) {
          timeCounter--;
        }
        if (!isMyTurn() && fisherTime != fisherCounter) {
          timeCounter = timeCounter + fisherCounter;
          fisherCounter = fisherTime;
          getView().setTime(formatTime(timeCounter));
          getView().setFisherTime(formatTime(fisherCounter));
        }
        if (0 == timeCounter) {
          timeTimer.cancel();
          GameDto endGame = playSession.getGame();
          GameDto.GameEnds gameEnd = isMyTurn()
              ? (getView().isWhite() ? GameDto.GameEnds.BLACK_WIN : GameDto.GameEnds.WHITE_WIN)
                  : (getView().isWhite() ? GameDto.GameEnds.WHITE_WIN : GameDto.GameEnds.BLACK_WIN);
          endGame.setPlayEndStatus(gameEnd);
          GameMessageDto gameMessageDto = createGameMessage();
          gameMessageDto.setGame(endGame);
          gameMessageDto.setMessageType(GameMessageDto.MessageType.PLAY_TIMEOUT);
          fireEvent(new GameMessageEvent(gameMessageDto));
          fireEvent(new GameOverEvent(endGame, gameEnd, new AbstractAsyncCallback<GameDto>() {
            @Override
            public void onSuccess(GameDto result) {
              Growl.growlNotif(messages.timeOut());
            }
          }));
        }
      }
    };

    fisherTimer = new Timer() {
      @Override
      public void run() {
        if (isMyTurn() && fisherCounter > 0) {
          fisherCounter--;
          getView().setFisherTime(formatTime(fisherCounter));
        }
        if (fisherCounter == 0) {
          timeCounter = timeCounter - fisherTime;
          fisherCounter = fisherTime;
        }
      }
    };

    timeTimer.scheduleRepeating(1000);
    fisherTimer.scheduleRepeating(1000);
  }

  private void updatePlayerFriendList() {
    friendsDelegate.withCallback(new AbstractAsyncCallback<List<FriendDto>>() {
      @Override
      public void onSuccess(List<FriendDto> result) {
        getView().setFriendList(result);
        updateUnreadMessages();
      }
    }).getPlayerFriendList(playSession.getPlayer().getId());
  }

  public interface MyView extends View, HasUiHandlers<PlayComponentUiHandlers> {
    void setUnreadMessagesMapForFriends(Map<Long, Integer> result);

    void setFriendList(List<FriendDto> playerList);

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

    void setTurnMessage(String message);

    int getMyDraughtsSize();

    int getOpponentDraughtsSize();

    boolean isWhite();

    void hideInviteDialog();

    void showInviteDialog(ClickHandler inviteClickHandler);

    boolean getOpponentColor();

    String takeScreenshot();

    void setOpponent(PlayerDto opponent);

    Board getBoard();

    void initNotationPanel(Long gameId);

    void setUnreadMessagesMapForPlayers(Map<Long, Integer> result);

    String getTimeOnPlay();

    String getFisherTime();

    void setTime(String time);

    void setFisherTime(String fisherTime);
  }
}
