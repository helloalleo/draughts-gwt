package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.draughts.rus.server.dao.PlayerDao;
import online.draughts.rus.server.domain.Player;

import javax.servlet.http.HttpSession;
import java.util.List;

@Singleton
public class PlayerService {

  private final Provider<PlayerDao> playerDaoProvider;

  @Inject
  PlayerService(
      Provider<PlayerDao> playerDaoProvider) {
    this.playerDaoProvider = playerDaoProvider;
  }

  public Player save(Player player) {
//    if (player != null && player.getId() == null) {
    return playerDaoProvider.get().save(player);
//    }

//    if (player == null) {
//      return null;
//    }
//    final Player mapped = mapper.map(player, Player.class);

//    Player playerById = playerDaoProvider.get().find(player.getId());

//    if (playerById != null) {
//      playerById.updateSerializable(player);
//      return playerDaoProvider.get().save(mapped);
//      return playerById;
//    }
//    return null;
  }

  public Player getLoggedInUser(HttpSession session) {
    if (session != null) {
      return playerDaoProvider.get().findBySessionId(session.getId());
    }
    return null;
  }

  public Player findByVkId(String vkId) {
    return playerDaoProvider.get().findByVkId(vkId);
  }

  public Player findBySessionId(String sessionId) {
    return playerDaoProvider.get().findBySessionId(sessionId);
  }

  public Player find(Long playerId) {
    return playerDaoProvider.get().find(playerId);
  }

  public List<Player> findAll() {
    return playerDaoProvider.get().findAll();
  }

  public Player findByFbId(String user_id) {
    return playerDaoProvider.get().findByFbId(user_id);
  }

  public Player findByGoogleSub(String sub) {
    return playerDaoProvider.get().findByGoogleSub(sub);
  }

  public Integer totalPlayers() {
    return playerDaoProvider.get().findAll().size();
  }

  public Integer onlinePlayers() {
    return playerDaoProvider.get().findOnline().size();
  }

  public void resetUnreadMessages(Long playerId, Long friendId) {
    Player player = playerDaoProvider.get().find(playerId);
    player.getFriendUnreadMessagesMap().remove(friendId);
    playerDaoProvider.get().save(player);
  }
}
