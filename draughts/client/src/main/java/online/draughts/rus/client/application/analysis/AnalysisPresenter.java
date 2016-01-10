package online.draughts.rus.client.application.analysis;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import online.draughts.rus.client.application.ApplicationPresenter;
import online.draughts.rus.client.application.common.PlayComponentPresenter;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.Cookies;


public class AnalysisPresenter extends Presenter<AnalysisPresenter.MyView, AnalysisPresenter.MyProxy>
    implements AnalysisUiHandlers {
  public static final PermanentSlot<PlayComponentPresenter> SLOT_ANALYSIS = new PermanentSlot<>();

  @Inject
  AnalysisPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      Cookies cookies) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);

    cookies.setLocation(NameTokens.analysisPage);
  }

  @Override
  public boolean isMyTurn() {
    return false;
  }

  interface MyView extends View, HasUiHandlers<AnalysisUiHandlers> {
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.analysisPage)
  @NoGatekeeper
  interface MyProxy extends ProxyPlace<AnalysisPresenter> {
  }
}
