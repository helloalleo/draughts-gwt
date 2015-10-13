package online.shashki.rus.client.application.profile;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.Title;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.presenter.slots.Slot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.shashki.rus.client.application.ApplicationPresenter;
import online.shashki.rus.client.application.profile.settings.SettingsPresenter;
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.client.util.SHCookies;
import online.shashki.rus.shared.model.Player;
import online.shashki.rus.shared.service.PlayerService;
import online.shashki.rus.shared.service.PlayerServiceAsync;


public class ProfilePresenter extends Presenter<ProfilePresenter.MyView, ProfilePresenter.MyProxy>
    implements ProfileUiHandlers {
  public static final NestedSlot SLOT_PROFILE = new NestedSlot();
  public static final Slot<SettingsPresenter> SLOT_PROFILE_CONTENT = new Slot<>();
  private SettingsPresenter settingsPresenter;
  private final PlayerServiceAsync playrService;
  private final SettingsPresenter.Factory settingsFactory;

  @Inject
  ProfilePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      SettingsPresenter.Factory settingsFactory) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    this.playrService = PlayerService.App.getInstance();
    this.settingsFactory = settingsFactory;

    getView().setUiHandlers(this);
    SHCookies.setLocation(NameTokens.profilePage);
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

    playrService.getCurrentProfile(new AsyncCallback<Player>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
        getProxy().manualRevealFailed();
      }

      @Override
      public void onSuccess(Player result) {
        settingsPresenter = settingsFactory.create(result);
        getProxy().manualReveal(ProfilePresenter.this);
        displayPage(NameTokens.settingsPage);
      }
    });
  }

  @Override
  public boolean useManualReveal() {
    return true;
  }

  interface MyView extends View, HasUiHandlers<ProfileUiHandlers> {
  }

  @Title("Профиль")
  @ProxyCodeSplit
  @NameToken(NameTokens.profilePage)
  interface MyProxy extends ProxyPlace<ProfilePresenter> {
  }
}
