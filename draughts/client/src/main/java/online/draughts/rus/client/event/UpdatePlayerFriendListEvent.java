package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.10.15
 * Time: 21:31
 */
public class UpdatePlayerFriendListEvent extends GwtEvent<UpdatePlayerFriendListEventHandler> {
  public static Type<UpdatePlayerFriendListEventHandler> TYPE = new Type<UpdatePlayerFriendListEventHandler>();

  public Type<UpdatePlayerFriendListEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(UpdatePlayerFriendListEventHandler handler) {
    handler.onUpdatePlayerFriendList(this);
  }
}
