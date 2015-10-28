package online.shashki.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 26.10.15
 * Time: 16:13
 */
public interface UpdateAllPlayerListEventHandler extends EventHandler {
  void onUpdateAllPlayerList(UpdateAllPlayerListEvent event);
}
