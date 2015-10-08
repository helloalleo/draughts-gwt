package online.shashki.rus.shared.service;

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
public interface GameService extends RemoteService {

  Game save(Game game);

  Game find(Long id);

  List<Game> findGames(int start, int length);

  List<Game> findAllGames();

  List<Game> findUserGames(int start, int length);

  /**
   * Utility/Convenience class.
   * Use GameRpcService.App.fromString() to access static instance of GameRpcServiceAsync
   */
  class App {
    private static final GameServiceAsync ourInstance = (GameServiceAsync) GWT.create(GameService.class);

    public static GameServiceAsync getInstance() {
      return ourInstance;
    }
  }
}
