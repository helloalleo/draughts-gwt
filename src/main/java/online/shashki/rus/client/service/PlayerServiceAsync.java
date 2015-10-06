package online.shashki.rus.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import online.shashki.rus.shared.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 20:03
 */
public interface PlayerServiceAsync {

  void isAuthenticated(AsyncCallback<Boolean> async);

  void isCookieValid(String cookie, AsyncCallback<Boolean> async);

  void find(Long shashistId, AsyncCallback<Player> async);

  void save(Player profile, AsyncCallback<Player> async);

  void getCurrentProfile(AsyncCallback<Player> async);
}
