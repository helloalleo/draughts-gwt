package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.01.15
 * Time: 23:09
 */
public class ClearPlayComponentEvent extends GwtEvent<ClearPlayComponentEventHandler> {
  public static Type<ClearPlayComponentEventHandler> TYPE = new Type<ClearPlayComponentEventHandler>();

  public Type<ClearPlayComponentEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(ClearPlayComponentEventHandler handler) {
    handler.onClearPlayComponent(this);
  }
}
