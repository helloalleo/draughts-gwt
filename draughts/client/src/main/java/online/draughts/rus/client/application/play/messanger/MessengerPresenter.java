
package online.draughts.rus.client.application.play.messanger;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import online.draughts.rus.client.application.play.PlayView;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.event.ChatMessageEvent;
import online.draughts.rus.client.event.ChatMessageEventHandler;
import online.draughts.rus.client.event.GameMessageEvent;
import online.draughts.rus.client.event.ResetUnreadMessagesEvent;
import online.draughts.rus.client.gin.DialogFactory;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.dto.GameMessageDto;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.resource.GameMessagesResource;

import java.util.Date;
import java.util.List;

public class MessengerPresenter extends PresenterWidget<MessengerPresenter.MyView> implements MessengerUiHandlers {

  private final ResourceDelegate<GameMessagesResource> gameMessagesDelegate;
  private final PlayerDto player;
  private final ClientConfiguration config;
  private final DraughtsMessages messages;
  private PlayerDto opponent;
  private final DialogFactory dialogFactory;

  private MessengerPresenter(final EventBus eventBus,
                             final MyView view,
                             final ResourceDelegate<GameMessagesResource> gameMessagesDelegate,
                             final ClientConfiguration config,
                             final CurrentSession currentSession,
                             final DraughtsMessages messages,
                             DialogFactory dialogFactory,
                             final PlayerDto opponent) {
    super(eventBus, view);

    this.gameMessagesDelegate = gameMessagesDelegate;
    this.player = currentSession.getPlayer();
    this.messages = messages;
    this.dialogFactory = dialogFactory;
    this.opponent = opponent;
    this.config = config;

    registerHandlers();
    getView().setUiHandlers(this);
  }

  private void registerHandlers() {
    addRegisteredHandler(ChatMessageEvent.TYPE, new ChatMessageEventHandler() {
      @Override
      public void onChatMessage(ChatMessageEvent event) {
        getView().addFriendMessage(event.getMessage().getMessage(), event.getMessage().getSentDate());
      }
    });
  }

  @Override
  protected void onReveal() {
    super.onReveal();

    getPlayerChat();
  }

  private void specialMessage() {
    if (opponent.isModerator()) {
      getView().addFriendMessage(messages.moderatorGreeting(), new Date());
    } else if (!opponent.isOnline()) {
      getView().addFriendMessage(messages.userOfflineButYouCanMessageHim(opponent.getPublicName()), new Date());
    }
  }

  private void getPlayerChat() {
    getView().setHeading(opponent.getPublicName());
    gameMessagesDelegate.withCallback(new AbstractAsyncCallback<List<GameMessageDto>>(dialogFactory) {
      @Override
      public void onSuccess(List<GameMessageDto> result) {
        for (GameMessageDto gameMessage : result) {
          if (gameMessage.getSender().getId() == player.getId()) {
            // моё сообщение
            getView().addMyMessage(gameMessage.getMessage(), gameMessage.getSentDate());
          } else if (gameMessage.getSender().getId() == MessengerPresenter.this.opponent.getId()) {
            // сообщение от друга
            getView().addFriendMessage(gameMessage.getMessage(), gameMessage.getSentDate());
          }
        }
        specialMessage();
        getView().setMessageFocus();
      }
    }).findLastMessages(Integer.valueOf(config.lastGameMessagesLoad()), player.getId(), this.opponent.getId());
  }

  @Override
  public void sendMessage(String message) {
    GameMessageDto gameMessage = new GameMessageDto();
    gameMessage.setMessageType(GameMessageDto.MessageType.CHAT_PRIVATE_MESSAGE);
    gameMessage.setMessage(message);
    gameMessage.setSender(player);
    gameMessage.setReceiver(opponent);

    fireEvent(new GameMessageEvent(gameMessage));
  }

  @Override
  public void resetOpponent() {
    fireEvent(new ResetUnreadMessagesEvent(opponent));
    opponent = null;
  }

  public interface MyView extends PopupView, HasUiHandlers<MessengerUiHandlers> {
    void setMessageFocus();

    void addMyMessage(String message, Date date);

    void addFriendMessage(String message, Date date);

    void setHeading(String publicName);
  }

  public interface Factory {
    MessengerPresenter create(PlayView playView, PlayerDto opponent);
  }

  public interface ViewFactory {
    MyView create(PlayView playView);
  }

  public static class FactoryImpl implements Factory {

    private final EventBus eventBus;
    private final ViewFactory viewFactory;
    private final ResourceDelegate<GameMessagesResource> gameMessagesDelegate;
    private final ClientConfiguration config;
    private final CurrentSession currentSession;
    private final DraughtsMessages messages;
    private final DialogFactory dialogFactory;

    @Inject
    public FactoryImpl(EventBus eventBus,
                       ViewFactory viewFactory,
                       ResourceDelegate<GameMessagesResource> gameMessagesDelegate,
                       ClientConfiguration config,
                       CurrentSession currentSession,
                       DraughtsMessages messages,
                       DialogFactory dialogFactory) {
      this.eventBus = eventBus;
      this.viewFactory = viewFactory;
      this.gameMessagesDelegate = gameMessagesDelegate;
      this.config = config;
      this.currentSession = currentSession;
      this.messages = messages;
      this.dialogFactory = dialogFactory;
    }

    @Override
    public MessengerPresenter create(PlayView playView, PlayerDto opponent) {
      return new MessengerPresenter(eventBus, viewFactory.create(playView),
          gameMessagesDelegate, config, currentSession, messages, dialogFactory, opponent);
    }
  }
}
