package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.01.15
 * Time: 9:48
 */
public class TurnChangeEvent extends GwtEvent<TurnChangeEventHandler> {

  public static Type<TurnChangeEventHandler> TYPE = new Type<>();
  private boolean myTurn;

  public TurnChangeEvent(boolean myTurn) {
    this.myTurn = myTurn;
  }

  public boolean isMyTurn() {
    return myTurn;
  }

  public Type<TurnChangeEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(TurnChangeEventHandler handler) {
    handler.onTurnChange(this);
  }

}
