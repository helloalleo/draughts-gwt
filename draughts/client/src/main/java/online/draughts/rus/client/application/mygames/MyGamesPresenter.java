package online.draughts.rus.client.application.mygames;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.ManualRevealCallback;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.draughts.rus.client.application.ApplicationPresenter;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.gin.DialogFactory;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.resource.GamesResource;
import online.draughts.rus.shared.resource.PlayersResource;

import java.util.List;

public class MyGamesPresenter extends Presenter<MyGamesPresenter.MyView, MyGamesPresenter.MyProxy> implements MyGamesUiHandlers {
  public static final NestedSlot SLOT_MYGAME = new NestedSlot();
  private final CurrentSession currentSession;
  private final Cookies cookies;
  private final ResourceDelegate<GamesResource> gamesDelegate;
  private final ResourceDelegate<PlayersResource> playersDelegate;
  private final ClientConfiguration config;
  private int gamesOffset = 0;
  private final DialogFactory dialogFactory;

  @Inject
  MyGamesPresenter(
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

    getView().setUiHandlers(this);
    this.dialogFactory = dialogFactory;

    getView().setUiHandlers(this);

    this.currentSession = currentSession;
    this.gamesDelegate = gamesDelegate;
    this.playersDelegate = playersDelegate;
    this.cookies = cookies;
    this.config = config;
  }

  @Override
  protected void onReveal() {
    cookies.setLocation(NameTokens.MY_GAMES_PAGE);
  }

  @Override
  public void prepareFromRequest(PlaceRequest request) {
    updatePlayShowPanel();
  }


  public void updatePlayShowPanel() {
    gamesDelegate.withCallback(ManualRevealCallback.create(this, new AbstractAsyncCallback<List<GameDto>>(dialogFactory) {
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
    })).getLoggedInUserGames(0, Integer.valueOf(config.initShowGamesPageSize()));
  }

  @Override
  public void getMoreGames(boolean myGames, int newPageSize) {
    gamesDelegate.withCallback(new AbstractAsyncCallback<List<GameDto>>(dialogFactory) {
      @Override
      public void onSuccess(List<GameDto> result) {
        gamesOffset = getView().addGames(result);
      }
    }).getLoggedInUserGames(gamesOffset, newPageSize);
  }

  interface MyView extends View, HasUiHandlers<MyGamesUiHandlers> {
    int setGames(List<GameDto> gameList);

    int addGames(List<GameDto> games);
  }

  @NameToken(NameTokens.MY_GAMES_PAGE)
  @ProxyCodeSplit
  interface MyProxy extends ProxyPlace<MyGamesPresenter> {
  }
}
