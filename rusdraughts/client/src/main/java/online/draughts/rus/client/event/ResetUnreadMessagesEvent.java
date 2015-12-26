package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.shared.dto.PlayerDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 26.12.15
 * Time: 11:53
 */
public class ResetUnreadMessagesEvent extends GwtEvent<ResetUnreadMessagesEventHandler> {
  public static Type<ResetUnreadMessagesEventHandler> TYPE = new Type<ResetUnreadMessagesEventHandler>();
  private PlayerDto friend;

  public ResetUnreadMessagesEvent(PlayerDto friend) {
    this.friend = friend;
  }

  public Type<ResetUnreadMessagesEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(ResetUnreadMessagesEventHandler handler) {
    handler.onResetUnreadMessages(this);
  }

  public PlayerDto getFriend() {
    return friend;
  }

  public void setFriend(PlayerDto friend) {
    this.friend = friend;
  }
}
