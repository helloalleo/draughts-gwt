package online.shashki.rus.server.rest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.shashki.rus.server.dao.GameDao;
import online.shashki.rus.server.dao.PlayerDao;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.Player;
import online.shashki.rus.shared.rest.GameResource;
import online.shashki.rus.shared.rest.GamesResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DefaultValue;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class GamesResourceImpl implements GamesResource {
  private Logger logger;
  private ResourcesFactory resourcesFactory;
  private Provider<GameDao> gameDaoProvider;
  private Provider<PlayerDao> playerDaoProvider;
  private Provider<HttpServletRequest> requestProvider;

  public GamesResourceImpl() {
  }

  @Inject
  GamesResourceImpl(
//      Logger logger,
//      ResourcesFactory resourcesFactory,
      Provider<GameDao> gameDaoProvider,
      Provider<PlayerDao> playerDaoProvider
      /*HttpServletRequest requestProvider*/) {
    this.logger = null;
    this.resourcesFactory = null; //resourcesFactory;
    this.gameDaoProvider = gameDaoProvider;
    this.playerDaoProvider = playerDaoProvider;
    this.requestProvider = null;
  }

  @Override
  public List<Game> getGames(int offset, int limit) {
    List<Game> cars;

    if (limit == LIMIT_ALL) {
      cars = gameDaoProvider.get().findAll();
    } else {
      cars = gameDaoProvider.get().findRange(offset, limit);
    }

    return cars;
  }

  @Override
  public List<Game> getLoggedInUserGames(@DefaultValue(DEFAULT_OFFSET) int offset, @DefaultValue(DEFAULT_LIMIT) int limit) {
    Player currentUser = getCurrentProfile();
    System.out.println(currentUser);
    if (currentUser == null) {
      return new ArrayList<>();
    }
    return gameDaoProvider.get().findUserGames(currentUser.getId(), offset, limit);
  }

  private Player getCurrentProfile() {
    HttpSession session = requestProvider.get().getSession();
    if (session != null) {
      final Player bySessionId = playerDaoProvider.get().findBySessionId(session.getId());
      System.out.println(bySessionId);
      return bySessionId;
    }
    return null;
  }

  @Override
  public Long getGamesCount() {
    return gameDaoProvider.get().countAll();
  }

  @Override
  public Game saveOrCreate(Game game) {
    if (!AuthUtils.isAuthenticated(requestProvider.get().getSession())) {
      throw new RuntimeException("Unauthorized");
    }
    Player playerWhite = game.getPlayerWhite();
    if (playerWhite == null) {
      return null;
    }
    playerWhite = playerDaoProvider.get().find(playerWhite.getId());
    if (playerWhite == null) {
      return null;
    }
    Player playerBlack = game.getPlayerBlack();
    if (playerBlack == null) {
      return null;
    }
    playerBlack = playerDaoProvider.get().find(playerBlack.getId());
    if (playerBlack == null) {
      return null;
    }

    game.setPlayerWhite(playerWhite);
    game.setPlayerBlack(playerBlack);
    if (game.getId() == null) {
      gameDaoProvider.get().create(game);
    } else {
      gameDaoProvider.get().edit(game);
    }
    return game;
  }

  @Override
  public GameResource game(Long gameId) {
    return resourcesFactory.createGameResource(gameId);
  }
}
