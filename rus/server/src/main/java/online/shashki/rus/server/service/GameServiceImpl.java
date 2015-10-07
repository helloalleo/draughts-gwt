package online.shashki.rus.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.shashki.rus.client.service.GameRpcService;
import online.shashki.rus.server.dao.GameDao;
import online.shashki.rus.server.dao.PlayerDao;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.Player;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 17:13
 */
@Singleton
public class GameServiceImpl extends RemoteServiceServlet implements GameRpcService {

  private final GameDao gameDao;
  private final PlayerDao playerDao;
  private final PlayerServiceImpl profileService;
  private final HttpServletRequest httpServletRequest;

  @Inject
  public GameServiceImpl(Provider<GameDao> gameDaoProvider,
                         Provider<PlayerDao> playerDaoProvider,
                         PlayerServiceImpl profileService,
                         Provider<HttpServletRequest> httpServletRequestProvider) {
    this.gameDao = gameDaoProvider.get();
    this.playerDao = playerDaoProvider.get();
    this.profileService = profileService;
    this.httpServletRequest = httpServletRequestProvider.get();
  }

  @Override
  public Game save(Game game) {
    if (!AuthUtils.isAuthenticated(getThreadLocalRequest().getSession())) {
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
  public Game find(Long id) {
    if (!AuthUtils.isAuthenticated(httpServletRequest.getSession())) {
      throw new RuntimeException("Unauthorized");
    }
    return gameDao.findLazyFalse(id);
  }

  @Override
  public List<Game> findGames(int start, int length) {
    return gameDao.findRange(start, length);
  }

  @Override
  public List<Game> findAllGames() {
    return gameDao.findAll();
  }

  @Override
  public List<Game> findUserGames(int start, int length) {
    Player currentUser = profileService.getCurrentProfile();
    System.out.println(currentUser);
    if (currentUser == null) {
      return new ArrayList<>();
    }
    return gameDao.findUserGames(currentUser.getId(), start, length);
  }
}