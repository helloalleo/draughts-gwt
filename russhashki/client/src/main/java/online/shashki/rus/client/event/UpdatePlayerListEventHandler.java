package online.shashki.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.08.15
 * Time: 17:45
 */
public interface UpdatePlayerListEventHandler extends EventHandler {
    void onUpdatePlayerList(UpdatePlayerListEvent event);
}
