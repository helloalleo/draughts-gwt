package online.draughts.rus.client.util;

import com.google.gwt.core.client.GWT;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.locale.DraughtsMessages;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.10.15
 * Time: 13:50
 */
public class TrUtils {

  private static DraughtsMessages messages = GWT.create(DraughtsMessages.class);

  public static String translateGameType(GameDto.GameType gameType) {
    // legacy
    if (null == gameType) {
      return messages.draughts();
    }
    switch (gameType) {
      case DRAUGHTS:
        return messages.draughts();
      case GIVEAWAY:
        return messages.giveaway();
      default:
        return messages.draughts();
    }
  }

  public static String translateEndGame(GameDto.GameEnd gameEnd) {
    switch (gameEnd) {
      case WHITE_WIN:
        return messages.WHITE_WIN();
      case BLACK_WIN:
        return messages.BLACK_WIN();
      case WHITE_LEFT:
        return messages.WHITE_LEFT();
      case BLACK_LEFT:
        return messages.BLACK_LEFT();
      case DRAW:
        return messages.DRAW();
      case SURRENDER_WHITE:
        return messages.SURRENDER_WHITE();
      case SURRENDER_BLACK:
        return messages.SURRENDER_BLACK();
      default:
        return "";
    }
  }
}
