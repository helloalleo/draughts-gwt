package online.draughts.rus.server.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.server.util.AuthUtils;
import online.draughts.rus.server.domain.Friend;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.shared.resource.PlayersResource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import java.util.List;

@RequestScoped
public class PlayersResourceImpl implements PlayersResource {

  private final PlayerService playerService;
  private final Provider<Boolean> authProvider;

  @Inject
  PlayersResourceImpl(
      @Named(AuthUtils.AUTHENTICATED) Provider<Boolean> authProvider,
      PlayerService playerService,
      HttpServletRequest request) {
    this.playerService = playerService;
    this.authProvider = authProvider;
  }

  @Override
  public Player saveOrCreate(Player player) {
    if (!authProvider.get()) {
      throw new NotAuthorizedException("Access denied");
    }
    return playerService.saveOrCreate(player);
  }

  @Override
  public List<Friend> getPlayerFriendList(Long playerId) {
    return playerService.findFriends(playerId);
  }

  @Override
  public Integer totalPlayers() {
    return playerService.totalPlayers();
  }

  @Override
  public Integer onlinePlayers() {
    return playerService.onlinePlayers();
  }

  @Override
  public void resetUnreadMessages(Long playerId, Long friendId) {
    playerService.resetUnreadMessages(playerId, friendId);
  }
}
