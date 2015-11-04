package online.draughts.rus.client.util;

import com.google.gwt.user.client.Cookies;
import online.draughts.rus.shared.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.09.15
 * Time: 17:31
 */
public class DTCookiesImpl implements online.draughts.rus.client.util.Cookies {

  private final String GAMES_ON_PAGE_COUNTER = "gamesOnPage";
  private final String LOCATION = "LOCATION"; // куки адреса страницы
  private final String NEW_GAME_BUTTON_STATE_MAIN_PAGE = "newGameButtonState";
  private final String MY_GAMES = "myGames";

  public void setLocation(String nameToken) {
    Cookies.setCookie(LOCATION, nameToken);
  }

  public String getLocation() {
    return Cookies.getCookie(LOCATION);
  }

  public void setNewGameButtonState(boolean newGame) {
    Cookies.setCookie(NEW_GAME_BUTTON_STATE_MAIN_PAGE, String.valueOf(newGame));
  }

  public boolean getNewGameButtonState() {
    return Boolean.valueOf(Cookies.getCookie(NEW_GAME_BUTTON_STATE_MAIN_PAGE));
  }

  public void setGamesOnPageCounter(int value) {
    Cookies.setCookie(GAMES_ON_PAGE_COUNTER, String.valueOf(value));
  }

  public int getGamesOnPageCounter() {
    String gamesOnPage = Cookies.getCookie(GAMES_ON_PAGE_COUNTER);
    if (StringUtils.isEmpty(gamesOnPage)) {
      gamesOnPage = "0";
    }
    return Integer.valueOf(gamesOnPage);
  }

  public void setMyGames(boolean myGames) {
    Cookies.setCookie(MY_GAMES, String.valueOf(myGames));
  }

  public boolean isMyGames() {
    String myGames = Cookies.getCookie(MY_GAMES);
    if (StringUtils.isEmpty(myGames)) {
      myGames = Boolean.FALSE.toString();
    }
    return Boolean.valueOf(myGames);
  }
}
