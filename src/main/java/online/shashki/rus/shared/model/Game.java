package online.shashki.rus.shared.model;

import com.google.gwt.user.client.rpc.GwtTransient;
import com.google.inject.Inject;
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

  @GwtTransient
  @JsonIgnore
  @Column(name = "play_start_date")
  private Date playStartDate;

  @Column(name = "play_end_date")
  private Date playEndDate;

  @Column(name = "party_notation", length = 1000)
  private String partyNotation;

  @Column(name = "end_game_screenshot")
  public String endGameScreenshot;

  @Inject
  public Game() {
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

  public Date getPlayEndDate() {
    return playEndDate;
  }

  public void setPlayEndDate(Date playEndDate) {
    this.playEndDate = playEndDate;
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
