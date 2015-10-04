package online.shashki.rus.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import online.shashki.rus.client.service.GameRpcService;
import online.shashki.rus.server.dao.GameDao;
import online.shashki.rus.server.dao.ShashistDao;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.Shashist;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 17:13
 */
public class GameRpcServiceImpl extends RemoteServiceServlet implements GameRpcService {

  @Inject
  private GameDao gameDao;

  @Inject
  private ShashistDao shashistDao;

  @Inject
  private ProfileRpcServiceImpl profileService;

  @Override
  public Game save(Game game) {
    if (!AuthUtils.isAuthenticated(getThreadLocalRequest().getSession())) {
      throw new RuntimeException("Unauthorized");
    }
    Shashist playerWhite = game.getPlayerWhite();
    if (playerWhite == null) {
      return null;
    }
    playerWhite = shashistDao.find(playerWhite.getId());
    if (playerWhite == null) {
      return null;
    }
    Shashist playerBlack = game.getPlayerBlack();
    if (playerBlack == null) {
      return null;
    }
    playerBlack = shashistDao.find(playerBlack.getId());
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
    if (!AuthUtils.isAuthenticated(getThreadLocalRequest().getSession())) {
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
    Shashist currentUser = profileService.getCurrentProfile();
    System.out.println(currentUser);
    if (currentUser == null) {
      return new ArrayList<>();
    }
    return gameDao.findUserGames(currentUser.getId(), start, length);
  }
}