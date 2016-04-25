package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.04.16
 * Time: 18:36
 */
public class GameStuckEvent extends GwtEvent<GameStuckEventHandler> {
  public static Type<GameStuckEventHandler> TYPE = new Type<GameStuckEventHandler>();

  private boolean white;

  public GameStuckEvent(boolean white) {
    this.white = white;
  }

  public boolean isWhite() {
    return white;
  }

  public Type<GameStuckEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(GameStuckEventHandler handler) {
    handler.onGameStuck(this);
  }
}
