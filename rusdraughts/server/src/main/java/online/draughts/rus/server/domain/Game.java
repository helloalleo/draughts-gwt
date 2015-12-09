package online.draughts.rus.server.domain;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Text;
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
public class Game extends ModelImpl {

  private Player playerWhite;
  private Player playerBlack;

  private GameDto.GameEnds playEndStatus;

  private Date playStartDate;

//  @Index
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
    setIndexedProperty("playerWhite", playerWhite);
  }

  public Player getPlayerBlack() {
    return playerBlack;
  }

  public void setPlayerBlack(Player playerBlack) {
    this.playerBlack = playerBlack;
    setIndexedProperty("playerBlack", playerBlack);
  }

  public GameDto.GameEnds getPlayEndStatus() {
    return playEndStatus;
  }

  public void setPlayEndStatus(GameDto.GameEnds playEndStatus) {
    this.playEndStatus = playEndStatus;
    getEntiy().setIndexedProperty("playEndStatus", playEndStatus);
  }

  public Date getPlayStartDate() {
    return playStartDate;
  }

  public void setPlayStartDate(Date playStartDate) {
    this.playStartDate = playStartDate;
    getEntiy().setProperty("playStartDate", playStartDate);
  }

  public Date getPlayFinishDate() {
    return playFinishDate;
  }

  public void setPlayFinishDate(Date playEndDate) {
    this.playFinishDate = playEndDate;
    getEntiy().setIndexedProperty("playFinishDate", playFinishDate);
  }

  public String getNotation() {
    return notation;
  }

  public void setNotation(String notation) {
    this.notation = notation;
    getEntiy().setProperty("notation", new Text(notation));
  }

  public String getEndGameScreenshot() {
    return endGameScreenshot;
  }

  public void setEndGameScreenshot(String endGameScreenshot) {
    this.endGameScreenshot = endGameScreenshot;
    getEntiy().setProperty("endGameScreenshot", new Blob(endGameScreenshot.getBytes()));
  }

  public Set<GameMessage> getGameMessages() {
    return gameMessages;
  }
}
