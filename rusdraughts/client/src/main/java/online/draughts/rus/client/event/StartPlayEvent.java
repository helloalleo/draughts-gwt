package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 1:37
 */
public class StartPlayEvent extends GwtEvent<StartPlayEventHandler> {
  public static Type<StartPlayEventHandler> TYPE = new Type<>();
  private final boolean white;
  private final boolean inviter;
  private final int timeOnPlay;
  private final int fisherTime;

  public StartPlayEvent(boolean white, boolean inviter, int timeOnPlay, int fisherTime) {
    this.white = white;
    this.inviter = inviter;
    this.timeOnPlay = timeOnPlay;
    this.fisherTime = fisherTime;
  }

  public boolean isWhite() {
    return white;
  }

  public boolean isInviter() {
    return inviter;
  }

  public int getTimeOnPlay() {
    return timeOnPlay;
  }

  public Type<StartPlayEventHandler> getAssociatedType() {
    return TYPE;
  }

  protected void dispatch(StartPlayEventHandler handler) {
    handler.onStartPlay(this);
  }

  public int getFisherTime() {
    return fisherTime;
  }
}
