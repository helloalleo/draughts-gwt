package online.draughts.rus.client.util;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 18:15
 */
public interface Cookies {

  String getLocation();

  void setLocation(String nameToken);

  int getGamesOnPageCounter();

  void setGamesOnPageCounter(int value);

  boolean isMyGames();

  void setMyGames(boolean myGames);

  int getTimeOnPlay();

  void setTimeOnPlay(String value);

  void setTimeOnPlayCustom(String value);

  boolean getTimeOnPlayCustom();

  void setPublishGame(Boolean publishGame);

  String getDefaultLocale();

  String getLocale();

  List<String> getLastUsedSmiles();

  int getFisherTime();

  void setFisherTime(String value);

  void setLastUsedSmiles(List<String> lastUsedSmilesQueue);

  void setLocale(String locale);

  void setShowAvatars(Boolean value);

  boolean isShowAvatars();

  Boolean isPublishGame();

  String getGameType();

  void setGameType(String selectedGameType);
}
