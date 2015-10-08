package online.shashki.rus.client.application.home;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
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
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.client.utils.SHCookies;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.service.GameService;
import online.shashki.rus.shared.service.GameServiceAsync;
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
  private GameServiceAsync gameService;

  @Inject
  HomePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      CurrentSession currentSession,
      PlayComponentPresenter playPresenter) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);

    this.currentSession = currentSession;
    this.profileService = PlayerService.App.getInstance();
    this.gameService = GameService.App.getInstance();
    this.playPresenter = playPresenter;
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

    gameService.findGames(0, INIT_SHOW_GAMES_PAGE_SIZE, new AsyncCallback<List<Game>>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
        getProxy().manualRevealFailed();
      }

      @Override
      public void onSuccess(List<Game> result) {
        getView().setGames(result);
        getProxy().manualReveal(HomePresenter.this);
      }
    });
  }

  @Override
  public void getMoreGames(boolean myGames, int newPageSize) {
    if (myGames) {
      gameService.findUserGames(0, newPageSize, new AsyncCallback<List<Game>>() {
        @Override
        public void onFailure(Throwable caught) {
          ErrorDialogBox.setMessage(caught).show();
        }

        @Override
        public void onSuccess(List<Game> result) {
          getView().setGames(result);
        }
      });
    } else {
      gameService.findGames(0, newPageSize, new AsyncCallback<List<Game>>() {
        @Override
        public void onFailure(Throwable caught) {
          ErrorDialogBox.setMessage(caught).show();
        }

        @Override
        public void onSuccess(List<Game> result) {
          getView().setGames(result);
        }
      });
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
