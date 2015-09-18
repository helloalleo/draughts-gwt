package online.shashki.ru.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.08.15
 * Time: 22:30
 */
public class RemovePlayMoveOpponentHandlerEvent extends GwtEvent<RemoveWebsocketHandlersEventHandler> {
    public static Type<RemoveWebsocketHandlersEventHandler> TYPE = new Type<RemoveWebsocketHandlersEventHandler>();

    public Type<RemoveWebsocketHandlersEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(RemoveWebsocketHandlersEventHandler handler) {
        handler.onRemovePlayMoveOpponentHandler(this);
    }
}
