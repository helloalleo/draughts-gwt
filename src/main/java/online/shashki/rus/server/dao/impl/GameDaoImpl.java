package online.shashki.rus.server.dao.impl;

import online.shashki.rus.server.dao.GameDao;
import online.shashki.rus.shared.model.Game;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 17:34
 */
@Stateless
public class GameDaoImpl extends DaoImpl<Game> implements GameDao {

  @Inject
  private EntityManager entityManager;

  public GameDaoImpl() {
    super(Game.class);
  }

  @Override
  protected EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  public Game findLazyFalse(Long id) {
    String hql = "SELECT g " +
        "FROM Game g " +
        "JOIN FETCH g.playerWhite " +
        "JOIN FETCH g.playerBlack " +
        "WHERE g.id = :gameId";
    Query query = entityManager.createQuery(hql);
    query.setParameter("gameId", id);
    return (Game) query.getSingleResult();
  }

  @Override
  public List<Game> findRange(int start, int length) {
    String hql = "SELECT g " +
        "FROM Game g " +
        "JOIN FETCH g.playerWhite " +
        "JOIN FETCH g.playerBlack";
    Query query = entityManager.createQuery(hql);
    query.setFirstResult(start);
    query.setMaxResults(length);
    return query.getResultList();
  }
}
