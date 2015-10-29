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
public class DTCookies {

  private static ClientConfiguration configuration = GWT.create(ClientConfiguration.class);

  private static final String GAMES_ON_PAGE_COUNTER = "gamesOnPage";
  private static final String LOCATION = "LOCATION"; // куки адреса страницы
  private static final String NEW_GAME_BUTTON_STATE_MAIN_PAGE = "newGameButtonState";
  private static final String MY_GAMES = "myGames";

  public static void setLocation(String nameToken) {
    Cookies.setCookie(LOCATION, nameToken);
    DTLog.debug("set LOCATION: " + nameToken);
  }

  public static String getLocation() {
    DTLog.debug("get location: " + Cookies.getCookie(LOCATION));
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

  public static void setMyGames(boolean myGames) {
    Cookies.setCookie(MY_GAMES, String.valueOf(myGames));
  }

  public static boolean isMyGames() {
    String myGames = Cookies.getCookie(MY_GAMES);
    if (StringUtils.isEmpty(myGames)) {
      myGames = Boolean.FALSE.toString();
    }
    return Boolean.valueOf(myGames);
  }
}
