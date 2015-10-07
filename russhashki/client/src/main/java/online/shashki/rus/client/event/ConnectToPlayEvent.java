package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.12.14
 * Time: 10:48
 */
public class ConnectToPlayEvent extends GwtEvent<ConnectToPlayEventHandler> {
    public static Type<ConnectToPlayEventHandler> TYPE = new Type<>();

    public Type<ConnectToPlayEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ConnectToPlayEventHandler handler) {
        handler.onConnectToPlay(this);
    }
}
