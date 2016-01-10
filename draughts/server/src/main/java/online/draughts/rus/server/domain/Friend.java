package online.draughts.rus.server.domain;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import online.draughts.rus.server.annotation.Index;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 14:10
 */
public class Friend extends ModelImpl<Friend> {

  @Index
  private Player me;
  @Index
  private Player friendOf;

  @Index
  private boolean favorite;

  public Friend() {
    super(Friend.class);
  }

  public static Friend getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public Player getMe() {
    return me;
  }

  public void setMe(Player me) {
    this.me = me;
  }

  public Player getFriendOf() {
    return friendOf;
  }

  public void setFriendOf(Player friendOf) {
    this.friendOf = friendOf;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }

  // ********* DB Queries ********* //
  public List<Friend> findFriends(long playerId) {
    Query query = new Query(getEntityName());
    Query.Filter friendFilter =
        new Query.FilterPredicate("me",
            Query.FilterOperator.EQUAL,
            KeyFactory.createKey(Player.getInstance().getEntityName(), playerId));
    query.setFilter(friendFilter);
    final PreparedQuery preparedQuery = getDatastore().prepare(query);
    return getListResult(preparedQuery);
  }

  public List<Friend> getPlayerFriends(long playerId, long friendId) {
    Key playerKey = KeyFactory.createKey(Player.getInstance().getEntityName(), playerId);
    Key friendKey = KeyFactory.createKey(Player.getInstance().getEntityName(), friendId);

    Query query = new Query(getEntityName());
    Query.Filter playerFilter =
        new Query.FilterPredicate("me",
            Query.FilterOperator.EQUAL,
            playerKey);
    Query.Filter friendFilter =
        new Query.FilterPredicate("friendOf",
            Query.FilterOperator.EQUAL,
            friendKey);

    Query.Filter playerAndFriendFilter =
        Query.CompositeFilterOperator.and(
            playerFilter,
            friendFilter
        );

    query.setFilter(playerAndFriendFilter);

    final PreparedQuery preparedQuery = getDatastore().prepare(query);
    return getListResult(preparedQuery);
  }

  private static class SingletonHolder {
    private static final Friend INSTANCE = new Friend();
  }
}
