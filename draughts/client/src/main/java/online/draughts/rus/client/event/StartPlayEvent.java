package online.draughts.rus.client.event;

import com.google.gwt.event.shared.GwtEvent;
import online.draughts.rus.shared.dto.GameDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.12.14
 * Time: 1:37
 */
public class StartPlayEvent extends GwtEvent<StartPlayEventHandler> {
  public static Type<StartPlayEventHandler> TYPE = new Type<>();
  private final GameDto.GameType gameType;
  private final boolean white;
  private final boolean invitee;
  private final int timeOnPlay;
  private final int fisherTime;

  public StartPlayEvent(GameDto.GameType gameType, boolean white, boolean invitee, int timeOnPlay, int fisherTime) {
    this.gameType = gameType;
    this.white = white;
    this.invitee = invitee;
    this.timeOnPlay = timeOnPlay;
    this.fisherTime = fisherTime;
  }

  public GameDto.GameType getGameType() {
    return gameType;
  }

  public boolean isWhite() {
    return white;
  }

  public boolean isInvitee() {
    return invitee;
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
