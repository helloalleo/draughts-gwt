package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.08.15
 * Time: 17:45
 */
public class UpdatePlayerListEvent extends GwtEvent<UpdatePlayerListEventHandler> {
    public static Type<UpdatePlayerListEventHandler> TYPE = new Type<UpdatePlayerListEventHandler>();

    public Type<UpdatePlayerListEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(UpdatePlayerListEventHandler handler) {
        handler.onUpdatePlayerList(this);
    }
}
