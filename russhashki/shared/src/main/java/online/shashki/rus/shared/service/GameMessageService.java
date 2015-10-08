package online.shashki.rus.shared.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import online.shashki.rus.shared.model.GameMessage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 30.12.14
 * Time: 9:31
 */
@RemoteServiceRelativePath("GameMessageRpcService")
public interface GameMessageService extends RemoteService {

  List<GameMessage> getLastPlayerMessages(int countLast, Long playerId, Long opponentId);

  /**
   * Utility/Convenience class.
   * Use GameMessageService.App.fromString() to access static instance of GameMessageServiceAsync
   */
  class App {
    private static final GameMessageServiceAsync ourInstance = (GameMessageServiceAsync) GWT.create(GameMessageService.class);

    public static GameMessageServiceAsync getInstance() {
      return ourInstance;
    }
  }
}
