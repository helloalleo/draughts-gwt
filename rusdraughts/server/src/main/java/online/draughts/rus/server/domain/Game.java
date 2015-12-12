package online.draughts.rus.server.domain;

import online.draughts.rus.server.annotation.Index;
import online.draughts.rus.shared.dto.GameDto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 16:18
 */
public class Game extends ModelImpl<Game> {

  @Index
  private Player playerWhite;
  @Index
  private Player playerBlack;

  @Index
  private GameDto.GameEnds playEndStatus;

  private Date playStartDate;

  @Index
  private Date playFinishDate;

  private String notation;

  private String endGameScreenshot;

  private Set<GameMessage> gameMessages = new HashSet<>();

  public Game() {
    super(Game.class);
  }

  public Game(Player playerWhiteId,
              Player playerBlackId,
              GameDto.GameEnds playEndStatus,
              Date playStartDate,
              Date playFinishDate,
              String notation,
              String endGameScreenshot) {
    super(Game.class);
    this.playerWhite = playerWhiteId;
    this.playerBlack = playerBlackId;
    this.playEndStatus = playEndStatus;
    this.playStartDate = playStartDate;
    this.playFinishDate = playFinishDate;
    this.notation = notation;
    this.endGameScreenshot = endGameScreenshot;
  }

  public Player getPlayerWhite() {
    return playerWhite;
  }

  public void setPlayerWhite(Player playerWhite) {
    this.playerWhite = playerWhite;
  }

  public Player getPlayerBlack() {
    return playerBlack;
  }

  public void setPlayerBlack(Player playerBlack) {
    this.playerBlack = playerBlack;
  }

  public GameDto.GameEnds getPlayEndStatus() {
    return playEndStatus;
  }

  public void setPlayEndStatus(GameDto.GameEnds playEndStatus) {
    this.playEndStatus = playEndStatus;
  }

  public Date getPlayStartDate() {
    return playStartDate;
  }

  public void setPlayStartDate(Date playStartDate) {
    this.playStartDate = playStartDate;
  }

  public Date getPlayFinishDate() {
    return playFinishDate;
  }

  public void setPlayFinishDate(Date playEndDate) {
    this.playFinishDate = playEndDate;
  }

  public String getNotation() {
    return notation;
  }

  public void setNotation(String notation) {
    this.notation = notation;
  }

  public String getEndGameScreenshot() {
    return endGameScreenshot;
  }

  public void setEndGameScreenshot(String endGameScreenshot) {
    this.endGameScreenshot = endGameScreenshot;
  }

  public Set<GameMessage> getGameMessages() {
    return gameMessages;
  }
}
