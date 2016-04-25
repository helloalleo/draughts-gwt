package online.draughts.rus.client.application.common;

import com.google.web.bindery.event.shared.EventBus;
import online.draughts.rus.client.application.widget.dialog.GameResultDialogBox;
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.event.GameCheckStuckEvent;
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
    if (0 == myDraughtsSize) {
      GameResultDialogBox.setMessage(messages.youLose(), eventBus).show();
      if (white) {
        gameEnd = GameDto.GameEnds.BLACK_WIN;
      } else {
        gameEnd = GameDto.GameEnds.WHITE_WIN;
      }
    }
    if (0 == opponentDraughtsSize) {
      GameResultDialogBox.setMessage(messages.youWon(), eventBus).show();
      if (white) {
        gameEnd = GameDto.GameEnds.WHITE_WIN;
      } else {
        gameEnd = GameDto.GameEnds.BLACK_WIN;
      }
    }

    final GameDto endGame = playSession.getGame();
    if (gameEnd == null) {
      eventBus.fireEvent(new GameCheckStuckEvent());
      return;
    }
    eventBus.fireEvent(new GameOverEvent(endGame, gameEnd, new AbstractAsyncCallback<GameDto>() {
      @Override
      public void onSuccess(GameDto result) {
      }
    }));
  }

  public static void checkStuck(EventBus eventBus, PlaySession playSession, boolean isWhite) {
    GameDto.GameEnds gameEnd;
    if (isWhite) {
      gameEnd = GameDto.GameEnds.BLACK_WIN;
    } else {
      gameEnd = GameDto.GameEnds.WHITE_WIN;
    }
    final GameDto endGame = playSession.getGame();
    eventBus.fireEvent(new GameOverEvent(endGame, gameEnd, new AbstractAsyncCallback<GameDto>() {
      @Override
      public void onSuccess(GameDto result) {
      }
    }));
  }
}
