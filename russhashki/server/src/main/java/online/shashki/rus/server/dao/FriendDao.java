package online.shashki.rus.server.dao;

import online.shashki.rus.shared.model.Friend;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 18:04
 */
public interface FriendDao extends Dao<Friend> {

  Friend findById(Long friendId);
}
