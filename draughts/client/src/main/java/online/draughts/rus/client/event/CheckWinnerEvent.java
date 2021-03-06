package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.01.15
 * Time: 20:53
 */
public class CheckWinnerEvent extends GwtEvent<CheckWinnerEventHandler> {
  public static Type<CheckWinnerEventHandler> TYPE = new Type<CheckWinnerEventHandler>();

  private boolean player;

  public CheckWinnerEvent(boolean player) {
    this.player = player;
  }

  public boolean isPlayer() {
    return player;
  }

  public Type<CheckWinnerEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(CheckWinnerEventHandler handler) {
    handler.onCheckWinner(this);
  }
}
