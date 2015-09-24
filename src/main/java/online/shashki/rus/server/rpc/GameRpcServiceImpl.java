package online.shashki.rus.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import online.shashki.rus.client.rpc.GameRpcService;
import online.shashki.rus.server.service.GameService;
import online.shashki.rus.server.service.ShashistService;
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
//    Shashist playerWhite = shashistService.find(game.getPlayerWhite().getId());
//    if (playerWhite == null) {
//      return null;
//    }
//    Shashist playerBlack = shashistService.find(game.getPlayerBlack().getId());
//    if (playerBlack == null) {
//      return null;
//    }
//
//    gameEntity.setPlayerWhite(playerWhite);
//    gameEntity.setPlayerBlack(playerBlack);
    gameService.create(game);
    return game;
  }

  @Override
  public Game getGame(Long id) {
    return gameService.findLazyFalse(id);
  }

  @Override
  public void saveGame(Game game) {
    Game gameEntity = gameService.find(game.getId());
    if (gameEntity != null) {
      Shashist playerWhite = shashistService.find(game.getPlayerWhite().getId());
      if (playerWhite == null) {
        return;
      }
      Shashist playerBlack = shashistService.find(game.getPlayerBlack().getId());
      if (playerBlack == null) {
        return;
      }
      gameEntity.setPlayerWhite(playerWhite);
      gameEntity.setPlayerBlack(playerBlack);
      gameService.edit(gameEntity);
    }
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