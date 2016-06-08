package online.draughts.rus.client.application.common;

import com.google.web.bindery.event.shared.EventBus;
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.event.GameOverEvent;
import online.draughts.rus.client.gin.DialogFactory;
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
  static void checkWin(EventBus eventBus, PlaySession playSession, DraughtsMessages messages,
                       DialogFactory dialogFactory, int myDraughtsSize, int opponentDraughtsSize, boolean white) {
    GameDto.GameEnds gameEnd = null;
    final GameDto.GameType gameType = playSession.getGameType();
    if (0 == myDraughtsSize) {
      dialogFactory.createGameResultDialogBox(endGameMessage(messages, gameType, false)).show();
      if (white) {
        gameEnd = blackWin(gameType);
      } else {
        gameEnd = whiteWin(gameType);
      }
    }
    if (0 == opponentDraughtsSize) {
      dialogFactory.createGameResultDialogBox(endGameMessage(messages, gameType, true)).show();
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
    eventBus.fireEvent(new GameOverEvent(endGame, gameEnd, new AbstractAsyncCallback<GameDto>(dialogFactory) {
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
        return !win ? messages.youWon() : messages.youLose();
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

  static void checkShut(EventBus eventBus, PlaySession playSession,
                        DraughtsMessages messages, DialogFactory dialogFactory, boolean isWhite) {
    final GameDto.GameEnds gameEnd;
    final GameDto.GameType gameType = playSession.getGameType();
    if (isWhite == playSession.isPlayerHasWhiteColor()) {
      dialogFactory.createGameResultDialogBox(endGameMessage(messages, gameType, false)).show();
    } else {
      dialogFactory.createGameResultDialogBox(endGameMessage(messages, gameType, true)).show();
    }
    if (isWhite) {
      gameEnd = blackWin(playSession.getGameType());
    } else {
      gameEnd = whiteWin(playSession.getGameType());
    }
    final GameDto endedGame = playSession.getGame();
    eventBus.fireEvent(new GameOverEvent(endedGame, gameEnd, new AbstractAsyncCallback<GameDto>(dialogFactory) {
      @Override
      public void onSuccess(GameDto result) {
      }
    }));
  }
}
