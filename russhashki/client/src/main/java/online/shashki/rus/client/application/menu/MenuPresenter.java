package online.shashki.rus.client.application.menu;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.NavigationEvent;
import com.gwtplatform.mvp.client.proxy.NavigationHandler;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.shashki.rus.client.service.PlayerServiceAsync;
import online.shashki.rus.client.utils.SHCookies;

public class MenuPresenter extends PresenterWidget<MenuPresenter.MyView> implements MenuUiHandlers, NavigationHandler {
  private final PlaceManager placeManager;
  private final PlayerServiceAsync profileService;

  @Inject
  MenuPresenter(EventBus eventBus,
                MyView view,
                PlaceManager placeManager,
                PlayerServiceAsync profileService) {
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
  protected void onBind() {
    super.onBind();
  }

  @Override
  public void onNavigation(NavigationEvent navigationEvent) {
//    String nameToken = navigationEvent.getRequest().getNameToken();
  }

  @Override
  public void isAuthenticated(AsyncCallback<Boolean> async) {
    profileService.isAuthenticated(async);
  }

  interface MyView extends View, HasUiHandlers<MenuUiHandlers> {
  }
}
