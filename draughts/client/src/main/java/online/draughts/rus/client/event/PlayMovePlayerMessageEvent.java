package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.shared.dto.MoveDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 10.09.15
 * Time: 10:25
 */
public class PlayMovePlayerMessageEvent extends GwtEvent<PlayMoveMessageEventHandler> {
  public static Type<PlayMoveMessageEventHandler> TYPE = new Type<PlayMoveMessageEventHandler>();
  private final MoveDto move;

  public PlayMovePlayerMessageEvent(MoveDto move) {
    this.move = move;
  }

  public MoveDto getMove() {
    return move;
  }

  public Type<PlayMoveMessageEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(PlayMoveMessageEventHandler handler) {
    handler.onPlayMoveMessage(this);
  }
}
