package online.shashki.rus.server.rest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.shashki.rus.server.dao.GameDao;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.Player;
import online.shashki.rus.shared.rest.GamesResource;

import javax.ws.rs.DefaultValue;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.10.15
 * Time: 9:48
 */
@Singleton
public class GamesResourceImpl implements GamesResource {

  private Provider<GameDao> gameDaoProvider;
  private PlayersResourceImpl playersResource;

  public GamesResourceImpl() {
  }

  @Inject
  public GamesResourceImpl(Provider<GameDao> gameDaoProvider,
                           PlayersResourceImpl playersResource) {
    this.gameDaoProvider = gameDaoProvider;
    this.playersResource = playersResource;
  }

  @Override
  public List<Game> getGames(@DefaultValue(DEFAULT_OFFSET) int offset, @DefaultValue(DEFAULT_LIMIT) int limit) {
    return gameDaoProvider.get().findRange(offset, limit);
  }

  @Override
  public List<Game> getLoggedInUserGames(@DefaultValue(DEFAULT_OFFSET) int offset, @DefaultValue(DEFAULT_LIMIT) int limit) {
    final Player loggedInUser = playersResource.getLoggedInUser();
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
