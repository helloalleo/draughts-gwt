package online.shashki.rus.client.application.profile.settings;

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
import online.shashki.rus.client.application.security.CurrentSession;
import online.shashki.rus.client.application.profile.ProfilePresenter;
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;
import online.shashki.rus.client.application.widget.dialog.InfoDialogBox;
import online.shashki.rus.client.event.UpdatePlayerListEvent;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.client.service.ProfileRpcServiceAsync;
import online.shashki.rus.client.utils.SHLog;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.Shashist;


public class SettingsPresenter extends Presenter<SettingsPresenter.MyView, SettingsPresenter.MyProxy> implements SettingsUiHandlers {
  public static final NestedSlot SLOT_SETTINGS = new NestedSlot();
  private final ProfileRpcServiceAsync profileService;
  private final CurrentSession currentSession;
  private final ShashkiMessages messages;
  private final EventBus eventBus;
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

    this.eventBus = eventBus;
    this.profileService = profileService;
    this.currentSession = currentSession;
    this.messages = messages;

    getView().setUiHandlers(this);
    profileService.getCurrentProfile(new AsyncCallback<Shashist>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
      }

      @Override
      public void onSuccess(Shashist result) {
        player = result;
        getView().setPlayerName(result.getPublicName());
      }
    });
  }

  @Override
  public void submitNewPlayerName(String playerName) {
    player.setPlayerName(playerName);
    SHLog.debug(playerName);
    profileService.saveProfile(player, new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
      }

      @Override
      public void onSuccess(Void result) {
        eventBus.fireEvent(new UpdatePlayerListEvent());
        InfoDialogBox.setMessage(messages.profileUpdated()).show();
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
