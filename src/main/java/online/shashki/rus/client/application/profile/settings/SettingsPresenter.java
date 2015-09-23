package online.shashki.rus.client.application.profile.settings;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;
import online.shashki.rus.client.application.login.CurrentSession;
import online.shashki.rus.client.application.profile.ProfilePresenter;
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.client.rpc.ProfileRpcServiceAsync;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.Shashist;


public class SettingsPresenter extends Presenter<SettingsPresenter.MyView, SettingsPresenter.MyProxy> implements SettingsUiHandlers {
  public static final NestedSlot SLOT_SETTINGS = new NestedSlot();
  private final ProfileRpcServiceAsync profileService;
  private final CurrentSession currentSession;
  private final ShashkiMessages messages;
  private Shashist player;


  @Inject
  SettingsPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      CurrentSession currentSession,
      ProfileRpcServiceAsync profileService,
      ShashkiMessages messages) {
    super(eventBus, view, proxy, ProfilePresenter.SLOT_PROFILE);

    this.profileService = profileService;
    this.currentSession = currentSession;
    this.messages = messages;

    getView().setUiHandlers(this);
    profileService.getCurrentProfile(new AsyncCallback<Shashist>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.showError(caught).show();
      }

      @Override
      public void onSuccess(Shashist result) {
        player = result;
        getView().setPlayerName(result.getPlayerName());
      }
    });
  }

  @Override
  public void submitNewPlayerName(String playerName) {
    player.setPlayerName(playerName);
    profileService.saveProfile(player, new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.showError(caught).show();
      }

      @Override
      public void onSuccess(Void result) {
        Window.alert(messages.profileUpdated());
      }
    });
  }

  interface MyView extends View, HasUiHandlers<SettingsUiHandlers> {
    void setPlayerName(String playerName);
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.settingsPage)
  interface MyProxy extends Proxy<SettingsPresenter> {
  }
}
