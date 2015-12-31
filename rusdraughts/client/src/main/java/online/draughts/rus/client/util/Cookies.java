package online.draughts.rus.client.util;

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

  int getFisherTime();

  void setFisherTime(String value);
}
