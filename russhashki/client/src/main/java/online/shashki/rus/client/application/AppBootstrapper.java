package online.shashki.rus.client.application;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import online.shashki.rus.client.application.security.CurrentSession;
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;
import online.shashki.rus.client.utils.SHLog;
import online.shashki.rus.shared.dispatch.FetchCurrentPlayerAction;
import online.shashki.rus.shared.dispatch.FetchCurrentPlayerResult;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.10.15
 * Time: 8:19
 */
public class AppBootstrapper implements Bootstrapper {

  private final CurrentSession currentSession;
  private final PlaceManager placeManager;
  private final DispatchAsync dispatch;

  @Inject
  public AppBootstrapper(PlaceManager placeManager,
                         CurrentSession currentSession,
                         DispatchAsync dispatch) {
    this.placeManager = placeManager;
    this.currentSession = currentSession;
    this.dispatch = dispatch;
    SHLog.debug("Bootstrap constructor");
  }

  @Override
  public void onBootstrap() {
    dispatch.execute(new FetchCurrentPlayerAction(), new AsyncCallback<FetchCurrentPlayerResult>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
      }

      @Override
      public void onSuccess(FetchCurrentPlayerResult result) {
        currentSession.setPlayer(result.getPlayer());
        placeManager.revealCurrentPlace();
        SHLog.debug("Is logged in? " + result);
      }
    });

//    profileService.isAuthenticated(new AsyncCallback<Boolean>() {
//      @Override
//      public void onFailure(Throwable caught) {
//        ErrorDialogBox.setMessage(caught).show();
//      }
//
//      @Override
//      public void onSuccess(Boolean result) {
//      }
//    });
    SHLog.info("Bootstrapped");
  }
}
