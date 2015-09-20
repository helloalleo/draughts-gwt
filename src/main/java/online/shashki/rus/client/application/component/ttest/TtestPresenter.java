package online.shashki.rus.client.application.component.ttest;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;
import online.shashki.rus.client.application.home.HomePresenter;


public class TtestPresenter extends Presenter<TtestPresenter.MyView, TtestPresenter.MyProxy> implements TtestUiHandlers {

  public static final NestedSlot SLOT_PLAY = new NestedSlot();

  @Inject
  TtestPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy) {
    super(eventBus, view, proxy, HomePresenter.SLOT_HOME);

    getView().setUiHandlers(this);
  }


  interface MyView extends View, HasUiHandlers<TtestUiHandlers> {
  }

  @ProxyCodeSplit
  interface MyProxy extends Proxy<TtestPresenter> {
  }
}
