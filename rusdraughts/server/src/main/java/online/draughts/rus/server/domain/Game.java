package online.draughts.rus.server.domain;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 16:18
 */
@Entity
public class Game extends PersistableObjectImpl {

  @Index
  private Ref<Player> playerWhite;

  @Index
  private Ref<Player> playerBlack;

  @Index
  private GameEnds playEndStatus;

  private Date playStartDate;

  @Index
  private Date playFinishDate;

  private String notation;

  private String endGameScreenshot;

  private Set<Ref<GameMessage>> gameMessages = new HashSet<>();

  public Game() {
  }

  public Game(Player playerWhiteId,
              Player playerBlackId,
              GameEnds playEndStatus,
              Date playStartDate,
              Date playFinishDate,
              String notation,
              String endGameScreenshot) {
    this.playerWhite = Ref.create(playerWhiteId);
    this.playerBlack = Ref.create(playerBlackId);
    this.playEndStatus = playEndStatus;
    this.playStartDate = playStartDate;
    this.playFinishDate = playFinishDate;
    this.notation = notation;
    this.endGameScreenshot = endGameScreenshot;
  }

  public Player getPlayerWhite() {
    return playerWhite.get();
  }

  public void setPlayerWhite(Player playerWhite) {
    if (playerWhite == null) {
      return;
    }
    this.playerWhite = Ref.create(playerWhite);
  }

  public Player getPlayerBlack() {
    return playerBlack.get();
  }

  public void setPlayerBlack(Player playerBlack) {
    if (playerBlack == null) {
      return;
    }
    this.playerBlack = Ref.create(playerBlack);
  }

  public GameEnds getPlayEndStatus() {
    return playEndStatus;
  }

  public void setPlayEndStatus(GameEnds playEndStatus) {
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

  public Game setEndGameScreenshot(String endGameScreenshot) {
    this.endGameScreenshot = endGameScreenshot;
    return this;
  }

  public Set<Ref<GameMessage>> getGameMessages() {
    return gameMessages;
  }

  public enum GameEnds implements IsSerializable {
    BLACK_WIN,
    WHITE_WIN,
    BLACK_LEFT,
    WHITE_LEFT,
    SURRENDER_BLACK,
    SURRENDER_WHITE,
    DRAW
  }
}
