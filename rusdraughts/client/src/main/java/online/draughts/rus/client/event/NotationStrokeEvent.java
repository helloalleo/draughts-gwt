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

  public NotationStrokeEvent(Stroke stroke) {
    this.stroke = stroke;
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
}
