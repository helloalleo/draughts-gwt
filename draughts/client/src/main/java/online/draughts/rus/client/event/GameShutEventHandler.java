package online.draughts.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.04.16
 * Time: 18:36
 */
public interface GameShutEventHandler extends EventHandler {
  void onGameShut(GameShutEvent event);
}
