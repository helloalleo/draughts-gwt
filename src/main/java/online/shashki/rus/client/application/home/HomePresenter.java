package online.shashki.rus.client.application.home;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import online.shashki.rus.client.application.ApplicationPresenter;
import online.shashki.rus.client.application.component.play.PlayComponentPresenter;
import online.shashki.rus.client.application.login.CurrentSession;
import online.shashki.rus.client.place.NameTokens;

public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy>
    implements HomeUiHandlers {

  public static final PermanentSlot<PlayComponentPresenter> SLOT_PLAY = new PermanentSlot<>();
  private PlayComponentPresenter playPresenter;

  @Inject
  HomePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      PlayComponentPresenter playPresenterFactory, CurrentSession currentSession) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    this.playPresenter = playPresenterFactory;
//    currentSession.isAuthenticated();
  }

  @Override
  protected void onBind() {
    super.onBind();

    setInSlot(SLOT_PLAY, playPresenter);
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
  public interface MyView extends View {
  }
}
