package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 1:57
 */
public class ChatMessageEvent extends GwtEvent<ChatMessageEventHandler> {
  public static Type<ChatMessageEventHandler> TYPE = new Type<>();

  private String message;

  public ChatMessageEvent(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public Type<ChatMessageEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(ChatMessageEventHandler handler) {
    handler.onChatMessage(this);
  }
}
