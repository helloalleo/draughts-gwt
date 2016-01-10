package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 13.01.15
 * Time: 0:32
 */
public class WebsocketReconnectEvent extends GwtEvent<WebsocketReconnectEventHandler> {
  public static Type<WebsocketReconnectEventHandler> TYPE = new Type<WebsocketReconnectEventHandler>();

  public Type<WebsocketReconnectEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(WebsocketReconnectEventHandler handler) {
    handler.onWebsocketReconnect(this);
  }
}
