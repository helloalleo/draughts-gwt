package online.shashki.rus.shared.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import online.shashki.rus.shared.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 20:03
 */
@RemoteServiceRelativePath("PlayerService")
public interface PlayerService extends RemoteService {

  Boolean isAuthenticated();

  Player find(Long playerId);

  Player getCurrentProfile();

  Player save(Player profile);

  boolean isCookieValid(String cookie);

  /**
   * Utility/Convenience class.
   * Use ProfileRpcService.App.fromString() to access static instance of ProfileRpcServiceAsync
   */
  class App {
    private static final PlayerServiceAsync ourInstance = (PlayerServiceAsync) GWT.create(PlayerService.class);

    public static PlayerServiceAsync getInstance() {
      return ourInstance;
    }
  }
}
