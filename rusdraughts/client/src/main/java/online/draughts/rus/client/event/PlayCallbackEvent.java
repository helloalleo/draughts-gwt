package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.10.15
 * Time: 9:46
 */
public class PlayCallbackEvent extends GwtEvent<PlayCallbackEventHandler> {
  public static Type<PlayCallbackEventHandler> TYPE = new Type<PlayCallbackEventHandler>();

  public Type<PlayCallbackEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(PlayCallbackEventHandler handler) {
    handler.onPlayCallback(this);
  }
}
