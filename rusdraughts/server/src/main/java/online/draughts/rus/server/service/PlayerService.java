package online.draughts.rus.server.service;

import com.google.inject.Singleton;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.shared.dto.PlayerDto;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import java.util.List;

@Singleton
public class PlayerService {

  private final Logger logger = Logger.getLogger(PlayerService.class);

  PlayerService() {
  }

  public Player save(Player player) {
    player.update();
    player.flush();
    return player;
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
    return Player.getInstance().find(playerId);
  }

  public List<Player> findAll() {
    return Player.getInstance().findAll();
  }

  public Player findByFbId(String userId) {
    return Player.getInstance().findByFbId(userId);
  }

  public Player findByGoogleSub(String sub) {
    return Player.getInstance().findByGoogleSub(sub);
  }

  public Integer totalPlayers() {
    return Player.getInstance().findAll().size();
  }

  public Integer onlinePlayers() {
    return Player.getInstance().findOnline().size();
  }

  public void resetUnreadMessages(long playerId, Long friendId) {
    Player player = Player.getInstance().find(playerId);
    player.getFriendUnreadMessagesMap().remove(friendId);
    player.update();
  }

  public Player saveDto(PlayerDto dto) {
    Player player = find(dto.getId());
    player.updateSerializable(dto);
    player.update();
    return player;
  }
}
