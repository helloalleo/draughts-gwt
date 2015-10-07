package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.shashki.rus.shared.model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.01.15
 * Time: 17:11
 */
public class PlayMoveCancelEvent extends GwtEvent<PlayMoveCancelEventHandler> {

  public static Type<PlayMoveCancelEventHandler> TYPE = new Type<>();
  private final Move move;

  public PlayMoveCancelEvent(Move move) {
    this.move = move;
  }

  public Move getMove() {
    return move;
  }

  public Type<PlayMoveCancelEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(PlayMoveCancelEventHandler handler) {
    handler.onPlayMove(this);
  }

}
