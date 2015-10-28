package online.draughts.rus.server.rest;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.server.utils.AuthUtils;
import online.draughts.rus.shared.model.Friend;
import online.draughts.rus.shared.model.Player;
import online.draughts.rus.shared.rest.PlayersResource;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestScoped
public class PlayersResourceImpl implements PlayersResource {

  private final PlayerService playerService;
  private final HttpServletRequest request;

  @Inject
  PlayersResourceImpl(
      PlayerService playerService,
      HttpServletRequest request) {
    this.playerService = playerService;
    this.request = request;
  }

  @Override
  public Player saveOrCreate(Player player) {
    if (!AuthUtils.isAuthenticated(request.getSession())) {
      throw new RuntimeException("Unauthorized");
    }
    return playerService.saveOrCreate(player);
  }

  @Override
  public List<Friend> getPlayerFriendList(Long playerId) {
    return playerService.findFriends(playerId);
  }
}
