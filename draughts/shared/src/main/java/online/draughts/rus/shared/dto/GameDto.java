package online.draughts.rus.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 16:18
 */
public class GameDto extends BaseDto {

  private PlayerDto playerWhite;

  private PlayerDto playerBlack;

  private GameType gameType;

  private GameEnd playEndStatus;

  private Date playStartDate;

  private Date playFinishDate;

  private String notation;

  private String endGameScreenshot;
  private String endGameScreenshotUrl;

  private String currentStateScreenshot;
  private String currentStateScreenshotUrl;

  private Set<DraughtDto> initialPos = new HashSet<>();

  private boolean publish;
  // указывает является ли игра сохраненной
  private boolean gameSnapshot;

  private boolean deleted;

  public GameDto() {
  }

  public GameDto(PlayerDto playerWhite,
                 PlayerDto playerBlack,
                 GameEnd playEndStatus,
                 Date playStartDate,
                 Date playFinishDate,
                 String notation,
                 String endGameScreenshot) {
    this.playerWhite = playerWhite;
    this.playerBlack = playerBlack;
    this.playEndStatus = playEndStatus;
    this.playStartDate = playStartDate;
    this.playFinishDate = playFinishDate;
    this.notation = notation;
    this.endGameScreenshot = endGameScreenshot;
  }

  public PlayerDto getPlayerWhite() {
    return playerWhite;
  }

  public void setPlayerWhite(PlayerDto playerWhite) {
    this.playerWhite = playerWhite;
  }

  public PlayerDto getPlayerBlack() {
    return playerBlack;
  }

  public void setPlayerBlack(PlayerDto playerBlack) {
    this.playerBlack = playerBlack;
  }

  public GameType getGameType() {
    return gameType;
  }

  public void setGameType(GameType gameType) {
    this.gameType = gameType;
  }

  public GameEnd getPlayEndStatus() {
    return playEndStatus;
  }

  public void setPlayEndStatus(GameEnd playEndStatus) {
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

  public void setNotation(String partyNotation) {
    this.notation = partyNotation;
  }

  public String getEndGameScreenshot() {
    return endGameScreenshot;
  }

  public GameDto setEndGameScreenshot(String endGameScreenshot) {
    this.endGameScreenshot = endGameScreenshot;
    return this;
  }

  public Set<DraughtDto> getInitialPos() {
    return initialPos;
  }

  public void setInitialPos(Set<DraughtDto> initialPos) {
    this.initialPos = initialPos;
  }

  public String getEndGameScreenshotFullUrl(String baseUrl) {
    return baseUrl + endGameScreenshotUrl;
  }

  public void setEndGameScreenshotUrl(String endGameScreenshotUrl) {
    this.endGameScreenshotUrl = endGameScreenshotUrl;
  }

  public String getEndGameScreenshotUrl() {
    return endGameScreenshotUrl;
  }

  public void setCurrentStateScreenshot(String currentStateScreenshot) {
    this.currentStateScreenshot = currentStateScreenshot;
  }

  public String getCurrentStateScreenshot() {
    return currentStateScreenshot;
  }

  public String getCurrentStateScreenshotUrl() {
    return currentStateScreenshotUrl;
  }

  public void setCurrentStateScreenshotUrl(String currentStateScreenshotUrl) {
    this.currentStateScreenshotUrl = currentStateScreenshotUrl;
  }

  public String getCurrentStateScreenshotFullUrl(String baseUrl) {
    return baseUrl + currentStateScreenshotUrl;
  }

  public boolean getPublish() {
    return publish;
  }

  public void setPublish(boolean publish) {
    this.publish = publish;
  }

  public boolean isGameSnapshot() {
    return gameSnapshot;
  }

  public void setGameSnapshot(boolean gameSnapshot) {
    this.gameSnapshot = gameSnapshot;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public enum GameType {
    DRAUGHTS,
    GIVEAWAY
  }

  public enum GameEnd implements IsSerializable {
    BLACK_WIN,
    WHITE_WIN,
    BLACK_LEFT,
    WHITE_LEFT,
    SURRENDER_BLACK,
    SURRENDER_WHITE,
    DRAW
  }
}
