package online.draughts.rus.client.application.common;

import online.draughts.rus.shared.dto.GameDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.05.16
 * Time: 0:21
 */
public class InviteData {

  private GameDto.GameType gameType;
  private boolean white;
  private int timeOnPlay;
  private int fisherTime;

  public GameDto.GameType getGameType() {
    return gameType;
  }

  public void setGameType(GameDto.GameType gameType) {
    this.gameType = gameType;
  }

  public boolean isWhite() {
    return white;
  }

  public void setWhite(boolean white) {
    this.white = white;
  }

  public int getTimeOnPlay() {
    return timeOnPlay;
  }

  public void setTimeOnPlay(int timeOnPlay) {
    this.timeOnPlay = timeOnPlay;
  }

  public int getFisherTime() {
    return fisherTime;
  }

  public void setFisherTime(int fisherTime) {
    this.fisherTime = fisherTime;
  }
}