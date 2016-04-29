package online.draughts.rus.shared.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 0:36
 */
public class PlayerDto extends BaseDto {

  private String sessionId;
  private String notificationsUserId;

  private String firstName;
  private String lastName;
  private String playerName;

  private String avatar;

  private int rating = 0;

  private boolean loggedIn;
  private boolean playing;
  private boolean online;

  private boolean subscribed;
  private boolean moderator;
  private boolean admin;
  private boolean subscribeOnNewsletter;

  public PlayerDto() {
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getNotificationsUserId() {
    return notificationsUserId;
  }

  public void setNotificationsUserId(String notificationsUserId) {
    this.notificationsUserId = notificationsUserId;
  }

  public PlayerDto setSubscribed(boolean subscribed) {
    this.subscribed = subscribed;
    return this;
  }

  public boolean isSubscribed() {
    return subscribed;
  }

  public boolean isModerator() {
    return moderator;
  }

  public PlayerDto setModerator(boolean moderator) {
    this.moderator = moderator;
    return this;
  }

  public boolean isAdmin() {
    return admin;
  }

  public PlayerDto setAdmin(boolean admin) {
    this.admin = admin;
    return this;
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

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
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

  public boolean isSubscribeOnNewsletter() {
    return subscribeOnNewsletter;
  }

  public void setSubscribeOnNewsletter(boolean subscribeOnNewsletter) {
    this.subscribeOnNewsletter = subscribeOnNewsletter;
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

  public void updateSerializable(PlayerDto playerDto) {
    setLoggedIn(playerDto.isLoggedIn());
    setPlaying(playerDto.isPlaying());
    setOnline(playerDto.isOnline());
    setSubscribed(playerDto.isSubscribed());
    setRating(playerDto.getRating());
  }

  /**
   * Обновляем только сеарилизуемые поля
   */
  public enum AuthProvider {
    VK,
    FACEBOOK,
    GOOGLE
  }
}
