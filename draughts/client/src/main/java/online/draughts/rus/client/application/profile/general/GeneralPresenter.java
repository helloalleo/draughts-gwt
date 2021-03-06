
package online.draughts.rus.client.application.profile.general;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import online.draughts.rus.client.application.widget.growl.Growl;
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.gin.DialogFactory;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.resource.PlayersResource;

public class GeneralPresenter extends PresenterWidget<GeneralPresenter.MyView> implements GeneralUiHandlers {

  static final NestedSlot SLOT_GENERAL_SETTINGS = new NestedSlot();
  private final DraughtsMessages messages;
  private final ResourceDelegate<PlayersResource> playersDelegate;
  private PlayerDto player;
  private final DialogFactory dialogFactory;

  @Inject
  GeneralPresenter(EventBus eventBus, MyView view, DraughtsMessages messages,
                   ResourceDelegate<PlayersResource> playersDelegate,
                   DialogFactory dialogFactory, PlayerDto player) {
    super(eventBus, view);
    this.playersDelegate = playersDelegate;
    this.dialogFactory = dialogFactory;
    this.player = player;
    this.messages = messages;

    getView().setUiHandlers(this);
    getView().setSubscribed(player.isSubscribed());
  }

  @Override
  public void setSubscribed(final Boolean subscribed) {
    player.setSubscribed(subscribed);
    playersDelegate.withCallback(new AbstractAsyncCallback<PlayerDto>(dialogFactory) {
      @Override
      public void onSuccess(PlayerDto result) {
        if (subscribed) {
          Growl.growlNotif(messages.youIsSubscribedToExtendedAccount());
        } else {
          Growl.growlNotif(messages.youIsUnsubscribedFromExtendedAccount());
        }
      }
    }).save(player);
  }

  interface MyView extends View, HasUiHandlers<GeneralUiHandlers> {
    void setSubscribed(boolean subscribed);
  }

  public interface ViewFactory {
    GeneralPresenter.MyView create();
  }

  public interface Factory {
    GeneralPresenter create(PlayerDto player);
  }

  public static class FactoryImpl implements GeneralPresenter.Factory {

    private final EventBus eventBus;
    private final GeneralPresenter.ViewFactory viewFactory;
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

    public GeneralPresenter create(PlayerDto player) {
      return new GeneralPresenter(eventBus, viewFactory.create(), messages, playersDelegate,
          dialogFactory, player);
    }
  }

}
