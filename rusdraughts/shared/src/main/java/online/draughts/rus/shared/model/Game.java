package online.draughts.rus.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gwt.user.client.rpc.GwtTransient;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Load;

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

  private Key<Player> playerWhite;

  private Key<Player> playerBlack;

  private GameEnds playEndStatus;

  private Date playStartDate;

  private Date playFinishDate;

  private String notation;

  private String endGameScreenshot;

  @GwtTransient
  @JsonIgnore
  @Load
  private Set<Ref<GameMessage>> gameMessages = new HashSet<>();

  public Game() {
  }

  public Game(Long playerWhiteId,
              Long playerBlackId,
              GameEnds playEndStatus,
              Date playStartDate,
              Date playFinishDate,
              String notation,
              String endGameScreenshot) {
    this.playerWhite = Key.create(Player.class, playerWhiteId);
    this.playerBlack = Key.create(Player.class, playerBlackId);
    this.playEndStatus = playEndStatus;
    this.playStartDate = playStartDate;
    this.playFinishDate = playFinishDate;
    this.notation = notation;
    this.endGameScreenshot = endGameScreenshot;
  }

  public Key<Player> getPlayerWhite() {
    return playerWhite;
  }

  public void setPlayerWhite(Long playerWhite) {
    this.playerWhite = Key.create(Player.class, playerWhite);
  }

  public Key<Player> getPlayerBlack() {
    return playerBlack;
  }

  public void setPlayerBlack(Long playerBlack) {
    this.playerBlack = Key.create(Player.class, playerBlack);
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
