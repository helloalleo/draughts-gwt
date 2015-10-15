package online.shashki.rus.client.application.menu;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.NavigationEvent;
import com.gwtplatform.mvp.client.proxy.NavigationHandler;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.shashki.rus.client.application.security.CurrentSession;
import online.shashki.rus.client.util.SHCookies;

public class MenuPresenter extends PresenterWidget<MenuPresenter.MyView> implements MenuUiHandlers, NavigationHandler {
  private final PlaceManager placeManager;
  private final CurrentSession currentSession;

  @Inject
  MenuPresenter(EventBus eventBus,
                MyView view,
                PlaceManager placeManager,
                CurrentSession currentSession) {
    super(eventBus, view);
    getView().setUiHandlers(this);

    this.placeManager = placeManager;
    this.currentSession = currentSession;
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
  public boolean isLoggedIn() {
    return currentSession.isLoggedIn();
  }

  interface MyView extends View, HasUiHandlers<MenuUiHandlers> {
  }
}
