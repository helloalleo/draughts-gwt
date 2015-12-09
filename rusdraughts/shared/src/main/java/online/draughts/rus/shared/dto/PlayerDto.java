package online.draughts.rus.shared.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 0:36
 */
public class PlayerDto extends BaseDto {

  private String firstName;

  private String lastName;

  private String playerName;

  private int rating = 0;

  private boolean loggedIn;
  private boolean playing;
  private boolean online;

  private boolean subscribed;

  public PlayerDto() {
  }

  public PlayerDto setSubscribed(boolean subscribed) {
    this.subscribed = subscribed;
    return this;
  }

  public boolean isSubscribed() {
    return subscribed;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  public boolean isPlaying() {
    return playing;
  }

  public void setPlaying(boolean playing) {
    this.playing = playing;
  }

  public boolean isOnline() {
    return online;
  }

  public void setOnline(boolean online) {
    this.online = online;
  }

  public int getRating() {
    return rating;
  }

  public PlayerDto setRating(int rating) {
    this.rating = rating;
    return this;
  }

  @JsonIgnore
  public String getPublicName() {
    if (getPlayerName() == null) {
      return getFullName().trim();
    }
    return getPlayerName();
  }

  @JsonIgnore
  private String getFullName() {
    return getFirstName() + " " + getLastName();
  }

  @JsonIgnore
  private String getShortName() {
    if (getPlayerName() == null) {
      return getFirstName();
    }
    return getPlayerName();
  }

  @JsonIgnore
  public String getSiteName() {
    return "";
  }

  /**
   * Обновляем только сеарилизуемые поля
   *
   * @param player
   */
  public enum AuthProvider {
    VK,
    FACEBOOK,
    GOOGLE
  }
}
