package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.domain.Friend;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class FriendService {

  @Inject
  FriendService(
      ) {
  }

  public Friend save(Friend friend) {
    if (friend == null) {
      return null;
    }

//    logger.info("New friend: " + friend.toString());
//    if (friend.getId() == null) {
     return new Friend();//friendDaoProvider.get().save(friend);
//    } else {
//      Friend friendById = friendDaoProvider.get().find(friend.getId());
//      friendById.setFavorite(friend.isFavorite());
//      friendDaoProvider.get().save(friendById);
//    }
//    return friend;
  }

  public List<Friend> findFriends(Long playerId) {
    return new ArrayList<>();
    //return friendDaoProvider.get().findFriends(playerId);
  }

  public Friend findById(Long friendId) {
    return new Friend();
//    return friendDaoProvider.get().find(friendId);
  }

  public boolean isPlayerFriendOf(Long playerId, Long friendId) {
    return false;//!friendDaoProvider.get().getPlayerFriends(playerId, friendId).isEmpty();
  }
}
