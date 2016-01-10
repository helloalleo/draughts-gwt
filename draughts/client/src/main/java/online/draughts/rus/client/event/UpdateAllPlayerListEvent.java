package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 26.10.15
 * Time: 16:13
 */
public class UpdateAllPlayerListEvent extends GwtEvent<UpdateAllPlayerListEventHandler> {
  public static Type<UpdateAllPlayerListEventHandler> TYPE = new Type<UpdateAllPlayerListEventHandler>();

  public Type<UpdateAllPlayerListEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(UpdateAllPlayerListEventHandler handler) {
    handler.onUpdateAllPlayerList(this);
  }
}
