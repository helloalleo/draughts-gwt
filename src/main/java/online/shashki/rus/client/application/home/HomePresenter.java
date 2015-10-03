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
import online.shashki.rus.client.service.GameRpcServiceAsync;
import online.shashki.rus.client.utils.SHCookies;
import online.shashki.rus.shared.model.Game;

import java.util.List;

public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy>
    implements HomeUiHandlers {

  public static final PermanentSlot<PlayComponentPresenter> SLOT_PLAY = new PermanentSlot<>();
  private static final int INIT_SHOW_GAMES = 50;
  private PlayComponentPresenter playPresenter;
  private GameRpcServiceAsync gameService;

  @Inject
  HomePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      CurrentSession currentSession,
      GameRpcServiceAsync gameService,
      PlayComponentPresenter playPresenter) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);

    this.gameService = gameService;
    this.playPresenter = playPresenter;
    getView().setShowLoggedInControls(currentSession.isLoggedIn());
    SHCookies.setLocation(NameTokens.homePage);
  }

  @Override
  protected void onBind() {
    super.onBind();

    setInSlot(SLOT_PLAY, playPresenter);
  }

  @Override
  public void prepareFromRequest(PlaceRequest request) {
    super.prepareFromRequest(request);

    gameService.findGames(0, INIT_SHOW_GAMES, new AsyncCallback<List<Game>>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
        getProxy().manualRevealFailed();
      }

      @Override
      public void onSuccess(List<Game> result) {
        getProxy().manualReveal(HomePresenter.this);
        getView().setGames(result);
      }
    });
  }

  @Override
  public void getMoreGames(int newPageSize) {
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
