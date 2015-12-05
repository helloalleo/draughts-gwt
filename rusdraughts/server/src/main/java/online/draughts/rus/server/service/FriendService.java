package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.draughts.rus.server.dao.FriendDao;
import online.draughts.rus.server.domain.Friend;
import online.draughts.rus.shared.dto.FriendDto;
import org.dozer.Mapper;

import java.util.List;
import java.util.logging.Logger;

@Singleton
public class FriendService {
  private Logger logger;
  private Provider<FriendDao> friendDaoProvider;
  private final Mapper mapper;

  @Inject
  FriendService(
      Logger logger,
      Provider<FriendDao> gameDaoProvider,
      Mapper mapper) {
    this.logger = logger;
    this.friendDaoProvider = gameDaoProvider;
    this.mapper = mapper;
  }
  public Friend save(Friend friend) {
    if (friend == null) {
      return null;
    }

//    logger.info("New friend: " + friend.toString());
//    if (friend.getId() == null) {
    return friendDaoProvider.get().save(friend);
  }
  public FriendDto save(FriendDto friend) {
    if (friend == null) {
      return null;
    }

//    logger.info("New friend: " + friend.toString());
//    if (friend.getId() == null) {
    Friend mapped = mapper.map(friend, Friend.class);
     Friend saved = friendDaoProvider.get().save(mapped);
    return mapper.map(saved, FriendDto.class);
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
