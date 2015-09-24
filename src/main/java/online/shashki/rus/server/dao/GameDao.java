package online.shashki.rus.server.dao;

import online.shashki.rus.shared.model.Game;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 17:33
 */
public interface GameDao extends Dao<Game> {
  Game findLazyFalse(Long id);

  List<Game> findRange(int start, int length);
}
