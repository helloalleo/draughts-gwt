


package online.shashki.rus.client.application.home;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;
import online.shashki.rus.client.application.ApplicationPresenter;


public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy> implements HomeUiHandlers {
  interface MyView extends View, HasUiHandlers<HomeUiHandlers> {
  }


  @ProxyStandard
  interface MyProxy extends Proxy<HomePresenter> {
  }

  public static final NestedSlot SLOT_HOME = new NestedSlot();

  @Inject
  HomePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);

    getView().setUiHandlers(this);
  }
}
