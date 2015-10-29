package online.draughts.rus.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.persistence.*;
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
@Table(name = "game")
public class Game extends PersistableObjectImpl {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "player_white_id")
  private Player playerWhite;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "player_black_id")
  private Player playerBlack;

  @Column(name = "play_end_status")
  @Enumerated(EnumType.STRING)
  private GameEnds playEndStatus;

  @Column(name = "play_start_date")
  private Date playStartDate;

  @Column(name = "play_finish_date")
  private Date playFinishDate;

  @Column(name = "notation", length = 1000)
  private String notation;

  @Column(name = "end_game_screenshot", columnDefinition = "TEXT")
  private String endGameScreenshot;

  // удаление выполняется вручную
  @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
  private Set<GameMessage> gameMessages = new HashSet<>();

  public Game() {
  }

  public Game(Player playerWhite,
              Player playerBlack,
              GameEnds playEndStatus,
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

  public Set<GameMessage> getGameMessages() {
    return gameMessages;
  }

  public Game setGameMessages(Set<GameMessage> gameMessages) {
    this.gameMessages = gameMessages;
    return this;
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
