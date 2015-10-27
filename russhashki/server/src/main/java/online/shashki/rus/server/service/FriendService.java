package online.shashki.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.shashki.rus.server.dao.FriendDao;
import online.shashki.rus.shared.model.Friend;

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
      friendDaoProvider.get().create(friend);
    } else {
      friendDaoProvider.get().edit(friend);
    }
    return friend;
  }

  public Friend findById(Long friendId) {
    return friendDaoProvider.get().find(friendId);
  }
}
