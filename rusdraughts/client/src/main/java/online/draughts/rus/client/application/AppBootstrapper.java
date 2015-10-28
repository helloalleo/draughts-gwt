package online.draughts.rus.client.application;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.application.widget.dialog.ErrorDialogBox;
import online.draughts.rus.client.util.DTLog;
import online.draughts.rus.shared.dispatch.FetchCurrentPlayerAction;
import online.draughts.rus.shared.dispatch.FetchCurrentPlayerResult;

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
    DTLog.debug("Bootstrap constructor");
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
        DTLog.debug("Is logged in? " + result.getPlayer());
      }
    });
    DTLog.info("Bootstrapped");
  }
}
