package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.shared.dto.PlayerDto;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PlayerService {

  private final Logger logger = Logger.getLogger(PlayerService.class);

  @Inject
  PlayerService() {
  }

  public Player save(Player player) {
//    if (player != null && player.getId() == null) {
    player.update();
    return player;
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
      return Player.getInstance().findBySessionId(session.getId());
    }
    return null;
  }

  public Player findByVkId(String vkId) {
    return Player.getInstance().findByVkId(vkId);
  }

  public Player findBySessionId(String sessionId) {
    return Player.getInstance().findBySessionId(sessionId);
  }

  public Player find(Long playerId) {
    return new Player();
//    return Player.getInstance().find(playerId);
  }

  public List<Player> findAll() {
//    return playerDaoProvider.get().findAll();
    return new ArrayList<>();
  }

  public Player findByFbId(String userId) {
//    return playerDaoProvider.get().findByFbId(userId);
    return new Player();
  }

  public Player findByGoogleSub(String sub) {
//    return playerDaoProvider.get().findByGoogleSub(sub);
    return new Player();
  }

  public Integer totalPlayers() {
//    return playerDaoProvider.get().findAll().size();
    return 1;
  }

  public Integer onlinePlayers() {
//    return playerDaoProvider.get().findOnline().size();
    return 1;
  }

  public void resetUnreadMessages(Long playerId, Long friendId) {
//    Player player = playerDaoProvider.get().find(playerId);
//    player.getFriendUnreadMessagesMap().remove(friendId);
//    playerDaoProvider.get().save(player);
  }

  public Player saveDto(PlayerDto dto) {
//    Player player = playerDaoProvider.get().find(dto.getId());
//    player.updateSerializable(dto);
//    return playerDaoProvider.get().save(player);
    return new Player();
  }
}
