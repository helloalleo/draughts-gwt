package online.shashki.rus.server.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import online.shashki.rus.server.dao.FriendDao;
import online.shashki.rus.shared.model.Friend;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 21:55
 */
public class FriendDaoImpl extends DaoImpl<Friend> implements FriendDao {

  private final Provider<EntityManager> entityManagerProvider;

  @Inject
  public FriendDaoImpl(TypeLiteral<Friend> type, Provider<EntityManager> entityManagerProvider) {
    super(type);
    this.entityManagerProvider = entityManagerProvider;
  }

  @Override
  protected EntityManager getEntityManager() {
    return entityManagerProvider.get();
  }

  @Override
  public Friend findById(Long friendId) {
    String hql = "SELECT f " +
        " FROM Friend f " +
        " WHERE f.id = :friendId";
    Query query = getEntityManager().createQuery(hql);
    query.setParameter("friendId", friendId);
    return (Friend) query.getSingleResult();
  }

  @Override
  public boolean isPlayerFriendOf(Long playerId, Long friendId) {
    String hql = "SELECT f " +
        " FROM Friend f " +
        " WHERE f.pk.friend.id = :playerId " +
        "   AND f.pk.friendOf.id = :friendId";
    Query query = getEntityManager().createQuery(hql);
    query.setParameter("playerId", playerId);
    query.setParameter("friendId", friendId);
    List result = query.getResultList();
    return !result.isEmpty();
  }
}
