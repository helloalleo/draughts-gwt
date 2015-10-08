package online.shashki.rus.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import online.shashki.rus.shared.model.Game;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 17:13
 */
public interface GameServiceAsync {

  void find(Long id, AsyncCallback<Game> async);

  void save(Game game, AsyncCallback<Game> async);

  void findGames(int start, int length, AsyncCallback<List<Game>> async);

  void findAllGames(AsyncCallback<List<Game>> async);

  void findUserGames(int start, int length, AsyncCallback<List<Game>> async);
}
