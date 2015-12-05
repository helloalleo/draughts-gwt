package online.draughts.rus.server.dao;

import online.draughts.rus.shared.model.Game;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 17:33
 */
public interface GameDao extends Dao<Game> {

  List<Game> findRange(int start, int length);

  List<Game> findUserGames(Long userId, int start, int length);
}
