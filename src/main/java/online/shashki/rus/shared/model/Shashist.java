package online.shashki.rus.shared.model;

import com.google.gwt.user.client.rpc.GwtTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 0:36
 */
@Entity
@Table(name = "shashist")
public class Shashist extends PersistableObjectImpl {

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Column(name = "session_id")
  private String sessionId;

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Column(name = "vk_uid")
  private String vkUid;

  @GwtTransient
  @JsonIgnore
  @com.fasterxml.jackson.annotation.JsonIgnore
  @Email
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
  private String authProvider;

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

  public Shashist() {
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getVkUid() {
    return vkUid;
  }

  public void setVkUid(String vkUid) {
    this.vkUid = vkUid;
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

  public String getAuthProvider() {
    return authProvider;
  }

  public void setAuthProvider(String authProvider) {
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

  public Shashist setIncludeInRating(boolean includeInRating) {
    this.includeInRating = includeInRating;
    return this;
  }

  public int getRating() {
    return rating;
  }

  public Shashist setRating(int rating) {
    this.rating = rating;
    return this;
  }

  @JsonIgnore
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
  public String getFullName() {
    return getFirstName() + " " + getLastName();
  }

  @Override
  public String toString() {
    return "Shashist{" +
        "sessionId='" + sessionId + '\'' +
        ", vkUid='" + vkUid + '\'' +
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
  public void updateSerializable(Shashist profile) {
    if (profile == null) {
      return;
    }
    if (profile.getSessionId() != null) {
      this.sessionId = profile.getSessionId();
    }
    this.loggedIn = profile.isLoggedIn();
    this.playing = profile.isPlaying();
    this.online = profile.isOnline();
    if (profile.getFirstName() != null) {
      this.firstName = profile.getFirstName();
    }
    if (profile.getLastName() != null) {
      this.lastName = profile.getLastName();
    }
    if (profile.getPlayerName() != null && !profile.getPlayerName().isEmpty()) {
      this.playerName = profile.getPlayerName();
    }
  }
}
