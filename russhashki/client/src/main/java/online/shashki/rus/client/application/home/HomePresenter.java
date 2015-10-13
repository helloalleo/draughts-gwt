package online.shashki.rus.client.application.home;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.Title;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.shashki.rus.client.application.ApplicationPresenter;
import online.shashki.rus.client.application.component.play.PlayComponentPresenter;
import online.shashki.rus.client.application.security.CurrentSession;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.client.util.AbstractAsyncCallback;
import online.shashki.rus.client.util.SHCookies;
import online.shashki.rus.shared.api.GamesResource;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.service.PlayerService;
import online.shashki.rus.shared.service.PlayerServiceAsync;

import java.util.List;

public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy>
    implements HomeUiHandlers {

  public static final PermanentSlot<PlayComponentPresenter> SLOT_PLAY = new PermanentSlot<>();
  public static final int INIT_SHOW_GAMES_PAGE_SIZE = 50;
  private final CurrentSession currentSession;
  private final PlayerServiceAsync profileService;
  private PlayComponentPresenter playPresenter;
  private final ResourceDelegate<GamesResource> gamesDelegate;

  @Inject
  HomePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      CurrentSession currentSession,
      PlayComponentPresenter playPresenter,
      ResourceDelegate<GamesResource> gamesDelegate) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);

    this.currentSession = currentSession;
    this.profileService = PlayerService.App.getInstance();
    this.playPresenter = playPresenter;
    this.gamesDelegate = gamesDelegate;
    SHCookies.setLocation(NameTokens.homePage);
  }

  @Override
  protected void onBind() {
    super.onBind();

    setInSlot(SLOT_PLAY, playPresenter);
    getView().setShowLoggedInControls(currentSession.isLoggedIn());
  }

  @Override
  public void prepareFromRequest(PlaceRequest request) {
    super.prepareFromRequest(request);

    gamesDelegate.withCallback(new AbstractAsyncCallback<List<Game>>() {
      @Override
      public void onFailure(Throwable caught) {
        super.onFailure(caught);
        getProxy().manualRevealFailed();
      }

      @Override
      public void onSuccess(List<Game> result) {
        getView().setGames(result);
        getProxy().manualReveal(HomePresenter.this);
      }
    }).getGames(0, INIT_SHOW_GAMES_PAGE_SIZE);
  }

  @Override
  public void getMoreGames(boolean myGames, int newPageSize) {
    if (myGames) {
      gamesDelegate.withCallback(new AbstractAsyncCallback<List<Game>>() {
        @Override
        public void onSuccess(List<Game> result) {
          getView().setGames(result);
        }
      }).getLoggedInUserGames(0, newPageSize);
    } else {
      gamesDelegate.withCallback(new AbstractAsyncCallback<List<Game>>() {
        @Override
        public void onSuccess(List<Game> result) {
          getView().setGames(result);
        }
      }).getGames(0, newPageSize);
    }
  }

  /**
   * {@link HomePresenter}'s proxy.
   */
  @Title("Главная")
  @ProxyCodeSplit
  @NameToken(NameTokens.homePage)
  @NoGatekeeper
  public interface MyProxy extends ProxyPlace<HomePresenter> {
  }

  @Override
  public boolean useManualReveal() {
    return true;
  }

  /**
   * {@link HomePresenter}'s view.
   */
  public interface MyView extends View, HasUiHandlers<HomeUiHandlers> {
    void setShowLoggedInControls(Boolean loggedIn);

    void setGames(List<Game> gameList);
  }
}
