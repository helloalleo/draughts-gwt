package online.shashki.rus.client.application.profile.settings;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;
import online.shashki.rus.client.application.widget.dialog.InfoDialogBox;
import online.shashki.rus.client.event.UpdatePlayerListEvent;
import online.shashki.rus.client.service.ProfileRpcServiceAsync;
import online.shashki.rus.client.utils.SHLog;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.Shashist;


public class SettingsPresenter extends PresenterWidget<SettingsPresenter.MyView> implements SettingsUiHandlers {
  public static final NestedSlot SLOT_SETTINGS = new NestedSlot();
  private final ShashkiMessages messages;
  private final EventBus eventBus;
  private final ProfileRpcServiceAsync profileService;
  private Shashist player;

  @Inject
  SettingsPresenter(
      EventBus eventBus,
      MyView view,
      ProfileRpcServiceAsync profileService,
      ShashkiMessages messages,
      Shashist player) {
    super(eventBus, view);

    this.eventBus = eventBus;
    this.profileService = profileService;
    this.player = player;
    this.messages = messages;

    getView().setUiHandlers(this);
    getView().setPlayerName(player.getPublicName());
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
        InfoDialogBox.setMessage(messages.profileUpdated()).show();
        try {
          eventBus.fireEvent(new UpdatePlayerListEvent());
        } catch (Exception e) {
          SHLog.error(e.getMessage(), e);
        }
      }
    });
  }

  public interface ViewFactory {
    MyView create();
  }

  public interface Factory {
    SettingsPresenter create(Shashist player);
  }

  public static class FactoryImpl implements Factory {

    private final EventBus eventBus;
    private final ViewFactory viewFactory;
    private final ShashkiMessages messages;
    private final ProfileRpcServiceAsync profileService;

    @Inject
    FactoryImpl(EventBus eventBus,
                ViewFactory viewFactory,
                ProfileRpcServiceAsync profileService,
                ShashkiMessages messages) {
      this.eventBus = eventBus;
      this.viewFactory = viewFactory;
      this.profileService = profileService;
      this.messages = messages;
    }

    public SettingsPresenter create(Shashist player) {
      return new SettingsPresenter(eventBus, viewFactory.create(), profileService, messages, player);
    }
  }

  public interface MyView extends View, HasUiHandlers<SettingsUiHandlers> {
    void setPlayerName(String playerName);
  }
}
