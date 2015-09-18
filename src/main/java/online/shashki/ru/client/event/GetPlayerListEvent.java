package online.shashki.ru.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.shashki.ru.shared.model.Shashist;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 11.12.14
 * Time: 0:41
 */
public class GetPlayerListEvent extends GwtEvent<GetPlayerListEventHandler> {
  public static Type<GetPlayerListEventHandler> TYPE = new Type<>();
  private List<Shashist> playerList;

  public GetPlayerListEvent(List<Shashist> playerList) {
    this.playerList = playerList;
  }

  public List<Shashist> getPlayerList() {
    return playerList;
  }

  public Type<GetPlayerListEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(GetPlayerListEventHandler handler) {
    handler.onGetPlayerList(this);
  }
}
