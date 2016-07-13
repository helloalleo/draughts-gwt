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
import online.draughts.rus.client.application.profile.general.GeneralPresenter;
import online.draughts.rus.client.application.profile.settings.SettingsPresenter;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.dto.PlayerDto;


public class ProfilePresenter extends Presenter<ProfilePresenter.MyView, ProfilePresenter.MyProxy>
    implements ProfileUiHandlers {
  static final NestedSlot SLOT_PROFILE = new NestedSlot();
  static final Slot<SettingsPresenter> SLOT_SETTINGS_CONTENT = new Slot<>();
  static final Slot<GeneralPresenter> SLOT_GENERAL_CONTENT = new Slot<>();
  private final PlayerDto player;
  private SettingsPresenter settingsPresenter;
  private final GeneralPresenter.Factory generalSettingsFactory;
  private final SettingsPresenter.Factory settingsFactory;
  private GeneralPresenter generalSettingsPresenter;

  @Inject
  ProfilePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      GeneralPresenter.Factory generalSettingsFactory,
      SettingsPresenter.Factory settingsFactory,
      Cookies cookies,
      CurrentSession currentSession) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    this.generalSettingsFactory = generalSettingsFactory;
    this.settingsFactory = settingsFactory;
    this.player = currentSession.getPlayer();

    getView().setUiHandlers(this);
    cookies.setLocation(NameTokens.PROFILE_PAGE);
  }

  @Override
  public void displayPage(String token) {
    switch (token) {
      case NameTokens.SETTINGS_PAGE:
        setInSlot(SLOT_SETTINGS_CONTENT, settingsPresenter);
        break;
      case NameTokens.GENERAL_SETTINGS_PAGE:
        setInSlot(SLOT_GENERAL_CONTENT, generalSettingsPresenter);
        break;
    }
  }

  @Override
  public void prepareFromRequest(PlaceRequest request) {
    super.prepareFromRequest(request);

    generalSettingsPresenter = generalSettingsFactory.create(player);
    settingsPresenter = settingsFactory.create(player);
    getProxy().manualReveal(ProfilePresenter.this);
    displayPage(NameTokens.GENERAL_SETTINGS_PAGE);
  }

  @Override
  public boolean useManualReveal() {
    return true;
  }

  interface MyView extends View, HasUiHandlers<ProfileUiHandlers> {
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.PROFILE_PAGE)
  interface MyProxy extends ProxyPlace<ProfilePresenter> {
  }
}
