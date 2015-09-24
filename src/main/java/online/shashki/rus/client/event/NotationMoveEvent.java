package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.shashki.rus.shared.model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 30.11.14
 * Time: 18:11
 */
public class NotationMoveEvent extends GwtEvent<NotationMoveEventHandler> {
  public static Type<NotationMoveEventHandler> TYPE = new Type<>();
  private final Move move;

  public NotationMoveEvent(Move move) {
    this.move = move;
  }

  public Move getMove() {
    return move;
  }

  public Type<NotationMoveEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(NotationMoveEventHandler handler) {
    handler.onNotationMove(this);
  }
}
