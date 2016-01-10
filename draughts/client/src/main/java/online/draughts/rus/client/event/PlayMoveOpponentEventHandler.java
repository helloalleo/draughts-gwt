package online.draughts.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.01.15
 * Time: 19:46
 */
public interface PlayMoveOpponentEventHandler extends EventHandler {
  void onPlayMoveOpponent(PlayMoveOpponentEvent event);
}
