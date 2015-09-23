package online.shashki.rus.client.application.profile.settings;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;
import online.shashki.rus.client.application.profile.ProfilePresenter;
import online.shashki.rus.client.place.NameTokens;


public class SettingsPresenter extends Presenter<SettingsPresenter.MyView, SettingsPresenter.MyProxy> implements SettingsUiHandlers {
  public static final NestedSlot SLOT_SETTINGS = new NestedSlot();


  @Inject
  SettingsPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy) {
    super(eventBus, view, proxy, ProfilePresenter.SLOT_PROFILE);

    getView().setUiHandlers(this);
  }

  interface MyView extends View, HasUiHandlers<SettingsUiHandlers> {
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.settingsPage)
  interface MyProxy extends Proxy<SettingsPresenter> {
  }
}
