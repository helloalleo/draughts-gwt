package online.shashki.rus.client.application.home;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import online.shashki.rus.client.application.ApplicationPresenter;
import online.shashki.rus.client.application.component.play.PlayComponentPresenter;
import online.shashki.rus.client.application.component.playshowpanel.PlayShowPanelPresenter;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.client.utils.SHCookies;

public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy>
    implements HomeUiHandlers {

  public static final PermanentSlot<PlayComponentPresenter> SLOT_PLAY = new PermanentSlot<>();
  //  public static final PermanentSlot<PlayShowPanelPresenter> SLOT_PLAY_SHOW_PANEL
//      = new PermanentSlot<>();
  private PlayComponentPresenter playPresenter;
  private PlayShowPanelPresenter playRowPresenter;

  @Inject
  HomePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
//      GameRpcServiceAsync gameService,
      PlayComponentPresenter playPresenterFactory
      ) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);

    this.playPresenter = playPresenterFactory;
    SHCookies.setLocation(NameTokens.homePage);

//    PlayShowPanelPresenter playShowPanelPresenter = injectionFactory.createPlayShowPanelPresenter("HI!");

//    gameService.findGames(0, 5, new AsyncCallback<List<Game>>() {
//      @Override
//      public void onFailure(Throwable caught) {
//        ErrorDialogBox.setMessage(caught).show();
//      }
//
//      @Override
//      public void onSuccess(List<Game> result) {
////        HomePresenter.this.playRowPresenter = injectionFactory.createPlayShowPanelPresenter(result);
//      }
//    });
  }

  @Override
  protected void onBind() {
    super.onBind();

    setInSlot(SLOT_PLAY, playPresenter);
//    setInSlot(SLOT_PLAY_SHOW_PANEL, playRowPresenter);
  }

  /**
   * {@link HomePresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.homePage)
  @NoGatekeeper
  public interface MyProxy extends ProxyPlace<HomePresenter> {
  }

  /**
   * {@link HomePresenter}'s view.
   */
  public interface MyView extends View, HasUiHandlers<HomeUiHandlers> {
  }
}
