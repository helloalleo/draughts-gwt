package online.shashki.rus.shared.model;

import com.google.gwt.user.client.rpc.GwtTransient;
import online.shashki.rus.shared.util.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 0:36
 */
@Entity
@Table(name = "player")
public class Player extends PersistableObjectImpl {

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Column(name = "session_id")
  private String sessionId;

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Column(name = "vk_id")
  private String vkId;

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  private String email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "player_name")
  private String playerName;

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Column(name = "include_in_rating")
  private boolean includeInRating;

  private int rating;

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Column(name = "auth_provider")
  @Enumerated(EnumType.STRING)
  private AuthProvider authProvider;

//  @GwtTransient
//  @JsonIgnore
//  @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.friendOf", cascade = CascadeType.ALL, orphanRemoval = true)
//  private Set<FriendEntity> friends;

//  @GwtTransient
//  @JsonIgnore
//  @Column(name = "fiends_of")
//  @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.friend", cascade = CascadeType.ALL, orphanRemoval = true)
//  private Set<FriendEntity> friendOf;

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @OneToMany(mappedBy = "receiver")
  private Set<GameMessage> receivedPlayerMessages;

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @OneToMany(mappedBy = "sender")
  private Set<GameMessage> sentPlayerMessages;

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @OneToMany(mappedBy = "receiverGame")
  private Set<GameMessage> receivedGameMessages;

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @OneToMany(mappedBy = "senderGame")
  private Set<GameMessage> sentGameMessages;

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @OneToMany(mappedBy = "playerWhite")
  private Set<Game> whiteRoleGames;

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @OneToMany(mappedBy = "playerBlack")
  private Set<Game> blackRoleGames;

  @Column(name = "logged_in")
  private boolean loggedIn;
  private boolean playing;
  private boolean online;
  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Column(name = "register_date")
  private Date registerDate;
  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Column(name = "last_visited")
  private Date lastVisited;
  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Column(name = "visit_counter")
  private int visitCounter;

  public Player() {
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getVkId() {
    return vkId;
  }

  public void setVkId(String vkId) {
    this.vkId = vkId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public AuthProvider getAuthProvider() {
    return authProvider;
  }

  public void setAuthProvider(AuthProvider authProvider) {
    this.authProvider = authProvider;
  }

//  public Set<FriendEntity> getFriends() {
//    return friends;
//  }
//
//  public void setFriends(Set<FriendEntity> friends) {
//    this.friends = friends;
//  }
//
//  public Set<FriendEntity> getFriendOf() {
//    return friendOf;
//  }
//
//  public void setFriendOf(Set<FriendEntity> friendOf) {
//    this.friendOf = friendOf;
//  }

  public Set<GameMessage> getReceivedPlayerMessages() {
    return receivedPlayerMessages;
  }

  public void setReceivedPlayerMessages(Set<GameMessage> playerMessageEntities) {
    this.receivedPlayerMessages = playerMessageEntities;
  }

  public Set<GameMessage> getSentPlayerMessages() {
    return sentPlayerMessages;
  }

  public void setSentPlayerMessages(Set<GameMessage> playerMessageEntities) {
    this.sentPlayerMessages = playerMessageEntities;
  }

  public Set<GameMessage> getReceivedGameMessages() {
    return receivedGameMessages;
  }

  public void setReceivedGameMessages(Set<GameMessage> receivedGameMessages) {
    this.receivedGameMessages = receivedGameMessages;
  }

  public Set<GameMessage> getSentGameMessages() {
    return sentGameMessages;
  }

  public void setSentGameMessages(Set<GameMessage> sentGameMessages) {
    this.sentGameMessages = sentGameMessages;
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

  public Date getRegisterDate() {
    return registerDate;
  }

  public void setRegisterDate(Date registerDate) {
    this.registerDate = registerDate;
  }

  public Date getLastVisited() {
    return lastVisited;
  }

  public void setLastVisited(Date lastVisited) {
    this.lastVisited = lastVisited;
  }

  public int getVisitCounter() {
    return visitCounter;
  }

  public void setVisitCounter(int visitCounter) {
    this.visitCounter = visitCounter;
  }

  public Set<Game> getWhiteRoleGames() {
    return whiteRoleGames;
  }

  public void setWhiteRoleGames(Set<Game> whiteRoleGames) {
    this.whiteRoleGames = whiteRoleGames;
  }

  public Set<Game> getBlackRoleGames() {
    return blackRoleGames;
  }

  public void setBlackRoleGames(Set<Game> blackRoleGames) {
    this.blackRoleGames = blackRoleGames;
  }

  public boolean isIncludeInRating() {
    return includeInRating;
  }

  public Player setIncludeInRating(boolean includeInRating) {
    this.includeInRating = includeInRating;
    return this;
  }

  public int getRating() {
    return rating;
  }

  public Player setRating(int rating) {
    this.rating = rating;
    return this;
  }

  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public String getPublicName() {
    if (getPlayerName() == null) {
      String fullName = getFullName().trim();
      if (fullName.isEmpty()) {
        return getEmail().split("@")[0];
      }
      return fullName;
    }
    return getPlayerName();
  }

  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  public String getFullName() {
    return getFirstName() + " " + getLastName();
  }

  @Override
  public String toString() {
    return "Player{" +
        "sessionId='" + sessionId + '\'' +
        ", vkId='" + vkId + '\'' +
        ", email='" + email + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", playerName='" + playerName + '\'' +
        ", authProvider='" + authProvider + '\'' +
        ", loggedIn=" + loggedIn +
        ", playing=" + playing +
        ", online=" + online +
        ", registerDate=" + registerDate +
        ", lastVisited=" + lastVisited +
        ", visitCounter=" + visitCounter +
        '}';
  }

  /**
   * Обновляем только сеарилизуемые поля
   *
   * @param profile
   */
  public void updateSerializable(Player profile) {
    if (profile == null) {
      return;
    }
    if (StringUtils.isNotEmpty(profile.getSessionId())) {
      this.sessionId = profile.getSessionId();
    }
    this.loggedIn = profile.isLoggedIn();
    this.playing = profile.isPlaying();
    this.online = profile.isOnline();
    if (StringUtils.isNotEmpty(profile.getFirstName())) {
      this.firstName = profile.getFirstName();
    }
    if (StringUtils.isNotEmpty(profile.getLastName())) {
      this.lastName = profile.getLastName();
    }
    if (StringUtils.isNotEmpty(profile.getPlayerName())) {
      this.playerName = profile.getPlayerName();
    }
  }

  public enum AuthProvider {
    VK,
    FACEBOOK,
    GOOGLE,
    WINDOWS
  }
}
