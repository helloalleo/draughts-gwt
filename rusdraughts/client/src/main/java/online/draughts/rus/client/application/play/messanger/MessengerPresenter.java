
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
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.model.GameMessage;
import online.draughts.rus.shared.model.Player;
import online.draughts.rus.shared.resource.GameMessagesResource;

import java.util.Date;
import java.util.List;

public class MessengerPresenter extends PresenterWidget<MessengerPresenter.MyView> implements MessengerUiHandlers {

  private final ResourceDelegate<GameMessagesResource> gameMessagesDelegate;
  private final Player player;
  private final DraughtsMessages messages;
  private final ClientConfiguration config;
  private Player opponent;

  public MessengerPresenter(final EventBus eventBus,
                            final MyView view,
                            final ResourceDelegate<GameMessagesResource> gameMessagesDelegate,
                            final DraughtsMessages messages,
                            final ClientConfiguration config,
                            final CurrentSession currentSession,
                            final Player opponent) {
    super(eventBus, view);

    this.gameMessagesDelegate = gameMessagesDelegate;
    this.messages = messages;
    this.player = currentSession.getPlayer();
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

    getPlayerChat(opponent);
  }

  public void getPlayerChat(Player opponent) {
    gameMessagesDelegate.withCallback(new AbstractAsyncCallback<List<GameMessage>>() {
      @Override
      public void onSuccess(List<GameMessage> result) {
        for (GameMessage gameMessage : result) {
          if (gameMessage.getSender().getId().equals(player.getId())) {
            getView().addMyMessage(gameMessage.getMessage(), gameMessage.getSentDate());
          } else if (gameMessage.getSender().getId().equals(MessengerPresenter.this.opponent.getId())) {
            getView().addFriendMessage(gameMessage.getMessage(), gameMessage.getSentDate());
          }
        }
      }
    }).findLastMessages(Integer.valueOf(config.lastGameMessagesLoad()), player.getId(), this.opponent.getId());
  }

  @Override
  public void sendMessage(String message) {
    GameMessage gameMessage = new GameMessage();
    gameMessage.setMessageType(GameMessage.MessageType.CHAT_PRIVATE_MESSAGE);
    gameMessage.setMessage(message);
    gameMessage.setSender(player);
    gameMessage.setReceiver(opponent);

    fireEvent(new GameMessageEvent(gameMessage));
  }

  @Override
  public void resetOpponent() {
    opponent = null;
  }

  public interface MyView extends PopupView, HasUiHandlers<MessengerUiHandlers> {
    void addMyMessage(String message, Date date);

    void addFriendMessage(String message, Date date);
  }

  public interface Factory {
    MessengerPresenter create(PlayView playView, Player opponent);
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

    @Inject
    public FactoryImpl(EventBus eventBus,
                       ViewFactory viewFactory,
                       ResourceDelegate<GameMessagesResource> gameMessagesDelegate,
                       ClientConfiguration config,
                       CurrentSession currentSession,
                       DraughtsMessages messages) {
      this.eventBus = eventBus;
      this.viewFactory = viewFactory;
      this.gameMessagesDelegate = gameMessagesDelegate;
      this.config = config;
      this.currentSession = currentSession;
      this.messages = messages;
    }

    @Override
    public MessengerPresenter create(PlayView playView, Player opponent) {
      return new MessengerPresenter(eventBus, viewFactory.create(playView),
          gameMessagesDelegate, messages, config, currentSession, opponent);
    }
  }
}
