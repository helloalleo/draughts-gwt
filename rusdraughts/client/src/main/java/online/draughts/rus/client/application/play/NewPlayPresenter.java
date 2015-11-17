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
import online.draughts.rus.client.place.NameTokens;


public class NewPlayPresenter extends Presenter<NewPlayPresenter.MyView, NewPlayPresenter.MyProxy>
    implements NewPlayUiHandlers {
  public static final PermanentSlot<PlayComponentPresenter> SLOT_NEWPLAY = new PermanentSlot<>();

  @Inject
  NewPlayPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      PlayComponentPresenter playComponentPresenter) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);
    setInSlot(SLOT_NEWPLAY, playComponentPresenter);
  }

  interface MyView extends View, HasUiHandlers<NewPlayUiHandlers> {
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.playPage)
  interface MyProxy extends ProxyPlace<NewPlayPresenter> {
  }
}
