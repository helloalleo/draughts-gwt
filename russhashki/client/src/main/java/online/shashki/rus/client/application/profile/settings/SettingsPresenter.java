package online.shashki.rus.client.application.profile.settings;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;
import online.shashki.rus.client.application.widget.dialog.InfoDialogBox;
import online.shashki.rus.client.event.UpdatePlayerListEvent;
import online.shashki.rus.client.util.SHLog;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.Player;
import online.shashki.rus.shared.rest.PlayersResource;


public class SettingsPresenter extends PresenterWidget<SettingsPresenter.MyView> implements SettingsUiHandlers {
  public static final NestedSlot SLOT_SETTINGS = new NestedSlot();
  private final ShashkiMessages messages;
  private final EventBus eventBus;
  private final ResourceDelegate<PlayersResource> playersDelegate;
  private Player player;

  @Inject
  SettingsPresenter(
      EventBus eventBus,
      MyView view,
      ShashkiMessages messages,
      ResourceDelegate<PlayersResource> playersDelegate,
      Player player) {
    super(eventBus, view);

    this.eventBus = eventBus;
    this.playersDelegate = playersDelegate;
    this.player = player;
    this.messages = messages;

    getView().setUiHandlers(this);
    getView().setPlayerName(player.getPublicName());
  }

  @Override
  public void submitNewPlayerName(String playerName) {
    player.setPlayerName(playerName);
    SHLog.debug(playerName);
    playersDelegate.withCallback(new AsyncCallback<Player>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
      }

      @Override
      public void onSuccess(Player result) {
        InfoDialogBox.setMessage(messages.profileUpdated()).show();
        try {
          eventBus.fireEvent(new UpdatePlayerListEvent());
        } catch (Exception e) {
          SHLog.error(e.getMessage(), e);
        }
      }
    }).saveOrCreate(player);
  }

  public interface ViewFactory {
    MyView create();
  }

  public interface Factory {
    SettingsPresenter create(Player player);
  }

  public static class FactoryImpl implements Factory {

    private final EventBus eventBus;
    private final ViewFactory viewFactory;
    private final ShashkiMessages messages;
    private final ResourceDelegate<PlayersResource> playersDelegate;

    @Inject
    FactoryImpl(EventBus eventBus,
                ViewFactory viewFactory,
                ShashkiMessages messages,
                ResourceDelegate<PlayersResource> playersDelegate) {
      this.eventBus = eventBus;
      this.viewFactory = viewFactory;
      this.messages = messages;
      this.playersDelegate = playersDelegate;
    }

    public SettingsPresenter create(Player player) {
      return new SettingsPresenter(eventBus, viewFactory.create(), messages, playersDelegate, player);
    }
  }

  public interface MyView extends View, HasUiHandlers<SettingsUiHandlers> {
    void setPlayerName(String playerName);
  }
}
