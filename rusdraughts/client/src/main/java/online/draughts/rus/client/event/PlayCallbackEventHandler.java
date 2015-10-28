package online.draughts.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.10.15
 * Time: 9:46
 */
public interface PlayCallbackEventHandler extends EventHandler {
  void onPlayCallback(PlayCallbackEvent event);
}
