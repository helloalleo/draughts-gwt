package online.draughts.rus.client.application.common;

import com.google.web.bindery.event.shared.EventBus;
import online.draughts.rus.client.application.widget.dialog.GameResultDialogBox;
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.event.GameOverEvent;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.locale.DraughtsMessages;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.04.16
 * Time: 18:54
 */
public class PlayComponentUtil {
  public static void checkWin(EventBus eventBus, PlaySession playSession, DraughtsMessages messages,
                              int myDraughtsSize, int opponentDraughtsSize, boolean white) {
    GameDto.GameEnds gameEnd = null;
    final GameDto.GameType gameType = playSession.getGameType();
    if (0 == myDraughtsSize) {
      GameResultDialogBox.setMessage(endGameMessage(messages, gameType, false), eventBus).show();
      if (white) {
        gameEnd = blackWin(gameType);
      } else {
        gameEnd = whiteWin(gameType);
      }
    }
    if (0 == opponentDraughtsSize) {
      GameResultDialogBox.setMessage(endGameMessage(messages, gameType, true), eventBus).show();
      if (white) {
        gameEnd = whiteWin(gameType);
      } else {
        gameEnd = blackWin(gameType);
      }
    }

    final GameDto endGame = playSession.getGame();
    if (gameEnd == null) {
      return;
    }
    eventBus.fireEvent(new GameOverEvent(endGame, gameEnd, new AbstractAsyncCallback<GameDto>() {
      @Override
      public void onSuccess(GameDto result) {
      }
    }));
  }

  private static String endGameMessage(DraughtsMessages messages, GameDto.GameType gameType, boolean win) {
    switch (gameType) {
      case DRAUGHTS:
        return win ? messages.youWon() : messages.youLose();
      case GIVEAWAY:
        return !win ? messages.youLose() : messages.youWon();
      default:
        return "";
    }
  }

  private static GameDto.GameEnds whiteWin(GameDto.GameType gameType) {
    switch (gameType) {
      case DRAUGHTS:
        return GameDto.GameEnds.WHITE_WIN;
      case GIVEAWAY:
        return GameDto.GameEnds.BLACK_WIN;
      default:
        return GameDto.GameEnds.WHITE_WIN;
    }
  }

  private static GameDto.GameEnds blackWin(GameDto.GameType gameType) {
    switch (gameType) {
      case DRAUGHTS:
        return GameDto.GameEnds.BLACK_WIN;
      case GIVEAWAY:
        return GameDto.GameEnds.WHITE_WIN;
      default:
        return GameDto.GameEnds.BLACK_WIN;
    }
  }

  public static void checkShut(EventBus eventBus, PlaySession playSession,
                               DraughtsMessages messages, boolean isWhite) {
    final GameDto.GameEnds gameEnd;
    if (isWhite == playSession.isPlayerHasWhiteColor()) {
      GameResultDialogBox.setMessage(messages.youLose(), eventBus).show();
    } else {
      GameResultDialogBox.setMessage(messages.youWon(), eventBus).show();
    }
    if (isWhite) {
      gameEnd = blackWin(playSession.getGameType());
    } else {
      gameEnd = whiteWin(playSession.getGameType());
    }
    final GameDto endedGame = playSession.getGame();
    eventBus.fireEvent(new GameOverEvent(endedGame, gameEnd, new AbstractAsyncCallback<GameDto>() {
      @Override
      public void onSuccess(GameDto result) {
      }
    }));
  }
}
