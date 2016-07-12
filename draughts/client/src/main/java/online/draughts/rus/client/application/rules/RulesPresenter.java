package online.draughts.rus.client.application.rules;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import online.draughts.rus.client.application.ApplicationPresenter;
import online.draughts.rus.client.place.NameTokens;


public class RulesPresenter extends Presenter<RulesPresenter.MyView, RulesPresenter.MyProxy> {
  static final NestedSlot SLOT_RULES = new NestedSlot();

  @Inject
  RulesPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);
  }

  interface MyView extends View {
  }

  @NameToken(NameTokens.rulesPage)
  @NoGatekeeper
  @ProxyStandard
  interface MyProxy extends ProxyPlace<RulesPresenter> {
  }
}
