package online.shashki.ru.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 10.01.15
 * Time: 1:14
 */
public class ClearNotationEvent extends GwtEvent<ClearNotationEventHandler> {
  public static Type<ClearNotationEventHandler> TYPE = new Type<ClearNotationEventHandler>();

  public Type<ClearNotationEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(ClearNotationEventHandler handler) {
    handler.onClearNotation(this);
  }
}
