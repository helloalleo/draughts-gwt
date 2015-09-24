package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.shashki.rus.shared.model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.09.15
 * Time: 19:49
 */
public class NotationCancelMoveEvent extends GwtEvent<NotationCancelMoveEventHandler> {
  public static Type<NotationCancelMoveEventHandler> TYPE = new Type<NotationCancelMoveEventHandler>();
  private final Move move;

  public NotationCancelMoveEvent(Move move) {
    this.move = move;
  }

  public Move getMove() {
    return move;
  }

  public Type<NotationCancelMoveEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(NotationCancelMoveEventHandler handler) {
    handler.onNotationCancelMove(this);
  }
}
