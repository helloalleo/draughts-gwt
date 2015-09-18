package online.shashki.ru.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 1:37
 */
public interface StartPlayEventHandler extends EventHandler {
  void onStartPlay(StartPlayEvent event);
}
