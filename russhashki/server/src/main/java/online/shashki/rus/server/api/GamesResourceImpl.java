package online.shashki.rus.server.api;

import online.shashki.rus.server.dao.GameDao;
import online.shashki.rus.server.dao.PlayerDao;
import online.shashki.rus.server.service.PlayerServiceImpl;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.api.GameResource;
import online.shashki.rus.shared.api.GamesResource;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.Player;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GamesResourceImpl implements GamesResource {
  private final Logger logger;
  private final ResourcesFactory resourcesFactory;
  private final GameDao gameDao;
  private final PlayerDao playerDao;
  private final HttpServletRequest request;
  private final PlayerServiceImpl playerResource;

  @Inject
  public GamesResourceImpl(
      ResourcesFactory resourcesFactory,
      GameDao gameDao,
      PlayerDao playerDao
      ) {
    this.logger = null;
    this.resourcesFactory = resourcesFactory;
    this.gameDao = gameDao;
    this.playerDao = playerDao;
    this.request = null;
    this.playerResource = null;
  }

  @Override
  public List<Game> getGames(int offset, int limit) {
    List<Game> cars;

    if (limit == LIMIT_ALL) {
      cars = gameDao.findAll();
    } else {
      cars = gameDao.findRange(offset, limit);
    }

    return cars;
  }

  @Override
  public List<Game> getLoggedInUserGames(@DefaultValue(DEFAULT_OFFSET) int offset, @DefaultValue(DEFAULT_LIMIT) int limit) {
    Player currentUser = playerResource.getCurrentProfile();
    System.out.println(currentUser);
    if (currentUser == null) {
      return new ArrayList<>();
    }
    return gameDao.findUserGames(currentUser.getId(), offset, limit);
  }

  @Override
  public Long getGamesCount() {
    return gameDao.countAll();
  }

  @Override
  public Game saveOrCreate(Game game) {
    if (!AuthUtils.isAuthenticated(request.getSession())) {
      throw new RuntimeException("Unauthorized");
    }
    Player playerWhite = game.getPlayerWhite();
    if (playerWhite == null) {
      return null;
    }
    playerWhite = playerDao.find(playerWhite.getId());
    if (playerWhite == null) {
      return null;
    }
    Player playerBlack = game.getPlayerBlack();
    if (playerBlack == null) {
      return null;
    }
    playerBlack = playerDao.find(playerBlack.getId());
    if (playerBlack == null) {
      return null;
    }

    game.setPlayerWhite(playerWhite);
    game.setPlayerBlack(playerBlack);
    if (game.getId() == null) {
      gameDao.create(game);
    } else {
      gameDao.edit(game);
    }
    return game;
  }

  @Override
  public GameResource game(Long gameId) {
    return resourcesFactory.createGameResource(gameId);
  }
}
