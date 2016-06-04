package online.draughts.rus.client.application.profile.settings;

import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import online.draughts.rus.client.application.widget.growl.Growl;
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.event.UpdateAllPlayerListEvent;
import online.draughts.rus.client.gin.DialogFactory;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.resource.PlayersResource;
import online.draughts.rus.shared.util.StringUtils;


public class SettingsPresenter extends PresenterWidget<SettingsPresenter.MyView> implements SettingsUiHandlers {
  static final NestedSlot SLOT_SETTINGS = new NestedSlot();
  private final DraughtsMessages messages;
  private final ResourceDelegate<PlayersResource> playersDelegate;
  private final PlaySession playSession;
  private final ClientConfiguration config;
  private final DialogFactory dialogFactory;
  private PlayerDto player;

  private SettingsPresenter(
      EventBus eventBus,
      MyView view,
      DraughtsMessages messages,
      ResourceDelegate<PlayersResource> playersDelegate,
      PlaySession playSession,
      DialogFactory dialogFactory, ClientConfiguration config,
      PlayerDto player) {
    super(eventBus, view);

    this.playersDelegate = playersDelegate;
    this.playSession = playSession;
    this.dialogFactory = dialogFactory;
    this.player = player;
    this.config = config;
    this.messages = messages;

    getView().setUiHandlers(this);
    getView().setPlayerName(player.getPublicName());
    getView().setSubscribedOnNewsletter(player.isSubscribeOnNewsletter());
  }

  @Override
  public void submitNewPlayerName(String playerName) {
    if (StringUtils.isEmpty(playerName) || playerName.length() < 3) {
      Growl.growlNotif(messages.tooShortPlayerName());
      return;
    } else if (playerName.length() > 19) {
      Growl.growlNotif(messages.tooLongPlayerName());
      return;
    }

    String name = playerName.replace(config.escapeChars(), "");
    name = SimpleHtmlSanitizer.getInstance().sanitize(name).asString();
    player.setPlayerName(name);
    playersDelegate.withCallback(new AbstractAsyncCallback<PlayerDto>(dialogFactory) {
      @Override
      public void onSuccess(PlayerDto result) {
        SettingsPresenter.this.player.setPlayerName(result.getPlayerName());
        Growl.growlNotif(messages.profileUpdated());
        if (playSession.isConnected()) {
          fireEvent(new UpdateAllPlayerListEvent());
        }
      }
    }).save(player);
  }

  @Override
  public void subscribeOnNewsletter(Boolean value) {
    player.setSubscribeOnNewsletter(value);
    playersDelegate.withCallback(new AbstractAsyncCallback<PlayerDto>(dialogFactory) {
      @Override
      public void onSuccess(PlayerDto result) {
        Growl.growlNotif(messages.profileUpdated());
      }
    }).save(player);
  }

  public interface ViewFactory {
    MyView create();
  }

  public interface Factory {
    SettingsPresenter create(PlayerDto player);
  }

  public static class FactoryImpl implements Factory {

    private final EventBus eventBus;
    private final ViewFactory viewFactory;
    private final DraughtsMessages messages;
    private final ResourceDelegate<PlayersResource> playersDelegate;
    private final ClientConfiguration config;
    private final PlaySession playSession;
    private final DialogFactory dialogFactory;

    @Inject
    FactoryImpl(EventBus eventBus,
                ViewFactory viewFactory,
                DraughtsMessages messages,
                ResourceDelegate<PlayersResource> playersDelegate,
                ClientConfiguration config, PlaySession playSession,
                DialogFactory dialogFactory) {
      this.eventBus = eventBus;
      this.viewFactory = viewFactory;
      this.messages = messages;
      this.playersDelegate = playersDelegate;
      this.config = config;
      this.playSession = playSession;
      this.dialogFactory = dialogFactory;
    }

    public SettingsPresenter create(PlayerDto player) {
      return new SettingsPresenter(eventBus, viewFactory.create(), messages, playersDelegate, playSession, dialogFactory, config,
          player);
    }
  }

  public interface MyView extends View, HasUiHandlers<SettingsUiHandlers> {
    void setPlayerName(String playerName);

    void setSubscribedOnNewsletter(boolean subscribeOnNewsletter);
  }
}
