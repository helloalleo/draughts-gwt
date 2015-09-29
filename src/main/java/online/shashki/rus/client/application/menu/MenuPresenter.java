package online.shashki.rus.client.application.menu;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.shashki.rus.client.rpc.ProfileRpcServiceAsync;
import online.shashki.rus.client.utils.SHCookies;

public class MenuPresenter extends PresenterWidget<MenuPresenter.MyView> implements MenuUiHandlers {
  private final PlaceManager placeManager;
  private final ProfileRpcServiceAsync profileService;

  @Inject
  MenuPresenter(EventBus eventBus,
                MyView view,
                PlaceManager placeManager,
                ProfileRpcServiceAsync profileService) {
    super(eventBus, view);
    getView().setUiHandlers(this);

    this.placeManager = placeManager;
    this.profileService = profileService;
  }

  @Override
  public void displayPage(String token) {
    PlaceRequest placeRequest = new PlaceRequest.Builder()
        .nameToken(token)
        .build();

    SHCookies.setLocation(token);
    placeManager.revealPlace(placeRequest);
  }

  @Override
  public void isAuthenticated(AsyncCallback<Boolean> callback) {
    profileService.isAuthenticated(callback);
  }

  interface MyView extends View, HasUiHandlers<MenuUiHandlers> {
  }
}
