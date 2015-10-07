package online.shashki.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 1:57
 */
public interface ChatMessageEventHandler extends EventHandler {
  void onChatMessage(ChatMessageEvent event);
}
