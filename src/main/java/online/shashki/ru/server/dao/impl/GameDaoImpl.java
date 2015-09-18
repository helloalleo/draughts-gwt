package online.shashki.ru.server.dao.impl;

import online.shashki.ru.server.dao.GameDao;
import online.shashki.ru.shared.model.entity.GameEntity;

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
public class GameDaoImpl extends DaoImpl<GameEntity> implements GameDao {

  @Inject
  private EntityManager entityManager;

  public GameDaoImpl() {
    super(GameEntity.class);
  }

  @Override
  protected EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  public GameEntity findLazyFalse(Long id) {
    String hql = "SELECT g " +
        "FROM GameEntity g " +
        "JOIN FETCH g.playerWhite " +
        "JOIN FETCH g.playerBlack " +
        "WHERE g.id = :gameId";
    Query query = entityManager.createQuery(hql);
    query.setParameter("gameId", id);
    return (GameEntity) query.getSingleResult();
  }

  @Override
  public List<GameEntity> findRange(int start, int length) {
    String hql = "SELECT ge " +
        "FROM GameEntity ge " +
        "JOIN FETCH ge.playerWhite " +
        "JOIN FETCH ge.playerBlack";
    Query query = entityManager.createQuery(hql);
    query.setFirstResult(start);
    query.setMaxResults(length);
    return query.getResultList();
  }
}
