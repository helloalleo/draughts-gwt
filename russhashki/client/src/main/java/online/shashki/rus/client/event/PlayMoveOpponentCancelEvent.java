package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.shashki.rus.shared.model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 10.09.15
 * Time: 15:48
 */
public class PlayMoveOpponentCancelEvent extends GwtEvent<PlayMoveOpponentCancelEventHandler> {
  public static Type<PlayMoveOpponentCancelEventHandler> TYPE = new Type<PlayMoveOpponentCancelEventHandler>();
  private final Move move;

  public PlayMoveOpponentCancelEvent(Move move) {
    this.move = move;
  }

  public Move getMove() {
    return move;
  }

  public Type<PlayMoveOpponentCancelEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(PlayMoveOpponentCancelEventHandler handler) {
    handler.onPlayMoveOpponentCancel(this);
  }
}
