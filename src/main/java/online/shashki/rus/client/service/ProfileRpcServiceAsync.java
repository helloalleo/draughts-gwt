package online.shashki.rus.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import online.shashki.rus.shared.model.Shashist;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 20:03
 */
public interface ProfileRpcServiceAsync {

  void isAuthenticated(AsyncCallback<Boolean> async);

  void isCookieValid(String cookie, AsyncCallback<Boolean> async);

  void find(Long shashistId, AsyncCallback<Shashist> async);

  void save(Shashist profile, AsyncCallback<Shashist> async);

  void getCurrentProfile(AsyncCallback<Shashist> async);
}
