package online.draughts.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.10.15
 * Time: 21:31
 */
public interface UpdatePlayerFriendListEventHandler extends EventHandler {
  void onUpdatePlayerFriendList(UpdatePlayerFriendListEvent event);
}
