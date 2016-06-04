package online.draughts.rus.client.application;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.gin.DialogFactory;
import online.draughts.rus.client.util.AbstractAsyncCallback;
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
  private final DialogFactory dialogFactory;

  @Inject
  public AppBootstrapper(PlaceManager placeManager,
                         CurrentSession currentSession,
                         DispatchAsync dispatch,
                         DialogFactory dialogFactory) {
    this.placeManager = placeManager;
    this.currentSession = currentSession;
    this.dispatch = dispatch;
    this.dialogFactory = dialogFactory;
  }

  @Override
  public void onBootstrap() {
    dispatch.execute(new FetchCurrentPlayerAction(), new AbstractAsyncCallback<FetchCurrentPlayerResult>(dialogFactory) {
      @Override
      public void onSuccess(FetchCurrentPlayerResult result) {
        currentSession.setPlayer(result.getPlayer());
        placeManager.revealCurrentPlace();
      }
    });
  }
}
