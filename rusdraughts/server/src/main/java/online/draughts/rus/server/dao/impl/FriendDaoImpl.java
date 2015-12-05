package online.draughts.rus.server.dao.impl;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import online.draughts.rus.server.dao.FriendDao;
import online.draughts.rus.shared.model.Friend;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 21:55
 */
public class FriendDaoImpl extends DaoImpl<Friend> implements FriendDao {

  @Inject
  public FriendDaoImpl(TypeLiteral<Friend> type) {
    super(type);
  }

  @Override
  public List<Friend> getPlayerFriends(Long playerId, Long friendId) {
//    String hql = "SELECT f " +
//        " FROM Friend f " +
//        " WHERE f.pk.friend.id = :playerId " +
//        "   AND f.pk.friendOf.id = :friendId";
//    Query query = getEntityManager().createQuery(hql);
//    query.setParameter("playerId", playerId);
//    query.setParameter("friendId", friendId);
//    List result = query.getResultList();
//    return !result.isEmpty();

    return ofy().load().type(getEntityClass())
        .filter("pk.friend.id", playerId)
        .filter("pk.friendOf.id", friendId)
        .list();
  }

  @Override
  public List<Friend> findFriends(Long playerId) {
//    String hql = "SELECT f " +
//        " FROM Player p " +
//        " LEFT JOIN p.friends f " +
//        " WHERE p.id = :playerId " +
//        "   AND f.pk.friend.loggedIn = true " +
//        " ORDER BY f.favorite DESC, f.pk.friend.online DESC";
//    Query query = getEntityManager().createQuery(hql);
//    query.setParameter("playerId", playerId);
//    query.setMaxResults(10);
//    return (List<Friend>) query.getResultList();

    return ofy().load().type(getEntityClass())
        .filter("id", playerId)
        .filter("pk.friend", playerId)
        .filter("loggedIn", true)
        .list();
  }

  @Override
  public Friend find(String id) {
    return ofy().load().type(getEntityClass()).id(id).now();
  }

}
