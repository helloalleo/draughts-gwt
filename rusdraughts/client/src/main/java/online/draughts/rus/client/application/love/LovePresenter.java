package online.draughts.rus.client.application.love;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import online.draughts.rus.client.application.ApplicationPresenter;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.Cookies;

public class LovePresenter extends Presenter<LovePresenter.MyView, LovePresenter.MyProxy> {
  public static final NestedSlot SLOT_LOVE = new NestedSlot();

  @Inject
  LovePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      Cookies cookies) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    cookies.setLocation(NameTokens.lovePage);
  }

  interface MyView extends View {
  }

  @NameToken(NameTokens.lovePage)
  @ProxyCodeSplit
  @NoGatekeeper
  interface MyProxy extends ProxyPlace<LovePresenter> {
  }
}
