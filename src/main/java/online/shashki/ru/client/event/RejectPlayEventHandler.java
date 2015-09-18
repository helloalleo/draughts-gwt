package online.shashki.ru.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 2:23
 */
public interface RejectPlayEventHandler extends EventHandler {
  void onRejectPlay(RejectPlayEvent event);
}
