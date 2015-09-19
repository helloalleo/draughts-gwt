package online.shashki.rus.shared.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 15:47
 */
public interface GameMessage extends Message {

  Game getGame();

  void setGame(Game gameId);

  List<Shashist> getPlayerList();

  void setPlayerList(List<Shashist> playerList);

  Move getMove();

  void setMove(Move move);
}
