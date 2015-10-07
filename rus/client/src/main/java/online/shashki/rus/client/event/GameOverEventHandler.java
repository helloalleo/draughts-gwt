package online.shashki.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.10.15
 * Time: 13:53
 */
public interface GameOverEventHandler extends EventHandler {
  void onGameOver(GameOverEvent event);
}
