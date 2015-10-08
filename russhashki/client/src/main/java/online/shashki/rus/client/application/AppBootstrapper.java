package online.shashki.rus.client.application;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import online.shashki.rus.client.application.security.CurrentSession;
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;
import online.shashki.rus.client.service.PlayerService;
import online.shashki.rus.client.service.PlayerServiceAsync;
import online.shashki.rus.client.utils.SHLog;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.10.15
 * Time: 8:19
 */
public class AppBootstrapper implements Bootstrapper {

  private final PlayerServiceAsync profileService;
  private final CurrentSession currentSession;
  private final PlaceManager placeManager;

  @Inject
  public AppBootstrapper(PlaceManager placeManager,
                         CurrentSession currentSession) {
    this.placeManager = placeManager;
    this.profileService = PlayerService.App.getInstance();
    this.currentSession = currentSession;
    SHLog.debug("Bootstrap constructor");
  }

  @Override
  public void onBootstrap() {
    profileService.isAuthenticated(new AsyncCallback<Boolean>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
      }

      @Override
      public void onSuccess(Boolean result) {
        currentSession.setLoggedIn(result);
        placeManager.revealCurrentPlace();
        SHLog.debug("Is logged in? " + result);
      }
    });
    SHLog.info("Bootstrapped");
  }
}
