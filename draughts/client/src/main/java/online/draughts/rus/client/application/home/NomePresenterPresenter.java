package online.draughts.rus.client.application.home;

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

public class NomePresenterPresenter extends Presenter<NomePresenterPresenter.MyView, NomePresenterPresenter.MyProxy> implements NomePresenterUiHandlers {
  interface MyView extends View, HasUiHandlers<NomePresenterUiHandlers> {
  }

  @NameToken(NameTokens.nome)
  @ProxyCodeSplit
  interface MyProxy extends ProxyPlace<NomePresenterPresenter> {
  }

  static final PermanentSlot<NomePresenterPresenter> SLOT_NOMEPRESENTER = new PermanentSlot<>();

  @Inject
  NomePresenterPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);
  }

  @Override
  public boolean useManualReveal() {
    return true;
  }
}
