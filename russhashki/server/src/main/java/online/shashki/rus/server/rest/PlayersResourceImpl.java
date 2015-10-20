package online.shashki.rus.server.rest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.shashki.rus.server.dao.PlayerDao;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.model.Player;
import online.shashki.rus.shared.rest.PlayersResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Singleton
public class PlayersResourceImpl implements PlayersResource {
  private Provider<PlayerDao> playerDaoProvider;
  private Provider<HttpServletRequest> requestProvider;

  public PlayersResourceImpl() {
  }

  @Inject
  PlayersResourceImpl(
      Provider<PlayerDao> playerDaoProvider,
      Provider<HttpServletRequest> requestProvider) {
    this.playerDaoProvider = playerDaoProvider;
    this.requestProvider = requestProvider;
  }

  @Override
  public Player saveOrCreate(Player player) {
    return saveOrCreate(player, false);
  }

  public Player saveOrCreate(Player player, boolean serverSide) {
    final Player currentProfile = getLoggedInUser();
    if (currentProfile == null && player != null && player.getId() == null) {
      playerDaoProvider.get().create(player);
      return player;
    }

    if (!(AuthUtils.isAuthenticated(requestProvider.get().getSession()) || serverSide)) {
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

  public Player getLoggedInUser() {
    HttpSession session = requestProvider.get().getSession();
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
