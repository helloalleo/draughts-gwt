package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.draughts.rus.server.dao.FriendDao;
import online.draughts.rus.server.domain.Friend;

import java.util.logging.Logger;

@Singleton
public class FriendService {
  private Logger logger;
  private Provider<FriendDao> friendDaoProvider;

  @Inject
  FriendService(
      Logger logger,
      Provider<FriendDao> gameDaoProvider) {
    this.logger = logger;
    this.friendDaoProvider = gameDaoProvider;
  }

  public Friend saveOrCreate(Friend friend) {
    if (friend == null) {
      return null;
    }

    logger.info("New friend: " + friend.toString());
    if (friend.getId() == null) {
      friendDaoProvider.get().save(friend);
    } else {
      Friend friendById = friendDaoProvider.get().find(friend.getId());
      friendById.setFavorite(friend.isFavorite());
      friendDaoProvider.get().save(friendById);
    }
    return friend;
  }

  public Friend findById(Long friendId) {
    return friendDaoProvider.get().find(friendId);
  }

  public boolean isPlayerFriendOf(Long playerId, Long friendId) {
    return !friendDaoProvider.get().getPlayerFriends(playerId, friendId).isEmpty();
  }
}
