package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.12.14
 * Time: 10:51
 */
public class DisconnectFromPlayEvent extends GwtEvent<DisconnectFromPlayEventHandler> {
    public static Type<DisconnectFromPlayEventHandler> TYPE = new Type<>();

    public Type<DisconnectFromPlayEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(DisconnectFromPlayEventHandler handler) {
        handler.onDisconnectFromPlay(this);
    }
}
