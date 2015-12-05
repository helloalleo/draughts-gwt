package online.draughts.rus.server.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.server.util.AuthUtils;
import online.draughts.rus.shared.dto.FriendDto;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.resource.PlayersResource;
import online.draughts.rus.shared.util.DozerHelper;
import org.dozer.Mapper;

import javax.ws.rs.NotAuthorizedException;
import java.util.List;

@RequestScoped
public class PlayersResourceImpl implements PlayersResource {

  private final PlayerService playerService;
  private final Provider<Boolean> authProvider;
  private final Mapper mapper;

  @Inject
  PlayersResourceImpl(
      @Named(AuthUtils.AUTHENTICATED) Provider<Boolean> authProvider,
      PlayerService playerService,
      Mapper mapper) {
    this.playerService = playerService;
    this.authProvider = authProvider;
    this.mapper = mapper;
  }

  @Override
  public PlayerDto save(PlayerDto player) {
    if (!authProvider.get()) {
      throw new NotAuthorizedException("Access denied");
    }
    Player entity = mapper.map(player, Player.class);
    return mapper.map(playerService.save(entity), PlayerDto.class);
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
