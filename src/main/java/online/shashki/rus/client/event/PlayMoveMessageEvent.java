package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.shashki.rus.shared.dto.MoveDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 10.09.15
 * Time: 10:25
 */
public class PlayMoveMessageEvent extends GwtEvent<PlayMoveMessageEventHandler> {
  public static Type<PlayMoveMessageEventHandler> TYPE = new Type<PlayMoveMessageEventHandler>();
  private final MoveDto move;

  public PlayMoveMessageEvent(MoveDto move) {
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
