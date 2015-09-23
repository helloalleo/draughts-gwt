package online.shashki.rus.client.application.profile.mygames;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import online.shashki.rus.client.application.profile.ProfilePresenter;
import online.shashki.rus.client.place.NameTokens;


public class MyGamesPresenter extends Presenter<MyGamesPresenter.MyView, MyGamesPresenter.MyProxy> implements MyGamesUiHandlers {
  public static final NestedSlot SLOT_MYGAMES = new NestedSlot();


  @Inject
  MyGamesPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy) {
    super(eventBus, view, proxy, ProfilePresenter.SLOT_PROFILE);

    getView().setUiHandlers(this);
  }

  interface MyView extends View, HasUiHandlers<MyGamesUiHandlers> {
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.myGamesPage)
  interface MyProxy extends ProxyPlace<MyGamesPresenter> {
  }

}
