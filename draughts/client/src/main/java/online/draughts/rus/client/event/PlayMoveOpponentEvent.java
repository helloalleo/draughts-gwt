package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.shared.dto.MoveDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.01.15
 * Time: 19:46
 */
public class PlayMoveOpponentEvent extends GwtEvent<PlayMoveOpponentEventHandler> {
  public static Type<PlayMoveOpponentEventHandler> TYPE = new Type<>();
  private final Integer opponentTimeSeconds;
  private MoveDto move;

  public PlayMoveOpponentEvent(MoveDto move, Integer data) {
    this.move = move;
    this.opponentTimeSeconds = data;
  }

  public Integer getOpponentTimeSeconds() {
    return opponentTimeSeconds;
  }

  public MoveDto getMove() {
    return move;
  }

  public Type<PlayMoveOpponentEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(PlayMoveOpponentEventHandler handler) {
    handler.onPlayMoveOpponent(this);
  }
}
