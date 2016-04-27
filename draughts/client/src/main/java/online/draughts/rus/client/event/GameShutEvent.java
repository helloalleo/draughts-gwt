package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.04.16
 * Time: 18:36
 */
public class GameShutEvent extends GwtEvent<GameShutEventHandler> {
  public static Type<GameShutEventHandler> TYPE = new Type<GameShutEventHandler>();

  private boolean white;

  public GameShutEvent(boolean white) {
    this.white = white;
  }

  public boolean isWhite() {
    return white;
  }

  public Type<GameShutEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(GameShutEventHandler handler) {
    handler.onGameShut(this);
  }
}
