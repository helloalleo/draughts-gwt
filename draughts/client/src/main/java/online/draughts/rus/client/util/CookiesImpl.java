package online.draughts.rus.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.09.15
 * Time: 17:31
 */
public class CookiesImpl implements online.draughts.rus.client.util.Cookies {

  private final DraughtsMessages messages = GWT.create(DraughtsMessages.class);

  private static final String TIME_ON_PLAY = "TIME_ON_PLAY";
  private static final String FISHER_TIME = "FISHER_TIME";
  private static final String TIME_ON_PLAY_CUSTOM = "TIME_ON_PLAY_CUSTOM";
  private static final String LAST_USED_SMILES = "LAST_USED_SMILES";
  private static final String LIST_DELIMITER = ",";
  public static final String LOCALE = "LOCALE";
  public static final String GAMES_IN_ROW_TOTAL = "GAME_IN_ROW_HOME_PANEL";
  public static final String GAMES_IN_ROW_MY_GAMES = "GAMES_IN_ROW_MY_GAMES_PANEL";
  private static final String DEFAULT_LOCALE = "ru";
  private static final String SHOW_AVATARS = "SHOW_AVATARS";
  private static final String PUBLISH_GAME = "PUBLISH_GAME";
  private static final String GAME_TYPE = "GAME_TYPE";
  private static final String GAMES_IN_ROW_NUMBER = "GAMES_IN_ROW";
  private static final String MY_GAMES_IN_ROW_NUMBER = "MY_GAMES_IN_ROW";
  private static final String LOCATION = "LOCATION"; // куки адреса страницы
  private static final String MY_GAMES = "MY_GAMES";

  public void setLocation(String nameToken) {
    setBasicCookie(LOCATION, nameToken);
  }

  private void setBasicCookie(String cookieName, String cookieValue) {
    Date expires = new Date();
    CalendarUtil.addMonthsToDate(expires, 6);
    Cookies.setCookie(cookieName, cookieValue, expires);
  }

  public String getLocation() {
    String location = Cookies.getCookie(LOCATION);
    if (StringUtils.isEmpty(location)) {
      location = NameTokens.HOME_PAGE;
    }
    return location;
  }

  public void setGamesInRowNumber(int value) {
    setBasicCookie(GAMES_IN_ROW_NUMBER, String.valueOf(value));
  }

  public int getGamesInRowNumber() {
    String gamesOnPage = Cookies.getCookie(GAMES_IN_ROW_NUMBER);
    if (StringUtils.isEmpty(gamesOnPage)) {
      gamesOnPage = "4";
    }
    return Integer.valueOf(gamesOnPage);
  }

  public void setMyGamesInRowNumber(int value) {
    setBasicCookie(MY_GAMES_IN_ROW_NUMBER, String.valueOf(value));
  }

  public int getMyGamesInRowNumber() {
    String gamesOnPage = Cookies.getCookie(MY_GAMES_IN_ROW_NUMBER);
    if (StringUtils.isEmpty(gamesOnPage)) {
      gamesOnPage = "3";
    }
    return Integer.valueOf(gamesOnPage);
  }

  public void setMyGames(boolean myGames) {
    setBasicCookie(MY_GAMES, String.valueOf(myGames));
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
    setBasicCookie(TIME_ON_PLAY, String.valueOf(value));
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
    setBasicCookie(TIME_ON_PLAY_CUSTOM, String.valueOf(value));
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
    setBasicCookie(FISHER_TIME, String.valueOf(value));
  }

  @Override
  public void setLastUsedSmiles(List<String> lastUsedSmilesQueue) {
    StringBuilder builder = new StringBuilder(lastUsedSmilesQueue.size());
    for (String s : lastUsedSmilesQueue) {
      builder.append(s);
      builder.append(LIST_DELIMITER);
    }
    setBasicCookie(LAST_USED_SMILES, builder.toString());
  }

  @Override
  public void setLocale(String locale) {
    setBasicCookie(LOCALE, locale);
  }

  @Override
  public void setShowAvatars(Boolean value) {
    setBasicCookie(SHOW_AVATARS, String.valueOf(value));
  }

  public boolean isShowAvatars() {
    String hideAvatars = Cookies.getCookie(SHOW_AVATARS);
    if (StringUtils.isEmpty(hideAvatars)) {
      hideAvatars = Boolean.TRUE.toString();
    }
    return Boolean.valueOf(hideAvatars);
  }

  @Override
  public Boolean isPublishGame() {
    String locale = Cookies.getCookie(PUBLISH_GAME);
    if (StringUtils.isEmpty(locale)) {
      return Boolean.TRUE;
    }
    return Boolean.valueOf(locale);
  }

  @Override
  public String getGameType() {
    String gameType = Cookies.getCookie(GAME_TYPE);
    if (StringUtils.isEmpty(gameType)) {
      return messages.draughts();
    }
    return gameType;
  }

  @Override
  public void setGameType(String selectedGameType) {
    setBasicCookie(GAME_TYPE, selectedGameType);
  }

  @Override
  public void setPublishGame(Boolean publishGame) {
    setBasicCookie(PUBLISH_GAME, String.valueOf(publishGame));
  }

  @Override
  public String getDefaultLocale() {
    return DEFAULT_LOCALE;
  }

  @Override
  public String getLocale() {
    String locale = Cookies.getCookie(LOCALE);
    if (StringUtils.isEmpty(locale)) {
      return DEFAULT_LOCALE;
    }
    return locale;
  }

  @Override
  public List<String> getLastUsedSmiles() {
    String s = Cookies.getCookie(LAST_USED_SMILES);
    if (StringUtils.isEmpty(s)) {
      return new ArrayList<>();
    }
    return new ArrayList<>(Arrays.asList(s.split(LIST_DELIMITER)));
  }

  @Override
  public int getFisherTime() {
    String gamesOnPage = Cookies.getCookie(FISHER_TIME);
    if (StringUtils.isEmpty(gamesOnPage)) {
      gamesOnPage = "0";
    }
    return Integer.valueOf(gamesOnPage);
  }

  @Override
  public String getCookie(String cookieName) {
    return Cookies.getCookie(cookieName);
  }

  @Override
  public void setCookie(String cookieName, String cookieValue) {
    setBasicCookie(cookieName, cookieValue);
  }
}
