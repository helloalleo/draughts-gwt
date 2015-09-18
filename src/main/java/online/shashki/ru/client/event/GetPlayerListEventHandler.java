package online.shashki.ru.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 11.12.14
 * Time: 0:41
 */
public interface GetPlayerListEventHandler extends EventHandler {
  void onGetPlayerList(GetPlayerListEvent event);
}
