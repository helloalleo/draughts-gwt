package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.shared.dto.GameMessageDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 1:57
 */
public class ChatMessageEvent extends GwtEvent<ChatMessageEventHandler> {
  public static Type<ChatMessageEventHandler> TYPE = new Type<>();

  private GameMessageDto message;

  public ChatMessageEvent(GameMessageDto message) {
    this.message = message;
  }

  public GameMessageDto getMessage() {
    return message;
  }

  public Type<ChatMessageEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(ChatMessageEventHandler handler) {
    handler.onChatMessage(this);
  }
}
