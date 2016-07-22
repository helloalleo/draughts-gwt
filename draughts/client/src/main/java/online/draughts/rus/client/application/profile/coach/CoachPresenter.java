package online.draughts.rus.client.application.profile.coach;

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
import online.draughts.rus.shared.resource.ErrorHandlerResource;
import online.draughts.rus.shared.resource.PlayersResource;


public class CoachPresenter extends PresenterWidget<CoachPresenter.MyView> implements CoachUiHandlers {
  private final ResourceDelegate<PlayersResource> playersDelegate;
  private final DialogFactory dialogFactory;
  private final DraughtsMessages messages;
  private final PlayerDto player;
  private final ResourceDelegate<ErrorHandlerResource> errorHandlingDelegate;

  @Inject
  public CoachPresenter(
      EventBus eventBus,
      MyView myView,
      DraughtsMessages messages,
      ResourceDelegate<PlayersResource> playersDelegate,
      PlaySession playSession,
      DialogFactory dialogFactory,
      ClientConfiguration config,
      PlayerDto player, ResourceDelegate<ErrorHandlerResource> errorHandlingDelegate) {
    super(eventBus, myView);

    getView().setUiHandlers(this);

    this.playersDelegate = playersDelegate;
    this.dialogFactory = dialogFactory;
    this.messages = messages;
    this.player = player;
    this.errorHandlingDelegate = errorHandlingDelegate;

    getView().setApplyToCoachEnabled(!player.isCoach());
  }

  @Override
  public void applyToCoach() {
    String message = "Пользователь " + player.getFirstName() + " " + player.getLastName()
      + " подал заявку в тренеры. ID: " + player.getId();
    errorHandlingDelegate.withCallback(new AbstractAsyncCallback<Void>(dialogFactory) {
      @Override
      public void onSuccess(Void aVoid) {
        Growl.growlNotif(messages.youApplied());
      }
    }).postApply(message, player.getId());
  }

  interface MyView extends View, HasUiHandlers<CoachUiHandlers> {
    void setApplyToCoachEnabled(boolean enabled);
  }

  public static final NestedSlot SLOT_COACH = new NestedSlot();

  public interface ViewFactory {
    CoachPresenter.MyView create();
  }

  public interface Factory {
    CoachPresenter create(PlayerDto player);
  }

  public static class FactoryImpl implements CoachPresenter.Factory {

    private final EventBus eventBus;
    private final CoachPresenter.ViewFactory viewFactory;
    private final DraughtsMessages messages;
    private final ResourceDelegate<PlayersResource> playersDelegate;
    private final ClientConfiguration config;
    private final PlaySession playSession;
    private final DialogFactory dialogFactory;
    private final ResourceDelegate<ErrorHandlerResource> errorHandlingDelegate;

    @Inject
    FactoryImpl(EventBus eventBus,
                ViewFactory viewFactory,
                DraughtsMessages messages,
                ResourceDelegate<PlayersResource> playersDelegate,
                ClientConfiguration config, PlaySession playSession,
                DialogFactory dialogFactory,
                ResourceDelegate<ErrorHandlerResource> errorHandlingDelegate) {
      this.eventBus = eventBus;
      this.viewFactory = viewFactory;
      this.messages = messages;
      this.playersDelegate = playersDelegate;
      this.config = config;
      this.playSession = playSession;
      this.dialogFactory = dialogFactory;
      this.errorHandlingDelegate = errorHandlingDelegate;
    }

    public CoachPresenter create(PlayerDto player) {
      return new CoachPresenter(eventBus, viewFactory.create(), messages, playersDelegate, playSession, dialogFactory, config,
          player, errorHandlingDelegate);
    }
  }
}
