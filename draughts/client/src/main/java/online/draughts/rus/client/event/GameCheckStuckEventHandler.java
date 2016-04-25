package online.draughts.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.04.16
 * Time: 19:52
 */
public interface GameCheckStuckEventHandler extends EventHandler {
  void onGameCheckStuck(GameCheckStuckEvent event);
}
