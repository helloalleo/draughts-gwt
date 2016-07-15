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
import online.draughts.rus.client.gin.DialogFactory;
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
  private final CurrentSession currentSession;
  private final ResourceDelegate<GamesResource> gamesDelegate;
  private final ResourceDelegate<PlayersResource> playersDelegate;
  private final ClientConfiguration config;
  private int gamesOffset = 0;
  private final DialogFactory dialogFactory;
  private final Cookies cookies;

  @Inject
  HomePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      CurrentSession currentSession,
      ClientConfiguration config,
      Cookies cookies,
      ResourceDelegate<GamesResource> gamesDelegate,
      ResourceDelegate<PlayersResource> playersDelegate,
      DialogFactory dialogFactory) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);
    this.dialogFactory = dialogFactory;

    getView().setUiHandlers(this);

    this.currentSession = currentSession;
    this.gamesDelegate = gamesDelegate;
    this.playersDelegate = playersDelegate;
    this.config = config;
    this.cookies = cookies;
    bindEvent();
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
    playersDelegate.withCallback(new AbstractAsyncCallback<Integer>(dialogFactory) {
      @Override
      public void onSuccess(Integer result) {
        getView().updateTotalPlayersCounter(result);
      }
    }).totalPlayers();
    playersDelegate.withCallback(new AbstractAsyncCallback<Integer>(dialogFactory) {
      @Override
      public void onSuccess(Integer result) {
        getView().updateOnlinePlayerCounter(result);
      }
    }).onlinePlayers();
    cookies.setLocation(NameTokens.HOME_PAGE);
  }

  @Override
  public void prepareFromRequest(PlaceRequest request) {
    updatePlayShowPanel();
  }

  public void updatePlayShowPanel() {
//    if (myGames) {
//      gamesDelegate.withCallback(ManualRevealCallback.create(this, new AbstractAsyncCallback<List<GameDto>>(dialogFactory) {
//        @Override
//        public void onFailure(Throwable caught) {
//          super.onFailure(caught);
//        }
//
//        @Override
//        public void onSuccess(List<GameDto> result) {
//          if (null != result) {
//            gamesOffset = getView().setGames(result);
//          }
//        }
//      })).getLoggedInUserGames(0, INIT_SHOW_GAMES_PAGE_SIZE);
//    } else {
      gamesDelegate.withCallback(ManualRevealCallback.create(this, new AbstractAsyncCallback<List<GameDto>>(dialogFactory) {
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
      })).getGames(0, Integer.valueOf(config.initShowGamesPageSize()));
//    }
  }

  @Override
  public void getMoreGames(int newPageSize) {
//    if (myGames) {
//      gamesDelegate.withCallback(new AbstractAsyncCallback<List<GameDto>>(dialogFactory) {
//        @Override
//        public void onSuccess(List<GameDto> result) {
//          gamesOffset = getView().addGames(result);
//        }
//      }).getLoggedInUserGames(gamesOffset, newPageSize);
//    } else {
      gamesDelegate.withCallback(new AbstractAsyncCallback<List<GameDto>>(dialogFactory) {
        @Override
        public void onSuccess(List<GameDto> result) {
          gamesOffset = getView().addGames(result);
        }
      }).getGames(gamesOffset, newPageSize);
//    }
  }

  @Override
  public PlayerDto getPlayer() {
    return null;// currentSession.getPlayer();
  }

  /**
   * {@link HomePresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.HOME_PAGE)
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
