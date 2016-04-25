package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.04.16
 * Time: 19:52
 */
public class GameCheckStuckEvent extends GwtEvent<GameCheckStuckEventHandler> {
  public static Type<GameCheckStuckEventHandler> TYPE = new Type<GameCheckStuckEventHandler>();

  public Type<GameCheckStuckEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(GameCheckStuckEventHandler handler) {
    handler.onGameCheckStuck(this);
  }
}
