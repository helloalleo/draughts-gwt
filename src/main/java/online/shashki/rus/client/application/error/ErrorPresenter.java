package online.shashki.rus.client.application.error;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import online.shashki.rus.client.application.ApplicationPresenter;
import online.shashki.rus.client.place.NameTokens;


public class ErrorPresenter extends Presenter<ErrorPresenter.MyView, ErrorPresenter.MyProxy> {
  public static final NestedSlot SLOT_ERROR = new NestedSlot();


  @Inject
  ErrorPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

  }

  interface MyView extends View {
  }

  @ProxyStandard
  @NameToken(NameTokens.errorPage)
  interface MyProxy extends ProxyPlace<ErrorPresenter> {
  }
}
