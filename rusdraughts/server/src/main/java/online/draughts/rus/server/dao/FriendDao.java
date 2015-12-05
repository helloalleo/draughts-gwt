package online.draughts.rus.server.dao;

import online.draughts.rus.shared.model.Friend;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 18:04
 */
public interface FriendDao extends Dao<Friend> {

  List<Friend> getPlayerFriends(Long playerId, Long friendId);

  List<Friend> findFriends(Long playerId);

  Friend find(String id);
}
