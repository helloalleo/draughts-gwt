package online.draughts.rus.client.application.play;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import online.draughts.rus.client.application.ApplicationPresenter;
import online.draughts.rus.client.application.common.PlayComponentPresenter;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.Cookies;


public class PlayPresenter extends Presenter<PlayPresenter.MyView, PlayPresenter.MyProxy>
    implements PlayUiHandlers {
  public static final PermanentSlot<PlayComponentPresenter> SLOT_NEWPLAY = new PermanentSlot<>();

  @Inject
  PlayPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      Cookies cookies,
      PlayComponentPresenter playComponentPresenter) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);

    cookies.setLocation(NameTokens.PLAY_PAGE);

    setInSlot(SLOT_NEWPLAY, playComponentPresenter);
  }

  interface MyView extends View, HasUiHandlers<PlayUiHandlers> {
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.PLAY_PAGE)
  interface MyProxy extends ProxyPlace<PlayPresenter> {
  }
}
