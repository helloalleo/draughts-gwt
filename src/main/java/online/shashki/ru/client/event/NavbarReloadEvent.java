package online.shashki.ru.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 23.11.14
 * Time: 23:31
 */
public class NavbarReloadEvent extends GwtEvent<NavbarReloadEventHandler> {
  public static Type<NavbarReloadEventHandler> TYPE = new Type<>();
  private String token;

  public NavbarReloadEvent(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  @Override
  public Type<NavbarReloadEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(NavbarReloadEventHandler handler) {
    handler.onEvent(this);
  }
}
