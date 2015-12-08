package online.draughts.rus.server.dao.impl;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.googlecode.objectify.Key;
import online.draughts.rus.server.dao.FriendDao;
import online.draughts.rus.server.domain.Friend;
import online.draughts.rus.server.domain.Player;

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
    Key playerKey = Key.create(Player.class, playerId);
    Key friendKey = Key.create(Player.class, friendId);
    return ofy().load().type(getEntityClass())
        .filter("friend", playerKey)
        .filter("friendOf", friendKey)
        .list();
  }

  @Override
  public List<Friend> findFriends(Long playerId) {
    // TODO сортировать по тем кто в сети
    Key playerKey = Key.create(Player.class, playerId);
    return ofy().load().type(getEntityClass())
        .filter("friend", playerKey)
        .order("-favorite")
        .list();
  }

  @Override
  public Friend find(String id) {
    return ofy().load().type(getEntityClass()).id(id).now();
  }
}
