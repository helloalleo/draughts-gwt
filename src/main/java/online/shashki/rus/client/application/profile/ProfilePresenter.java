package online.shashki.rus.client.application.profile;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import online.shashki.rus.client.application.ApplicationPresenter;
import online.shashki.rus.client.place.NameTokens;


public class ProfilePresenter extends Presenter<ProfilePresenter.MyView, ProfilePresenter.MyProxy> implements ProfileUiHandlers {
  public static final NestedSlot SLOT_PROFILE = new NestedSlot();


  @Inject
  ProfilePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);
  }

  interface MyView extends View, HasUiHandlers<ProfileUiHandlers> {
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.profilePage)
  interface MyProxy extends ProxyPlace<ProfilePresenter> {
  }
}
