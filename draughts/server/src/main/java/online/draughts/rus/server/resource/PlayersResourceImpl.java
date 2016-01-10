package online.draughts.rus.server.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.server.util.AuthUtils;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.resource.PlayersResource;
import org.dozer.Mapper;

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
      return null;
    }
    final Player saved = playerService.saveDto(player);
    return mapper.map(saved, PlayerDto.class);
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
