package online.shashki.rus.server.rest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;
import online.shashki.rus.server.service.GameService;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.rest.GamesResource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
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
  private final Provider<HttpServletRequest> requestProvider;

  @Inject
  public GamesResourceImpl(GameService gameService,
                           Provider<HttpServletRequest> requestProvider) {
    this.gameService = gameService;
    this.requestProvider = requestProvider;
  }

  @Override
  public List<Game> getGames(@DefaultValue(DEFAULT_OFFSET) int offset, @DefaultValue(DEFAULT_LIMIT) int limit) {
    return gameService.findRange(offset, limit);
  }

  @Override
  public List<Game> getLoggedInUserGames(@DefaultValue(DEFAULT_OFFSET) int offset, @DefaultValue(DEFAULT_LIMIT) int limit) {
    return gameService.findUserGames(requestProvider.get().getSession(), offset, limit);
  }

  @Override
  public Long getGamesCount() {
    return gameService.getGamesCount();
  }

  @Override
  public Game saveOrCreate(Game game) {
    return gameService.saveOrCreate(game);
  }

  @Override
  public Game game(Long gameId) {
    return gameService.find(gameId);
  }
}
