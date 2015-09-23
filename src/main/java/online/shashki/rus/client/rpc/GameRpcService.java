package online.shashki.rus.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import online.shashki.rus.shared.model.Game;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 17:13
 */
@RemoteServiceRelativePath("GameRpcService")
public interface GameRpcService extends RemoteService {

  Game createGame(Game game);

  Game getGame(Long id);

  void saveGame(Game game);

  List<Game> findGames(int start, int length);

  List<Game> findAllGames();

  /**
   * Utility/Convenience class.
   * Use GameRpcService.App.fromString() to access static instance of GameRpcServiceAsync
   */
  class App {
    private static final GameRpcServiceAsync ourInstance = (GameRpcServiceAsync) GWT.create(GameRpcService.class);

    public static GameRpcServiceAsync getInstance() {
      return ourInstance;
    }
  }
}
