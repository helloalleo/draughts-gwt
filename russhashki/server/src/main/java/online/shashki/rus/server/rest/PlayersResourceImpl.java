package online.shashki.rus.server.rest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.shashki.rus.server.dao.PlayerDao;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.rest.PlayersResource;
import online.shashki.rus.shared.model.Player;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Singleton
public class PlayersResourceImpl implements PlayersResource {
  private Provider<PlayerDao> playerDaoProvider;
  private Provider<HttpServletRequest> requestProvider;

  public PlayersResourceImpl() {
  }

  @Inject
  PlayersResourceImpl(
      Provider<PlayerDao> playerDaoProvider
      /*HttpServletRequest requestProvider*/) {
    this.playerDaoProvider = playerDaoProvider;
    this.requestProvider = null;
  }

  @Override
  public Player saveOrCreate(Player player) {
    return saveOrCreate(player, false);
  }

  public Player saveOrCreate(Player player, boolean serverSide) {
    final Player currentProfile = getCurrentProfile();
    if (currentProfile == null && player != null && player.getId() == null) {
      playerDaoProvider.get().create(player);
      return player;
    }

    if (!(isAuthenticated() || serverSide)) {
      return null;
    }

    if (player == null) {
      return null;
    }

    Player playerById = playerDaoProvider.get().findById(player.getId());

    if (playerById != null) {
      playerById.updateSerializable(playerById);
      playerDaoProvider.get().edit(playerById);
      return playerById;
    }
    return null;
  }

  private Player getCurrentProfile() {
    HttpSession session = requestProvider.get().getSession();
    if (session != null) {
      final Player bySessionId = playerDaoProvider.get().findBySessionId(session.getId());
      System.out.println(bySessionId);
      return bySessionId;
    }
    return null;
  }

  private Boolean isAuthenticated() {
    return AuthUtils.isAuthenticated(requestProvider.get().getSession());
  }
}
