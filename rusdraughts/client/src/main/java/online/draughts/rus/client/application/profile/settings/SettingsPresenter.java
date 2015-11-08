package online.draughts.rus.client.application.profile.settings;

import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import online.draughts.rus.client.application.widget.dialog.ErrorDialogBox;
import online.draughts.rus.client.application.widget.growl.Growl;
import online.draughts.rus.client.event.UpdateAllPlayerListEvent;
import online.draughts.rus.client.websocket.PlaySession;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.model.Player;
import online.draughts.rus.shared.resource.PlayersResource;


public class SettingsPresenter extends PresenterWidget<SettingsPresenter.MyView> implements SettingsUiHandlers {
  public static final NestedSlot SLOT_SETTINGS = new NestedSlot();
  private final DraughtsMessages messages;
  private final ResourceDelegate<PlayersResource> playersDelegate;
  private final PlaySession playSession;
  private final ClientConfiguration config;
  private Player player;

  SettingsPresenter(
      EventBus eventBus,
      MyView view,
      DraughtsMessages messages,
      ResourceDelegate<PlayersResource> playersDelegate,
      PlaySession playSession,
      ClientConfiguration config,
      Player player) {
    super(eventBus, view);

    this.playersDelegate = playersDelegate;
    this.playSession = playSession;
    this.player = player;
    this.config = config;
    this.messages = messages;

    getView().setUiHandlers(this);
    getView().setPlayerName(player.getPublicName());
  }

  @Override
  public void submitNewPlayerName(String playerName) {
    String name = playerName.replace(config.escapeChars(), "");
    name = SimpleHtmlSanitizer.getInstance().sanitize(name).asString();
    player.setPlayerName(name);
    playersDelegate.withCallback(new AsyncCallback<Player>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
      }

      @Override
      public void onSuccess(Player result) {
        SettingsPresenter.this.player.setPlayerName(result.getPlayerName());
        Growl.growlNotif(messages.profileUpdated());
        if (playSession.isConnected()) {
          fireEvent(new UpdateAllPlayerListEvent());
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
    private final DraughtsMessages messages;
    private final ResourceDelegate<PlayersResource> playersDelegate;
    private final ClientConfiguration config;
    private final PlaySession playSession;

    @Inject
    FactoryImpl(EventBus eventBus,
                ViewFactory viewFactory,
                DraughtsMessages messages,
                ResourceDelegate<PlayersResource> playersDelegate,
                ClientConfiguration config, PlaySession playSession) {
      this.eventBus = eventBus;
      this.viewFactory = viewFactory;
      this.messages = messages;
      this.playersDelegate = playersDelegate;
      this.config = config;
      this.playSession = playSession;
    }

    public SettingsPresenter create(Player player) {
      return new SettingsPresenter(eventBus, viewFactory.create(), messages, playersDelegate, playSession, config,
          player);
    }
  }

  public interface MyView extends View, HasUiHandlers<SettingsUiHandlers> {
    void setPlayerName(String playerName);
  }
}
