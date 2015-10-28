package online.draughts.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.12.14
 * Time: 10:50
 */
public interface ConnectedToPlayEventHandler extends EventHandler {
    void onConnectedToPlay(ConnectedToPlayEvent event);
}
