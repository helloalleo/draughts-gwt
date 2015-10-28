package online.draughts.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 13.12.14
 * Time: 12:38
 */
public interface GameMessageEventHandler extends EventHandler {
  void onPlayerMessage(GameMessageEvent event);
}
