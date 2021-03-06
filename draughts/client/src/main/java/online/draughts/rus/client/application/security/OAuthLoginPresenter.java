package online.draughts.rus.client.application.security;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
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


public class OAuthLoginPresenter extends Presenter<OAuthLoginPresenter.MyView, OAuthLoginPresenter.MyProxy>
    implements OAuthLoginUiHandlers {
  public static final NestedSlot SLOT_OAUTHLOGIN = new NestedSlot();

  @Inject
  OAuthLoginPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      Cookies cookies) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);
    cookies.setLocation(NameTokens.LOGIN_PAGE);
  }

  interface MyView extends View, HasUiHandlers<OAuthLoginUiHandlers> {
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.LOGIN_PAGE)
  @NoGatekeeper
  interface MyProxy extends ProxyPlace<OAuthLoginPresenter> {
  }
}
