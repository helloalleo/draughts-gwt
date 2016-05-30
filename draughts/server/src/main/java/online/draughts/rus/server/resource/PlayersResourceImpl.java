package online.draughts.rus.server.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.server.util.AuthUtils;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.exception.BannedException;
import online.draughts.rus.shared.resource.PlayersResource;
import org.apache.log4j.Logger;
import org.dozer.Mapper;

import javax.servlet.http.HttpSession;

@RequestScoped
public class PlayersResourceImpl implements PlayersResource {

  private Logger logger = Logger.getLogger(PlayersResourceImpl.class);
  private final PlayerService playerService;
  private final Provider<Boolean> authProvider;
  private final Provider<HttpSession> sessionProvider;
  private final Mapper mapper;

  @Inject
  PlayersResourceImpl(
      @Named(AuthUtils.AUTHENTICATED) Provider<Boolean> authProvider,
      @Named(AuthUtils.SESSION) Provider<HttpSession> sessionProvider,
      PlayerService playerService,
      Mapper mapper) {
    this.playerService = playerService;
    this.authProvider = authProvider;
    this.sessionProvider = sessionProvider;
    this.mapper = mapper;
  }

  @Override
  public PlayerDto save(PlayerDto player){
    if (!authProvider.get()) {
      return null;
    }
    final Player saved;
    try {
      saved = playerService.saveDto(player);
    } catch (BannedException e) {
      sessionProvider.get().invalidate();
      throw new RuntimeException(e.getMessage());
    }
    return mapper.map(saved, PlayerDto.class);
  }

  @Override
  public String subscribeOnNotifications(PlayerDto playerDto) {
    if (!authProvider.get()) {
      return "Unauthorized";
    }
    Player player = playerService.findBySessionId(playerDto.getSessionId());
    if (null == player) {
      return "Player not found";
    }
    player.setNotificationsUserId(playerDto.getNotificationsUserId());
    playerService.save(player);
    return "";
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
