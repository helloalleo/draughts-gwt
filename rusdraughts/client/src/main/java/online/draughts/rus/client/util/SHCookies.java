package online.draughts.rus.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.09.15
 * Time: 17:31
 */
public class SHCookies {

  private static ClientConfiguration configuration = GWT.create(ClientConfiguration.class);

  private static final String GAMES_ON_PAGE_COUNTER = "gamesOnPage";
  private static final String LOCATION = "LOCATION"; // куки адреса страницы
  private static final String NEW_GAME_BUTTON_STATE_MAIN_PAGE = "newGameButtonState";

  public static void setLOCATION(String nameToken) {
    Cookies.setCookie(LOCATION, nameToken);
    SHLog.debug("set LOCATION: " + nameToken);
  }

  public static String getLOCATION() {
    SHLog.debug("get location: " + Cookies.getCookie(LOCATION));
    return Cookies.getCookie(LOCATION);
  }

  public static void setNewGameButtonState(boolean newGame) {
    Cookies.setCookie(NEW_GAME_BUTTON_STATE_MAIN_PAGE, String.valueOf(newGame));
  }

  public static boolean getNewGameButtonState() {
    return Boolean.valueOf(Cookies.getCookie(NEW_GAME_BUTTON_STATE_MAIN_PAGE));
  }

  public static void setGamesOnPageCounter(int value) {
    Cookies.setCookie(GAMES_ON_PAGE_COUNTER, String.valueOf(value));
  }

  public static int getGamesOnPageCounter() {
    String gamesOnPage = Cookies.getCookie(GAMES_ON_PAGE_COUNTER);
    if (StringUtils.isEmpty(gamesOnPage)) {
      gamesOnPage = "0";
    }
    return Integer.valueOf(gamesOnPage);
  }
}
