package online.draughts.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.01.15
 * Time: 9:48
 */
public interface TurnChangeEventHandler extends EventHandler {
  void onTurnChange(TurnChangeEvent event);
}
