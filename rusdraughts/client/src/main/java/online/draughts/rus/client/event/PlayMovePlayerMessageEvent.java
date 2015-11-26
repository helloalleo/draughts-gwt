package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.shared.model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 10.09.15
 * Time: 10:25
 */
public class PlayMovePlayerMessageEvent extends GwtEvent<PlayMoveMessageEventHandler> {
  public static Type<PlayMoveMessageEventHandler> TYPE = new Type<PlayMoveMessageEventHandler>();
  private final Move move;

  public PlayMovePlayerMessageEvent(Move move) {
    this.move = move;
  }

  public Move getMove() {
    return move;
  }

  public Type<PlayMoveMessageEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(PlayMoveMessageEventHandler handler) {
    handler.onPlayMoveMessage(this);
  }
}