package online.draughts.rus.client.util;

import com.google.gwt.user.client.Cookies;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.shared.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.09.15
 * Time: 17:31
 */
public class DTCookiesImpl implements online.draughts.rus.client.util.Cookies {

  private static final String TIME_ON_PLAY = "timeOnPlay";
  private static final String FISHER_TIME = "fisherTime";
  private static final String TIME_ON_PLAY_CUSTOM = "timeOnPlayCustom";
  private final String GAMES_ON_PAGE_COUNTER = "gamesOnPage";
  private final String LOCATION = "LOCATION"; // куки адреса страницы
  private final String MY_GAMES = "myGames";

  public void setLocation(String nameToken) {
    Cookies.setCookie(LOCATION, nameToken);
  }

  public String getLocation() {
    String location = Cookies.getCookie(LOCATION);
    if (StringUtils.isEmpty(location)) {
      location = NameTokens.homePage;
    }
    return location;
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

  @Override
  public void setTimeOnPlay(String value) {
    Cookies.setCookie(TIME_ON_PLAY, String.valueOf(value));
  }

  @Override
  public int getTimeOnPlay() {
    String gamesOnPage = Cookies.getCookie(TIME_ON_PLAY);
    if (StringUtils.isEmpty(gamesOnPage)) {
      gamesOnPage = "0";
    }
    return Integer.valueOf(gamesOnPage);
  }

  @Override
  public void setTimeOnPlayCustom(String value) {
    Cookies.setCookie(TIME_ON_PLAY_CUSTOM, String.valueOf(value));
  }

  @Override
  public boolean getTimeOnPlayCustom() {
    String gamesOnPage = Cookies.getCookie(TIME_ON_PLAY_CUSTOM);
    if (StringUtils.isEmpty(gamesOnPage)) {
      gamesOnPage = Boolean.FALSE.toString();
    }
    return Boolean.valueOf(gamesOnPage);
  }

  @Override
  public void setFisherTime(String value) {
    Cookies.setCookie(FISHER_TIME, String.valueOf(value));
  }

  @Override
  public int getFisherTime() {
    String gamesOnPage = Cookies.getCookie(FISHER_TIME);
    if (StringUtils.isEmpty(gamesOnPage)) {
      gamesOnPage = "0";
    }
    return Integer.valueOf(gamesOnPage);
  }
}
