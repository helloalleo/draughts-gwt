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
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.ManualRevealCallback;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.draughts.rus.client.application.ApplicationPresenter;
import online.draughts.rus.client.application.common.PlayComponentPresenter;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.event.ReceivedPlayerListEvent;
import online.draughts.rus.client.event.ReceivedPlayerListEventHandler;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.resource.GamesResource;
import online.draughts.rus.shared.resource.PlayersResource;

import java.util.List;

public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy>
    implements HomeUiHandlers {

  static final PermanentSlot<PlayComponentPresenter> SLOT_SHOW_PLAY_PANEL = new PermanentSlot<>();
  private static int INIT_SHOW_GAMES_PAGE_SIZE;
  private final CurrentSession currentSession;
  private final Cookies cookies;
  private final ResourceDelegate<GamesResource> gamesDelegate;
  private final ResourceDelegate<PlayersResource> playersDelegate;
  private int gamesOffset = 0;

  @Inject
  HomePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      CurrentSession currentSession,
      ClientConfiguration config,
      Cookies cookies,
      ResourceDelegate<GamesResource> gamesDelegate,
      ResourceDelegate<PlayersResource> playersDelegate) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    INIT_SHOW_GAMES_PAGE_SIZE = getIncrementPlaysOnPage(config, cookies);
    getView().setUiHandlers(this);

    this.currentSession = currentSession;
    this.gamesDelegate = gamesDelegate;
    this.playersDelegate = playersDelegate;
    this.cookies = cookies;
    cookies.setLocation(NameTokens.homePage);
    bindEvent();
  }

  static int getIncrementPlaysOnPage(ClientConfiguration config, Cookies cookies) {
    int gamesOnPageCounter = cookies.getGamesOnPageCounter();
    int gamesOnPage = PlayShowPanel.GAMES_ON_PAGE[PlayShowPanel.GAMES_ON_PAGE.length - gamesOnPageCounter - 1];
    return Integer.valueOf(config.initShowGamesPageSize()) / gamesOnPage;
  }

  private void bindEvent() {
    addRegisteredHandler(ReceivedPlayerListEvent.TYPE, new ReceivedPlayerListEventHandler() {
      @Override
      public void onReceivedPlayerList(ReceivedPlayerListEvent event) {
        if (event.getPlayerList() != null) {
          getView().updateOnlinePlayerCounter(event.getPlayerList().size());
        }
      }
    });
  }

  @Override
  protected void onReveal() {
    getView().setShowLoggedInControls(currentSession.isLoggedIn());
    playersDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
      @Override
      public void onSuccess(Integer result) {
        getView().updateTotalPlayersCounter(result);
      }
    }).totalPlayers();
    playersDelegate.withCallback(new AbstractAsyncCallback<Integer>() {
      @Override
      public void onSuccess(Integer result) {
        getView().updateOnlinePlayerCounter(result);
      }
    }).onlinePlayers();
  }

  @Override
  public void prepareFromRequest(PlaceRequest request) {
//    super.prepareFromRequest(request);

    updatePlayShowPanel(cookies.isMyGames());
  }

  public void updatePlayShowPanel(boolean myGames) {
    if (myGames) {
      gamesDelegate.withCallback(ManualRevealCallback.create(this, new AbstractAsyncCallback<List<GameDto>>() {
        @Override
        public void onFailure(Throwable caught) {
          super.onFailure(caught);
        }

        @Override
        public void onSuccess(List<GameDto> result) {
          if (null != result) {
            gamesOffset = getView().setGames(result);
          }
        }
      })).getLoggedInUserGames(0, INIT_SHOW_GAMES_PAGE_SIZE);
    } else {
      gamesDelegate.withCallback(ManualRevealCallback.create(this, new AbstractAsyncCallback<List<GameDto>>() {
        @Override
        public void onFailure(Throwable caught) {
          super.onFailure(caught);
          getProxy().manualRevealFailed();
        }

        @Override
        public void onSuccess(List<GameDto> result) {
          if (null != result) {
            gamesOffset = getView().setGames(result);
          }
          getProxy().manualReveal(HomePresenter.this);
        }
      })).getGames(0, INIT_SHOW_GAMES_PAGE_SIZE);
    }
  }

  @Override
  public void getMoreGames(boolean myGames, int newPageSize) {
    if (myGames) {
      gamesDelegate.withCallback(new AbstractAsyncCallback<List<GameDto>>() {
        @Override
        public void onSuccess(List<GameDto> result) {
          gamesOffset = getView().addGames(result);
        }
      }).getLoggedInUserGames(gamesOffset, newPageSize);
    } else {
      gamesDelegate.withCallback(new AbstractAsyncCallback<List<GameDto>>() {
        @Override
        public void onSuccess(List<GameDto> result) {
          gamesOffset = getView().addGames(result);
        }
      }).getGames(gamesOffset, newPageSize);
    }
  }

  @Override
  public PlayerDto getPlayer() {
    return null;// currentSession.getPlayer();
  }

  /**
   * {@link HomePresenter}'s proxy.
   */
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

    int setGames(List<GameDto> games);

    int addGames(List<GameDto> games);

    boolean isMyGames();

    void updateOnlinePlayerCounter(int size);

    void updateTotalPlayersCounter(Integer result);
  }
}
