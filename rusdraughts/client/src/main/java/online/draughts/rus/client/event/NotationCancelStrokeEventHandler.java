package online.draughts.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.09.15
 * Time: 19:49
 */
public interface NotationCancelStrokeEventHandler extends EventHandler {
  void onNotationCancelStroke(NotationCancelStrokeEvent event);
}
