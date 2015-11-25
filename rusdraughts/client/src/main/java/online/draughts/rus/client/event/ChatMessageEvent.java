package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.shared.model.GameMessage;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 1:57
 */
public class ChatMessageEvent extends GwtEvent<ChatMessageEventHandler> {
  public static Type<ChatMessageEventHandler> TYPE = new Type<>();

  private GameMessage message;

  public ChatMessageEvent(GameMessage message) {
    this.message = message;
  }

  public GameMessage getMessage() {
    return message;
  }

  public Type<ChatMessageEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(ChatMessageEventHandler handler) {
    handler.onChatMessage(this);
  }
}
