package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.shared.model.GameMessage;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 13.12.14
 * Time: 12:38
 */
public class GameMessageEvent extends GwtEvent<GameMessageEventHandler> {
  public static Type<GameMessageEventHandler> TYPE = new Type<>();
  private final GameMessage gameMessage;

  public GameMessageEvent(GameMessage gameMessage) {
    this.gameMessage = gameMessage;
  }

  public GameMessage getGameMessage() {
    return gameMessage;
  }

  public Type<GameMessageEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(GameMessageEventHandler handler) {
    handler.onPlayerMessage(this);
  }
}
