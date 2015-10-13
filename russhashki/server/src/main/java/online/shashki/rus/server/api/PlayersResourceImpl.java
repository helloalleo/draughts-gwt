package online.shashki.rus.server.api;

import com.google.inject.Inject;
import online.shashki.rus.server.dao.PlayerDao;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.api.PlayersResource;
import online.shashki.rus.shared.model.Player;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class PlayersResourceImpl implements PlayersResource {
  private final PlayerDao playerDao;
  private final HttpServletRequest request;

  @Inject
  public PlayersResourceImpl(
      PlayerDao playerDao) {
    this.playerDao = playerDao;
    this.request = null;
  }

  @Override
  public Player saveOrCreate(Player player) {
    return saveOrCreate(player, false);
  }

  public Player saveOrCreate(Player player, boolean serverSide) {
    final Player currentProfile = getCurrentProfile();
    if (currentProfile == null && player != null && player.getId() == null) {
      playerDao.create(player);
      return player;
    }

    if (!(isAuthenticated() || serverSide)) {
      return null;
    }

    if (player == null) {
      return null;
    }

    Player playerById = playerDao.findById(player.getId());

    if (playerById != null) {
      playerById.updateSerializable(playerById);
      playerDao.edit(playerById);
      return playerById;
    }
    return null;
  }

  private Player getCurrentProfile() {
    HttpSession session = request.getSession();
    if (session != null) {
      final Player bySessionId = playerDao.findBySessionId(session.getId());
      System.out.println(bySessionId);
      return bySessionId;
    }
    return null;
  }

  private Boolean isAuthenticated() {
    return AuthUtils.isAuthenticated(request.getSession());
  }
}
