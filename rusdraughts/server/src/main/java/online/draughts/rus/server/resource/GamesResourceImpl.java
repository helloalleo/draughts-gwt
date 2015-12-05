package online.draughts.rus.server.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import online.draughts.rus.server.domain.Game;
import online.draughts.rus.server.service.GameService;
import online.draughts.rus.server.util.AuthUtils;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.resource.GamesResource;
import online.draughts.rus.shared.util.DozerHelper;
import org.dozer.Mapper;

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
  private final Mapper mapper;

  @Inject
  public GamesResourceImpl(
      @Named(AuthUtils.AUTHENTICATED) Provider<Boolean> authProvider,
      GameService gameService,
      HttpServletRequest request,
      Mapper mapper) {
    this.gameService = gameService;
    this.request = request;
    this.authProvider = authProvider;
    this.mapper = mapper;
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
    final List<Game> games = gameService.findUserGames(request.getSession(), offset, limit);
    return DozerHelper.map(mapper, games, GameDto.class);
  }

  @Override
  public Integer getGamesCount() {
    return gameService.getGamesCount();
  }

  @Override
  public GameDto save(GameDto game) {
    if (!authProvider.get()) {
      throw new NotAuthorizedException("Access denied");
    }
    Game entity = mapper.map(game, Game.class);
    return mapper.map(gameService.save(entity), GameDto.class);
  }

  @Override
  public GameDto game(Long gameId) {
    if (!authProvider.get()) {
      throw new NotAuthorizedException("Access denied");
    }
    return mapper.map(gameService.find(gameId), GameDto.class);
  }
}
