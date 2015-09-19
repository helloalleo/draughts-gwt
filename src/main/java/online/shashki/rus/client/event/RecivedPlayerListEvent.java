package online.shashki.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.shashki.rus.shared.model.Shashist;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 11.12.14
 * Time: 0:41
 */
public class RecivedPlayerListEvent extends GwtEvent<RecivedPlayerListEventHandler> {
  public static Type<RecivedPlayerListEventHandler> TYPE = new Type<>();
  private List<Shashist> playerList;

  public RecivedPlayerListEvent(List<Shashist> playerList) {
    this.playerList = playerList;
  }

  public List<Shashist> getPlayerList() {
    return playerList;
  }

  public Type<RecivedPlayerListEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(RecivedPlayerListEventHandler handler) {
    handler.onRecivedPlayerList(this);
  }
}
