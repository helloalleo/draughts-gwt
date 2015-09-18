package online.shashki.ru.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.08.15
 * Time: 22:30
 */
public interface RemoveWebsocketHandlersEventHandler extends EventHandler {
    void onRemovePlayMoveOpponentHandler(RemovePlayMoveOpponentHandlerEvent event);
}
