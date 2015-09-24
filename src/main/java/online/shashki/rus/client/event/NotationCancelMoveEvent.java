package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.shashki.rus.shared.dto.MoveDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.09.15
 * Time: 19:49
 */
public class NotationCancelMoveEvent extends GwtEvent<NotationCancelMoveEventHandler> {
  public static Type<NotationCancelMoveEventHandler> TYPE = new Type<NotationCancelMoveEventHandler>();
  private final MoveDto move;

  public NotationCancelMoveEvent(MoveDto move) {
    this.move = move;
  }

  public MoveDto getMove() {
    return move;
  }

  public Type<NotationCancelMoveEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(NotationCancelMoveEventHandler handler) {
    handler.onNotationCancelMove(this);
  }
}
