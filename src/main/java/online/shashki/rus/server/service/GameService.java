package online.shashki.rus.server.service;

import online.shashki.rus.server.dao.Dao;
import online.shashki.rus.server.dao.GameDao;
import online.shashki.rus.shared.model.Game;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 17:38
 */
@Stateless
public class GameService extends BaseService<Game> {

  @Inject
  private GameDao gameDao;

  @Override
  protected Dao<Game> getDao() {
    return gameDao;
  }

  @Override
  public List<Game> findRange(int start, int length) {
    return gameDao.findRange(start, length);
  }

  public Game findLazyFalse(Long id) {
    return gameDao.findLazyFalse(id);
  }
}
