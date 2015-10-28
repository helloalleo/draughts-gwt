package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.draughts.Stroke;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 30.11.14
 * Time: 18:11
 */
public class NotationStrokeEvent extends GwtEvent<NotationStrokeEventHandler> {
  public static Type<NotationStrokeEventHandler> TYPE = new Type<>();
  private final Stroke stroke;
  private boolean opponentStroke;

  public NotationStrokeEvent(Stroke stroke, boolean opponentStroke) {
    this.stroke = stroke;
    this.opponentStroke = opponentStroke;
  }

  public Stroke getStroke() {
    return stroke;
  }

  public Type<NotationStrokeEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(NotationStrokeEventHandler handler) {
    handler.onNotationStroke(this);
  }

  public boolean isOpponentStroke() {
    return opponentStroke;
  }
}
