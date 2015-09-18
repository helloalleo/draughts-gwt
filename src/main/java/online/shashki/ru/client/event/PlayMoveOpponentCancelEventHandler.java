package online.shashki.ru.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 10.09.15
 * Time: 15:48
 */
public interface PlayMoveOpponentCancelEventHandler extends EventHandler {
  void onPlayMoveOpponentCancel(PlayMoveOpponentCancelEvent event);
}
