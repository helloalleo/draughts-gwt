package online.shashki.rus.shared.model;

import com.google.gwt.user.client.rpc.GwtTransient;
import com.google.inject.Singleton;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 16:18
 */
@Singleton
@Entity
@Table(name = "game")
public class Game extends PersistableObjectImpl {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "player_white_id")
  private Shashist playerWhite;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "player_black_id")
  private Shashist playerBlack;

  @Enumerated(EnumType.STRING)
  @Column(name = "play_end_status")
  private GameEnds playEndStatus;

  @Column(name = "play_start_date")
  private Date playStartDate;

  @Column(name = "play_finish_date")
  private Date playFinishDate;

  @Column(name = "party_notation", length = 1000)
  private String partyNotation;

  @Column(name = "end_game_screenshot", columnDefinition = "TEXT")
  public String endGameScreenshot;

  public Game() {
  }

  public Game(Shashist playerWhite,
              Shashist playerBlack,
              GameEnds playEndStatus,
              Date playStartDate,
              Date playFinishDate,
              String partyNotation,
              String endGameScreenshot) {
    this.playerWhite = playerWhite;
    this.playerBlack = playerBlack;
    this.playEndStatus = playEndStatus;
    this.playStartDate = playStartDate;
    this.playFinishDate = playFinishDate;
    this.partyNotation = partyNotation;
    this.endGameScreenshot = endGameScreenshot;
  }

  public void copyFrom(Game game) {
    if (game.getPlayEndStatus() !=null) {
      setPlayEndStatus(game.getPlayEndStatus());
    }
    if (game.getPlayStartDate() !=null) {
      setPlayStartDate(game.getPlayStartDate());
    }
    if (game.getPlayFinishDate() != null) {
      setPlayFinishDate(game.getPlayFinishDate());
    }
    if (game.getPartyNotation() != null) {
      setPartyNotation(game.getPartyNotation());
    }
    if (game.getEndGameScreenshot() !=null) {
      setEndGameScreenshot(game.getEndGameScreenshot());
    }
  }

  public Shashist getPlayerWhite() {
    return playerWhite;
  }

  public void setPlayerWhite(Shashist playerWhite) {
    this.playerWhite = playerWhite;
  }

  public Shashist getPlayerBlack() {
    return playerBlack;
  }

  public void setPlayerBlack(Shashist playerBlack) {
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

  public String getPartyNotation() {
    return partyNotation;
  }

  public void setPartyNotation(String partyNotation) {
    this.partyNotation = partyNotation;
  }

  public String getEndGameScreenshot() {
    return endGameScreenshot;
  }

  public Game setEndGameScreenshot(String endGameScreenshot) {
    this.endGameScreenshot = endGameScreenshot;
    return this;
  }
}
