package online.shashki.ru.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.01.15
 * Time: 17:11
 */
public interface PlayMoveCancelEventHandler extends EventHandler {
  void onPlayMove(PlayMoveCancelEvent event);
}
