package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.domain.Friend;

import java.util.List;

@Singleton
public class FriendService {

  @Inject
  FriendService() {
  }

  public Friend save(Friend friend) {
    friend.update();
    friend.flush();
    return friend;
  }

  public List<Friend> findFriends(long playerId) {
    return Friend.getInstance().findFriends(playerId);
  }

  public boolean isPlayerFriendOf(long playerId, long friendId) {
    return !Friend.getInstance().getPlayerFriends(playerId, friendId).isEmpty();
  }
}
