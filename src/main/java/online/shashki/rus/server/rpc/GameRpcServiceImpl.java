package online.shashki.rus.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import online.shashki.rus.client.service.GameRpcService;
import online.shashki.rus.server.service.GameService;
import online.shashki.rus.server.service.ShashistService;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.Shashist;

import javax.inject.Inject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 17:13
 */
public class GameRpcServiceImpl extends RemoteServiceServlet implements GameRpcService {

  @Inject
  private GameService gameService;

  @Inject
  private ShashistService shashistService;

  @Override
  public Game createGame(Game game) {
    if (!AuthUtils.isAuthenticated(getThreadLocalRequest().getSession())) {
      throw new RuntimeException("Unauthorized");
    }
    Shashist playerWhite = game.getPlayerWhite();
    if (playerWhite == null) {
      return null;
    }
    playerWhite = shashistService.find(playerWhite.getId());
    if (playerWhite == null) {
      return null;
    }
    Shashist playerBlack = game.getPlayerBlack();
    if (playerBlack == null) {
      return null;
    }
    playerBlack = shashistService.find(playerBlack.getId());
    if (playerBlack == null) {
      return null;
    }

    game.setPlayerWhite(playerWhite);
    game.setPlayerBlack(playerBlack);
    gameService.create(game);
    return game;
  }

  @Override
  public Game getGame(Long id) {
    if (!AuthUtils.isAuthenticated(getThreadLocalRequest().getSession())) {
      throw new RuntimeException("Unauthorized");
    }
    return gameService.findLazyFalse(id);
  }

  @Override
  public void saveGame(Game game) {
    if (!AuthUtils.isAuthenticated(getThreadLocalRequest().getSession())) {
      throw new RuntimeException("Unauthorized");
    }

    Shashist playerWhite = shashistService.find(game.getPlayerWhite().getId());
    if (playerWhite == null) {
      return;
    }
    Shashist playerBlack = shashistService.find(game.getPlayerBlack().getId());
    if (playerBlack == null) {
      return;
    }
    game.setPlayerWhite(playerWhite);
    game.setPlayerBlack(playerBlack);
    gameService.edit(game);
  }

  @Override
  public List<Game> findGames(int start, int length) {
    return gameService.findRange(start, length);
  }

  @Override
  public List<Game> findAllGames() {
    return gameService.findAll();
  }
}