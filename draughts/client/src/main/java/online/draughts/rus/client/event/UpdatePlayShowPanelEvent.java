package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.10.15
 * Time: 8:38
 */
public class UpdatePlayShowPanelEvent extends GwtEvent<UpdatePlayShowPanelEventHandler> {
  public static Type<UpdatePlayShowPanelEventHandler> TYPE = new Type<UpdatePlayShowPanelEventHandler>();

  public Type<UpdatePlayShowPanelEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(UpdatePlayShowPanelEventHandler handler) {
    handler.onUpdatePlayShowPanel(this);
  }
}
