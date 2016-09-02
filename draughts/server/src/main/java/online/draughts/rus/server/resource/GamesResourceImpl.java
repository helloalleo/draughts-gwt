package online.draughts.rus.server.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import online.draughts.rus.server.service.GameService;
import online.draughts.rus.server.util.AuthUtils;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.resource.GamesResource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.NotAuthorizedException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.10.15
 * Time: 9:48
 */
@RequestScoped
public class GamesResourceImpl implements GamesResource {

  private final GameService gameService;
  private final HttpServletRequest request;
  private final Provider<Boolean> authProvider;

  @Inject
  public GamesResourceImpl(
      @Named(AuthUtils.AUTHENTICATED) Provider<Boolean> authProvider,
      GameService gameService,
      HttpServletRequest request) {
    this.gameService = gameService;
    this.request = request;
    this.authProvider = authProvider;
  }

  @Override
  public List<GameDto> getGames(@DefaultValue(DEFAULT_OFFSET) int offset, @DefaultValue(DEFAULT_LIMIT) int limit) {
    return gameService.findRange(offset, limit);
  }

  @Override
  public List<GameDto> getLoggedInUserGames(@DefaultValue(DEFAULT_OFFSET) int offset, @DefaultValue(DEFAULT_LIMIT) int limit) {
    if (!authProvider.get()) {
      throw new NotAuthorizedException("Access denied");
    }
    return gameService.findUserGames(request.getSession(), offset, limit);
  }

  @Override
  public Integer getGamesCount() {
    return gameService.getGamesCount();
  }

  @Override
  public GameDto save(GameDto gameDto) {
    if (!authProvider.get()) {
      throw new NotAuthorizedException("Access denied");
    }
    return gameService.save(gameDto);
  }

  @Override
  public PlayerDto updatePlayerStat(long gameId, long playerId) {
    return null; //gameService.updatePlayerStat(gameId, playerId);
  }

  @Override
  public GameDto game(long gameId) {
    return gameService.findDto(gameId);
  }
}
