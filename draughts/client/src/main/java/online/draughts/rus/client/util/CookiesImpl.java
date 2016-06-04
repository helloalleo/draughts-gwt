package online.draughts.rus.client.util;

import com.google.gwt.user.client.Cookies;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.shared.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.09.15
 * Time: 17:31
 */
public class CookiesImpl implements online.draughts.rus.client.util.Cookies {

  private static final String TIME_ON_PLAY = "TIME_ON_PLAY";
  private static final String FISHER_TIME = "FISHER_TIME";
  private static final String TIME_ON_PLAY_CUSTOM = "TIME_ON_PLAY_CUSTOM";
  private static final String LAST_USED_SMILES = "LAST_USED_SMILES";
  private static final String LIST_DELIMITER = ",";
  public static final String LOCALE = "LOCALE";
  private static final String DEFAULT_LOCALE = "ru";
  private static final String SHOW_AVATARS = "SHOW_AVATARS";
  private static final String PUBLISH_GAME = "PUBLISH_GAME";
  private final String GAMES_ON_PAGE_COUNTER = "GAMES_ON_PAGE";
  private final String LOCATION = "LOCATION"; // куки адреса страницы
  private final String MY_GAMES = "MY_GAMES";

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
      gamesOnPage = "3";
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
  public void setLastUsedSmiles(List<String> lastUsedSmilesQueue) {
    StringBuilder builder = new StringBuilder(lastUsedSmilesQueue.size());
    for (String s : lastUsedSmilesQueue) {
      builder.append(s);
      builder.append(LIST_DELIMITER);
    }
    Cookies.setCookie(LAST_USED_SMILES, builder.toString());
  }

  @Override
  public void setLocale(String locale) {
    Cookies.setCookie(LOCALE, locale);
  }

  @Override
  public void setShowAvatars(Boolean value) {
    Cookies.setCookie(SHOW_AVATARS, String.valueOf(value));
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
  public void setPublishGame(Boolean publishGame) {
    Cookies.setCookie(PUBLISH_GAME, String.valueOf(publishGame));
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
}
