package online.shashki.ru.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import online.shashki.ru.shared.model.Shashist;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 20:03
 */
@RemoteServiceRelativePath("ProfileRpcService")
public interface ProfileRpcService extends RemoteService {

  Boolean isAuthenticated();

  Shashist getProfile(Long shashistId);

  Shashist getAuthProfile();

  void saveProfile(Shashist profile);

  /**
   * Utility/Convenience class.
   * Use ProfileRpcService.App.fromString() to access static instance of ProfileRpcServiceAsync
   */
  class App {
    private static final ProfileRpcServiceAsync ourInstance = (ProfileRpcServiceAsync) GWT.create(ProfileRpcService.class);

    public static ProfileRpcServiceAsync getInstance() {
      return ourInstance;
    }
  }
}
