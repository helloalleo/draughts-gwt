package online.draughts.rus.client.application.mygame;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import online.draughts.rus.client.application.ApplicationPresenter;
import online.draughts.rus.client.place.NameTokens;


public class MyGamePresenter extends Presenter<MyGamePresenter.MyView, MyGamePresenter.MyProxy> implements MyGameUiHandlers {
  public static final NestedSlot SLOT_MYGAME = new NestedSlot();

  @Inject
  MyGamePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);
  }

  interface MyView extends View, HasUiHandlers<MyGameUiHandlers> {
  }

  @NameToken(NameTokens.myGamePage)
  @ProxyCodeSplit
  interface MyProxy extends ProxyPlace<MyGamePresenter> {
  }

}
