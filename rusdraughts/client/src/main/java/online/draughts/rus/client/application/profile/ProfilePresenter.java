package online.draughts.rus.client.application.profile;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.presenter.slots.Slot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.draughts.rus.client.application.ApplicationPresenter;
import online.draughts.rus.client.application.profile.settings.SettingsPresenter;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.DCookies;
import online.draughts.rus.shared.model.Player;


public class ProfilePresenter extends Presenter<ProfilePresenter.MyView, ProfilePresenter.MyProxy>
    implements ProfileUiHandlers {
  public static final NestedSlot SLOT_PROFILE = new NestedSlot();
  public static final Slot<SettingsPresenter> SLOT_PROFILE_CONTENT = new Slot<>();
  private final Player player;
  private SettingsPresenter settingsPresenter;
  private final SettingsPresenter.Factory settingsFactory;

  @Inject
  ProfilePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      SettingsPresenter.Factory settingsFactory,
      CurrentSession currentSession) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    this.settingsFactory = settingsFactory;
    this.player = currentSession.getPlayer();

    getView().setUiHandlers(this);
    DCookies.setLOCATION(NameTokens.profilePage);
  }

  @Override
  public void displayPage(String token) {
    switch (token) {
      case NameTokens.settingsPage:
        setInSlot(SLOT_PROFILE_CONTENT, settingsPresenter);
        break;
    }
  }

  @Override
  public void prepareFromRequest(PlaceRequest request) {
    super.prepareFromRequest(request);

    settingsPresenter = settingsFactory.create(player);
    getProxy().manualReveal(ProfilePresenter.this);
    displayPage(NameTokens.settingsPage);
  }

  @Override
  public boolean useManualReveal() {
    return true;
  }

  interface MyView extends View, HasUiHandlers<ProfileUiHandlers> {
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.profilePage)
  interface MyProxy extends ProxyPlace<ProfilePresenter> {
  }
}
