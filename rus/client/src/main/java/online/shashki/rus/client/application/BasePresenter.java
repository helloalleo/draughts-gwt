package online.shashki.rus.client.application;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.shashki.rus.client.application.security.CurrentSession;
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;
import online.shashki.rus.client.service.PlayerServiceAsync;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 02.10.15
 * Time: 21:43
 */
public abstract class BasePresenter<View_ extends View, Proxy_ extends Proxy<?>> extends
    Presenter<View_, Proxy_> {

//  private final PlaceManager placeManager;
  @Inject
  private CurrentSession currentSession;
  @Inject
  private PlayerServiceAsync profileService;

  protected BasePresenter(
      EventBus eventBus,
      View_ view,
      Proxy_ proxy,
      GwtEvent.Type<RevealContentHandler<?>> slot) {
//      PlaceManager placeManager,
//      CurrentSession currentSession,
//      ProfileRpcServiceAsync profileService) {
    super(eventBus, view, proxy, slot);

//    this.placeManager = placeManager;
//    this.currentSession = currentSession;
//    this.profileService = profileService;
  }

  @Override
  public void prepareFromRequest(PlaceRequest request) {
    super.prepareFromRequest(request);

    profileService.isAuthenticated(new AsyncCallback<Boolean>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
      }

      @Override
      public void onSuccess(Boolean result) {
        currentSession.setLoggedIn(result);
//        // необходимо чтобы вернуть отображение страницы на отоброжение, соответствующее адресной строке
//        // оно изменяется потому, что на время проверки аутентификации GWTP этот код еще не загрузился и
//        // Gateway получает false в isLoggedIn()
//        String hash = Window.Location.getHash();
//        if (hash == null || hash.isEmpty()) {
//          return;
//        }
//        String token = hash.substring(1, hash.length());
//        PlaceRequest placeRequest = new PlaceRequest.Builder()
//            .nameToken(token)
//            .build();
//        placeManager.revealPlace(placeRequest);
      }
    });
  }

  @Override
  public boolean useManualReveal() {
    return true;
  }

  public CurrentSession getCurrentSession() {
    return currentSession;
  }

  public BasePresenter setCurrentSession(CurrentSession currentSession) {
    this.currentSession = currentSession;
    return this;
  }

  public PlayerServiceAsync getProfileService() {
    return profileService;
  }

  public BasePresenter setProfileService(PlayerServiceAsync profileService) {
    this.profileService = profileService;
    return this;
  }
}