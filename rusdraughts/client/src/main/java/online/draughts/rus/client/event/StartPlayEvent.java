package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 1:37
 */
public class StartPlayEvent extends GwtEvent<StartPlayEventHandler> {
  public static Type<StartPlayEventHandler> TYPE = new Type<>();
  private final boolean white;

  public StartPlayEvent(boolean white) {
    this.white = white;
  }

  public boolean isWhite() {
    return white;
  }

  public Type<StartPlayEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(StartPlayEventHandler handler) {
    handler.onStartPlay(this);
  }
}
