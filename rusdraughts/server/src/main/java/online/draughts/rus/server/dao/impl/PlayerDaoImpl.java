package online.draughts.rus.server.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.persist.Transactional;
import online.draughts.rus.server.dao.PlayerDao;
import online.draughts.rus.shared.model.Friend;
import online.draughts.rus.shared.model.Player;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 0:19
 */
public class PlayerDaoImpl extends DaoImpl<Player> implements PlayerDao {

  private final Provider<EntityManager> entityManagerProvider;
  private final Logger log;

  @Inject
  public PlayerDaoImpl(TypeLiteral<Player> type, Provider<EntityManager> entityManagerProvider, Logger log) {
    super(type);
    this.entityManagerProvider = entityManagerProvider;
    this.log = log;
  }

  @Override
  protected EntityManager getEntityManager() {
    return entityManagerProvider.get();
  }

  @Override
  @Transactional
  public Player findByVkId(String vkId) {
    try {
      return findByParam("Player", new String[]{"vkId"}, new String[]{vkId});
    } catch (IllegalArgumentException e) {
      log.severe(e.getLocalizedMessage());
      return null;
    }
  }

  @Override
  @Transactional
  public Player findBySessionId(String sessionId) {
    try {
      return findByParam("Player", new String[]{"sessionId"}, new String[]{sessionId});
    } catch (IllegalArgumentException e) {
      log.severe(e.getLocalizedMessage());
      return null;
    }
  }

  @Override
  @Transactional
  public Player findById(Long playerId) {
    try {
      return findByParam("Player", new String[]{"id"}, new Long[]{playerId});
    } catch (IllegalArgumentException e) {
      log.severe(e.getLocalizedMessage());
      return null;
    }
  }

  @Override
  @Transactional
  public Player findByFbId(String user_id) {
    try {
      return findByParam("Player", new String[]{"fbId"}, new String[]{user_id});
    } catch (IllegalArgumentException e) {
      log.severe(e.getLocalizedMessage());
      return null;
    }
  }

  @Override
  @Transactional
  public Player findByGoogleSub(String sub) {
    try {
      return findByParam("Player", new String[]{"googleSub"}, new String[]{sub});
    } catch (IllegalArgumentException e) {
      log.severe(e.getLocalizedMessage());
      return null;
    }
  }

  @Override
  @Transactional
  public List<Player> findLoggedIn() {
    String hql = "SELECT p " +
        " FROM Player p " +
        " WHERE p.loggedIn = true " +
        " ORDER BY p.online DESC";
    Query query = getEntityManager().createQuery(hql);
    return query.getResultList();
  }

  @Override
  @Transactional
  public List<Friend> findFriends(Long playerId) {
    String hql = "SELECT f " +
        " FROM Player p " +
        " LEFT JOIN p.friends f " +
        " WHERE p.id = :playerId " +
        "   AND f.pk.friend.loggedIn = true " +
        " ORDER BY f.favorite DESC, f.pk.friend.online DESC";
    Query query = getEntityManager().createQuery(hql);
    query.setParameter("playerId", playerId);
    query.setMaxResults(10);
    return (List<Friend>) query.getResultList();
  }
}
