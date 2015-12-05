package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.shared.dto.MoveDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.01.15
 * Time: 17:11
 */
public class PlayMoveCancelEvent extends GwtEvent<PlayMoveCancelEventHandler> {

  public static Type<PlayMoveCancelEventHandler> TYPE = new Type<>();
  private final MoveDto move;

  public PlayMoveCancelEvent(MoveDto move) {
    this.move = move;
  }

  public MoveDto getMove() {
    return move;
  }

  public Type<PlayMoveCancelEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(PlayMoveCancelEventHandler handler) {
    handler.onPlayMove(this);
  }

}
