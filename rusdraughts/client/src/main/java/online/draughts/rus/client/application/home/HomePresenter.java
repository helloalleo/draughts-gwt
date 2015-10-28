package online.draughts.rus.client.application.home;

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
import online.draughts.rus.client.application.ApplicationPresenter;
import online.draughts.rus.client.application.component.play.PlayComponentPresenter;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.event.UpdatePlayShowPanelEvent;
import online.draughts.rus.client.event.UpdatePlayShowPanelEventHandler;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.client.util.DCookies;
import online.draughts.rus.shared.model.Game;
import online.draughts.rus.shared.model.Player;
import online.draughts.rus.shared.rest.GamesResource;
import online.draughts.rus.shared.rest.PlayersResource;

import java.util.List;

public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy>
    implements HomeUiHandlers {

  public static final PermanentSlot<PlayComponentPresenter> SLOT_PLAY = new PermanentSlot<>();
  public static final int INIT_SHOW_GAMES_PAGE_SIZE = 36;
  private final CurrentSession currentSession;
  private PlayComponentPresenter playPresenter;
  private final ResourceDelegate<GamesResource> gamesDelegate;

  @Inject
  HomePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      CurrentSession currentSession,
      PlayComponentPresenter playPresenter,
      ResourceDelegate<PlayersResource> playersDelegate,
      ResourceDelegate<GamesResource> gamesDelegate) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);

    this.currentSession = currentSession;
    this.playPresenter = playPresenter;
    this.gamesDelegate = gamesDelegate;
    DCookies.setLOCATION(NameTokens.homePage);
    bindEvent();
  }

  private void bindEvent() {
    addRegisteredHandler(UpdatePlayShowPanelEvent.TYPE, new UpdatePlayShowPanelEventHandler() {
      @Override
      public void onUpdatePlayShowPanel(UpdatePlayShowPanelEvent event) {
        updatePlayShowPanel();
      }
    });
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

    updatePlayShowPanel();
  }

  private void updatePlayShowPanel() {
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

  @Override
  public Player getPlayer() {
    return currentSession.getPlayer();
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
