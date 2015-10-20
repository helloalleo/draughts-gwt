package online.shashki.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.shashki.rus.server.dao.PlayerDao;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.model.Player;

import javax.servlet.http.HttpSession;
import java.util.List;

@Singleton
public class PlayerService {
  private Provider<PlayerDao> playerDaoProvider;

  @Inject
  PlayerService(
      Provider<PlayerDao> playerDaoProvider) {
    this.playerDaoProvider = playerDaoProvider;
  }

  public Player saveOrCreate(HttpSession session, Player player) {
    return saveOrCreate(session, player, false);
  }

  public Player saveOrCreate(HttpSession session, Player player, boolean serverSide) {
    final Player currentProfile = getLoggedInUser(session);
    if (currentProfile == null && player != null && player.getId() == null) {
      playerDaoProvider.get().create(player);
      return player;
    }

    if (!(AuthUtils.isAuthenticated(session) || serverSide)) {
      return null;
    }

    if (player == null) {
      return null;
    }

    Player playerById = playerDaoProvider.get().findById(player.getId());

    if (playerById != null) {
      playerById.updateSerializable(player);
      playerDaoProvider.get().edit(playerById);
      return playerById;
    }
    return null;
  }

  public Player getLoggedInUser(HttpSession session) {
    if (session != null) {
      final Player bySessionId = playerDaoProvider.get().findBySessionId(session.getId());
      System.out.println(bySessionId);
      return bySessionId;
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
}
