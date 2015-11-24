package online.draughts.rus.client.application.learn;

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


public class LearnPresenter extends Presenter<LearnPresenter.MyView, LearnPresenter.MyProxy> {
  public static final NestedSlot SLOT_LEARNRUSSIANDRAUGHTS = new NestedSlot();

  @Inject
  LearnPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);
  }

  interface MyView extends View {
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.learnPage)
  @NoGatekeeper
  interface MyProxy extends ProxyPlace<LearnPresenter> {
  }
}
