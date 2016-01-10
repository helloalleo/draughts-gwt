package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.shared.dto.MoveDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 10.09.15
 * Time: 15:48
 */
public class PlayMoveOpponentCancelEvent extends GwtEvent<PlayMoveOpponentCancelEventHandler> {
  public static Type<PlayMoveOpponentCancelEventHandler> TYPE = new Type<PlayMoveOpponentCancelEventHandler>();
  private final MoveDto move;

  public PlayMoveOpponentCancelEvent(MoveDto move) {
    this.move = move;
  }

  public MoveDto getMove() {
    return move;
  }

  public Type<PlayMoveOpponentCancelEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(PlayMoveOpponentCancelEventHandler handler) {
    handler.onPlayMoveOpponentCancel(this);
  }
}
