package online.draughts.rus.client.util;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 18:15
 */
public interface Cookies {

  void setLocation(String nameToken);
  String getLocation();

  void setNewGameButtonState(boolean newGame);
  boolean getNewGameButtonState();

  void setGamesOnPageCounter(int value);
  int getGamesOnPageCounter();

  void setMyGames(boolean myGames);
  boolean isMyGames();
}
