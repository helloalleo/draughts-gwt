package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.shared.dto.PlayerDto;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 11.12.14
 * Time: 0:41
 */
public class ReceivedPlayerListEvent extends GwtEvent<ReceivedPlayerListEventHandler> {
  public static Type<ReceivedPlayerListEventHandler> TYPE = new Type<>();
  private List<PlayerDto> playerList;

  public ReceivedPlayerListEvent(List<PlayerDto> playerList) {
    this.playerList = playerList;
  }

  public List<PlayerDto> getPlayerList() {
    return playerList;
  }

  public Type<ReceivedPlayerListEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(ReceivedPlayerListEventHandler handler) {
    handler.onReceivedPlayerList(this);
  }
}
