package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.08.15
 * Time: 11:55
 */
public class HideInviteDialogBoxEvent extends GwtEvent<HideInviteDialogBoxEventHandler> {
  public static Type<HideInviteDialogBoxEventHandler> TYPE = new Type<HideInviteDialogBoxEventHandler>();

  public Type<HideInviteDialogBoxEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(HideInviteDialogBoxEventHandler handler) {
    handler.onHideInviteDialogBox(this);
  }
}
