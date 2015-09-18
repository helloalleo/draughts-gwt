package online.shashki.ru.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.12.14
 * Time: 10:51
 */
public interface DisconnectFromPlayEventHandler extends EventHandler {
    void onDisconnectFromPlay(DisconnectFromPlayEvent event);
}
