package online.draughts.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 26.12.15
 * Time: 11:53
 */
public interface ResetUnreadMessagesEventHandler extends EventHandler {
  void onResetUnreadMessages(ResetUnreadMessagesEvent event);
}
