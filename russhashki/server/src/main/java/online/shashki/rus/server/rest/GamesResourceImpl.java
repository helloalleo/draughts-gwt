package online.shashki.rus.server.rest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import online.shashki.rus.server.dao.GameDao;
import online.shashki.rus.server.service.PlayerService;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.Player;
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
public class GamesResourceImpl implements GamesResource {

  private final Provider<GameDao> gameDaoProvider;
  private final PlayerService playerService;
  private final Provider<HttpServletRequest> requestProvider;

  @Inject
  public GamesResourceImpl(Provider<GameDao> gameDaoProvider,
                           PlayerService playerService,
                           Provider<HttpServletRequest> requestProvider) {
    this.gameDaoProvider = gameDaoProvider;
    this.playerService = playerService;
    this.requestProvider = requestProvider;
  }

  @Override
  public List<Game> getGames(@DefaultValue(DEFAULT_OFFSET) int offset, @DefaultValue(DEFAULT_LIMIT) int limit) {
    return gameDaoProvider.get().findRange(offset, limit);
  }

  @Override
  public List<Game> getLoggedInUserGames(@DefaultValue(DEFAULT_OFFSET) int offset, @DefaultValue(DEFAULT_LIMIT) int limit) {
    final Player loggedInUser = playerService.getLoggedInUser(requestProvider.get().getSession());
    if (loggedInUser == null) {
      throw new RuntimeException("Not authorized");
    }
    return gameDaoProvider.get().findUserGames(loggedInUser.getId(), offset, limit);
  }

  @Override
  public Long getGamesCount() {
    return gameDaoProvider.get().countAll();
  }

  @Override
  public Game saveOrCreate(Game game) {
    if (game.getId() == null) {
      gameDaoProvider.get().create(game);
    } else {
      gameDaoProvider.get().edit(game);
    }
    return game;
  }

  @Override
  public Game game(Long gameId) {
    return gameDaoProvider.get().find(gameId);
  }
}
