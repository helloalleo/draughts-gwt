package online.draughts.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 10.09.15
 * Time: 10:25
 */
public interface PlayMoveMessageEventHandler extends EventHandler {
  void onPlayMoveMessage(PlayMoveMessageEvent event);
}
