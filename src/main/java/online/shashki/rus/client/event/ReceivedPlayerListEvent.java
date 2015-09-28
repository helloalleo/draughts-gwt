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
public class ReceivedPlayerListEvent extends GwtEvent<ReceivedPlayerListEventHandler> {
  public static Type<ReceivedPlayerListEventHandler> TYPE = new Type<>();
  private List<Shashist> playerList;

  public ReceivedPlayerListEvent(List<Shashist> playerList) {
    this.playerList = playerList;
  }

  public List<Shashist> getPlayerList() {
    return playerList;
  }

  public Type<ReceivedPlayerListEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(ReceivedPlayerListEventHandler handler) {
    handler.onReceivedPlayerList(this);
  }
}
