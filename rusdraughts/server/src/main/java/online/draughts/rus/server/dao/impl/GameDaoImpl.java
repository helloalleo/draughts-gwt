package online.draughts.rus.server.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.persist.Transactional;
import online.draughts.rus.server.dao.GameDao;
import online.draughts.rus.shared.model.Game;
import online.draughts.rus.shared.model.Move;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 17:34
 */
public class GameDaoImpl extends DaoImpl<Game> implements GameDao {

  private final Provider<EntityManager> entityManagerProvider;

  @Inject
  public GameDaoImpl(TypeLiteral<Game> type, Provider<EntityManager> entityManagerProvider) {
    super(type);
    this.entityManagerProvider = entityManagerProvider;
  }

  @Override
  protected EntityManager getEntityManager() {
    return entityManagerProvider.get();
  }

  @Override
  @Transactional
  public Game findLazyFalse(Long id) {
    String hql = "SELECT g " +
        "FROM Game g " +
        "JOIN FETCH g.playerWhite " +
        "JOIN FETCH g.playerBlack " +
        "WHERE g.id = :gameId";
    Query query = getEntityManager().createQuery(hql);
    query.setParameter("gameId", id);
    return (Game) query.getSingleResult();
  }

  @Override
  @Transactional
  public List<Game> findRange(int start, int length) {
    String hql = "SELECT g " +
        " FROM Game g " +
        " JOIN FETCH g.playerWhite " +
        " JOIN FETCH g.playerBlack " +
        " WHERE g.playFinishDate IS NOT NULL " +
        " ORDER BY g.playFinishDate DESC";
    Query query = getEntityManager().createQuery(hql);
    query.setFirstResult(start);
    query.setMaxResults(length);
    return query.getResultList();
  }

  @Override
  @Transactional
  public List<Game> findUserGames(Long userId, int start, int length) {
    String hql = "SELECT g " +
        " FROM Game g " +
        " JOIN FETCH g.playerWhite white " +
        " JOIN FETCH g.playerBlack black " +
        " WHERE (white.id = :userId " +
        "    OR black.id = :userId) " +
        "   AND g.playStartDate IS NOT NULL" +
        " ORDER BY g.playStartDate DESC";
    Query query = getEntityManager().createQuery(hql);
    query.setParameter("userId", userId);
    query.setFirstResult(start);
    query.setMaxResults(length);
    return query.getResultList();
  }

  @Override
  public List<Move> findGameMoves(Long gameId) {
    String hql = "SELECT gm.move " +
        " FROM GameMessage gm " +
        " WHERE gm.game.id = :gameId";
    Query query = getEntityManager().createQuery(hql);
    query.setParameter("gameId", gameId);
    return query.getResultList();
  }
}
