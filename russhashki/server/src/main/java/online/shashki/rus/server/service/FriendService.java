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

  public void saveOrCreate(Friend friend) {
    if (friend == null) {
      return;
    }

    logger.info("New friend: " + friend.toString());
    if (friend.getPk() == null) {
      friendDaoProvider.get().create(friend);
    } else {
      friendDaoProvider.get().edit(friend);
    }
  }
}
