package online.draughts.rus.client.application.menu;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.NavigationEvent;
import com.gwtplatform.mvp.client.proxy.NavigationHandler;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.util.Cookies;

public class MenuPresenter extends PresenterWidget<MenuPresenter.MyView> implements MenuUiHandlers, NavigationHandler {
  private final PlaceManager placeManager;
  private final CurrentSession currentSession;
  private final Cookies cookies;

  @Inject
  MenuPresenter(EventBus eventBus,
                MyView view,
                PlaceManager placeManager,
                Cookies cookies,
                CurrentSession currentSession) {
    super(eventBus, view);
    getView().setUiHandlers(this);

    this.cookies = cookies;
    this.placeManager = placeManager;
    this.currentSession = currentSession;
  }

  @Override
  public void displayPage(String token) {
    PlaceRequest placeRequest = new PlaceRequest.Builder()
        .nameToken(token)
        .build();

    cookies.setLocation(token);
    placeManager.revealPlace(placeRequest);
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
