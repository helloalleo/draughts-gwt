package online.shashki.rus.shared.model.entity;

import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.GameEnds;
import online.shashki.rus.shared.model.Shashist;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 16:18
 */
@Entity
@Table(name = "game")
public class GameEntity extends PersistableObjectImpl implements Game {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "player_white_id")
  private ShashistEntity playerWhite;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "player_black_id")
  private ShashistEntity playerBlack;

  @Enumerated(EnumType.STRING)
  @Column(name = "play_end_status")
  private GameEnds playEndStatus;

  @Column(name = "play_start_date")
  private Date playStartDate;

  @Column(name = "play_end_date")
  private Date playEndDate;

  @Column(name = "party_notation", length = 1000)
  private String partyNotation;

  @Override
  public Shashist getPlayerWhite() {
    return playerWhite;
  }

  @Override
  public void setPlayerWhite(Shashist playerWhite) {

  }

  public void setPlayerWhite(ShashistEntity playerWhite) {
    this.playerWhite = playerWhite;
  }

  @Override
  public Shashist getPlayerBlack() {
    return playerBlack;
  }

  @Override
  public void setPlayerBlack(Shashist playerBlack) {

  }

  public void setPlayerBlack(ShashistEntity playerBlack) {
    this.playerBlack = playerBlack;
  }

  @Override
  public GameEnds getPlayEndStatus() {
    return playEndStatus;
  }

  @Override
  public void setPlayEndStatus(GameEnds playEndStatus) {
    this.playEndStatus = playEndStatus;
  }

  @Override
  public Date getPlayStartDate() {
    return playStartDate;
  }

  @Override
  public void setPlayStartDate(Date playStartDate) {
    this.playStartDate = playStartDate;
  }

  @Override
  public Date getPlayEndDate() {
    return playEndDate;
  }

  @Override
  public void setPlayEndDate(Date playEndDate) {
    this.playEndDate = playEndDate;
  }

  @Override
  public String getPartyNotation() {
    return partyNotation;
  }

  @Override
  public void setPartyNotation(String partyNotation) {
    this.partyNotation = partyNotation;
  }

  public GameEntity copy(Game game) {
    this.setPartyNotation(game.getPartyNotation());
    this.setPlayEndDate(game.getPlayEndDate());
    this.setPlayEndStatus(game.getPlayEndStatus());
    this.setPlayStartDate(game.getPlayStartDate());
    return this;
  }
}
