package online.shashki.rus.server.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import online.shashki.rus.server.dao.PlayerDao;
import online.shashki.rus.shared.model.Player;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 0:19
 */
public class PlayerDaoImpl extends DaoImpl<Player> implements PlayerDao {

  private final Provider<EntityManager> entityManagerProvider;

  @Inject
  public PlayerDaoImpl(TypeLiteral<Player> type, Provider<EntityManager> entityManagerProvider) {
    super(type);
    this.entityManagerProvider = entityManagerProvider;
  }

  @Override
  protected EntityManager getEntityManager() {
    return entityManagerProvider.get();
  }

//  @Override
//  public List<Shashist> findAll() {
//    Query query = getEntityManager().createQuery("SELECT sh FROM Shashist sh " +
//        "JOIN FETCH sh.blackRoleGames " +
//        "JOIN FETCH sh.whiteRoleGames");
//    return query.getResultList();
//  }

  @Override
  public Player findByVkId(String uid) {
    Query query = getEntityManager().createQuery("FROM Player " +
            "WHERE vkId = :vkId");
    query.setParameter("vkId", uid);
    List list = query.getResultList();
    return list.isEmpty() ? null : (Player) list.get(0);
  }

  @Override
  public Player findBySessionId(String sessionId) {
    Query query = getEntityManager().createQuery("FROM Player " +
            "WHERE sessionId = :sessionId");
    query.setParameter("sessionId", sessionId);
    List result = query.getResultList();
    if (result.isEmpty()) {
      return null;
    }
    return (Player) result.get(0);
  }

  @Override
  public Player findById(Long playerId) {
    Query query = getEntityManager().createQuery("FROM Player " +
            "WHERE id = :playerId"
    );
    query.setParameter("playerId", playerId);
    List result = query.getResultList();
    if (result.isEmpty()) {
      return null;
    }
    return (Player) result.get(0);
  }
}
