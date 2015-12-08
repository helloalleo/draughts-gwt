package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.draughts.rus.server.dao.FriendDao;
import online.draughts.rus.server.domain.Friend;

import java.util.List;

@Singleton
public class FriendService {
  private Provider<FriendDao> friendDaoProvider;

  @Inject
  FriendService(
      Provider<FriendDao> gameDaoProvider) {
    this.friendDaoProvider = gameDaoProvider;
  }

  public Friend save(Friend friend) {
    if (friend == null) {
      return null;
    }

//    logger.info("New friend: " + friend.toString());
//    if (friend.getId() == null) {
     return friendDaoProvider.get().save(friend);
//    } else {
//      Friend friendById = friendDaoProvider.get().find(friend.getId());
//      friendById.setFavorite(friend.isFavorite());
//      friendDaoProvider.get().save(friendById);
//    }
//    return friend;
  }

  public List<Friend> findFriends(Long playerId) {
    return friendDaoProvider.get().findFriends(playerId);
  }

  public Friend findById(Long friendId) {
    return friendDaoProvider.get().find(friendId);
  }

  public boolean isPlayerFriendOf(Long playerId, Long friendId) {
    return !friendDaoProvider.get().getPlayerFriends(playerId, friendId).isEmpty();
  }
}
