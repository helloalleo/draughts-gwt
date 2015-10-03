package online.shashki.rus.client.application.profile;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.Title;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import online.shashki.rus.client.application.ApplicationPresenter;
import online.shashki.rus.client.application.profile.mygames.MyGamesPresenter;
import online.shashki.rus.client.application.profile.settings.SettingsPresenter;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.client.utils.SHCookies;


public class ProfilePresenter extends Presenter<ProfilePresenter.MyView, ProfilePresenter.MyProxy>
    implements ProfileUiHandlers {
  public static final NestedSlot SLOT_PROFILE = new NestedSlot();
  public static final NestedSlot SLOT_PROFILE_CONTENT = new NestedSlot();
  private final MyGamesPresenter myGamesPresenter;
  private final SettingsPresenter settingsPresenter;


  @Inject
  ProfilePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      MyGamesPresenter myGamesPresenter,
      SettingsPresenter settingsPresenter) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    this.myGamesPresenter = myGamesPresenter;
    this.settingsPresenter = settingsPresenter;

    getView().setUiHandlers(this);
    SHCookies.setLocation(NameTokens.profilePage);
  }

  @Override
  protected void onBind() {
    super.onBind();

    setInSlot(SLOT_PROFILE_CONTENT, myGamesPresenter);
  }

  @Override
  public void displayPage(String token) {
    switch (token) {
      case NameTokens.myGamesPage:
        setInSlot(SLOT_PROFILE_CONTENT, myGamesPresenter);
        break;
      case NameTokens.settingsPage:
        setInSlot(SLOT_PROFILE_CONTENT, settingsPresenter);
        break;
    }
  }

  interface MyView extends View, HasUiHandlers<ProfileUiHandlers> {
  }

  @Title("Профиль")
  @ProxyCodeSplit
  @NameToken(NameTokens.profilePage)
  interface MyProxy extends ProxyPlace<ProfilePresenter> {
  }
}
