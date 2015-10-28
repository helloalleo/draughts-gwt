package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.draughts.Stroke;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.09.15
 * Time: 19:49
 */
public class NotationCancelStrokeEvent extends GwtEvent<NotationCancelStrokeEventHandler> {
  public static Type<NotationCancelStrokeEventHandler> TYPE = new Type<NotationCancelStrokeEventHandler>();
  private final Stroke stroke;

  public NotationCancelStrokeEvent(Stroke stroke) {
    this.stroke = stroke;
  }

  public Stroke getStroke() {
    return stroke;
  }

  public Type<NotationCancelStrokeEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(NotationCancelStrokeEventHandler handler) {
    handler.onNotationCancelStroke(this);
  }
}
