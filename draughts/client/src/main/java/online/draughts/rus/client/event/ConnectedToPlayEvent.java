package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.12.14
 * Time: 10:50
 */
public class ConnectedToPlayEvent extends GwtEvent<ConnectedToPlayEventHandler> {
    public static Type<ConnectedToPlayEventHandler> TYPE = new Type<>();

    public Type<ConnectedToPlayEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ConnectedToPlayEventHandler handler) {
        handler.onConnectedToPlay(this);
    }
}
