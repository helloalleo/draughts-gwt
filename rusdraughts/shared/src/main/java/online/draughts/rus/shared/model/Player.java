package online.draughts.rus.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gwt.user.client.rpc.GwtTransient;
import online.draughts.rus.shared.util.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
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
  @Column(name = "session_id")
  private String sessionId;

  @GwtTransient
  @JsonIgnore
  @Column(name = "vk_id")
  private String vkId;

  @GwtTransient
  @JsonIgnore
  private String email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "player_name")
  private String playerName;

  private int rating = 0;

  @GwtTransient
  @JsonIgnore
  @Column(name = "game_played")
  private int gamePlayed = 0;

  @GwtTransient
  @JsonIgnore
  @Column(name = "game_win")
  private int gameWin = 0;

  @GwtTransient
  @JsonIgnore
  @Column(name = "game_lose")
  private int gameLose = 0;

  @GwtTransient
  @JsonIgnore
  @Column(name = "game_draw")
  private int gameDraw = 0;

  @GwtTransient
  @JsonIgnore
  @Column(name = "auth_provider")
  @Enumerated(EnumType.STRING)
  private AuthProvider authProvider;

  @GwtTransient
  @JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.friendOf", orphanRemoval = true)
  private Set<Friend> friends = new HashSet<>();

  @GwtTransient
  @JsonIgnore
  @Column(name = "fiends_of")
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.friend", orphanRemoval = true)
  private Set<Friend> friendOf = new HashSet<>();

  @GwtTransient
  @JsonIgnore
  @OneToMany(mappedBy = "receiver")
  private Set<GameMessage> receivedPlayerMessages;

  @GwtTransient
  @JsonIgnore
  @OneToMany(mappedBy = "sender")
  private Set<GameMessage> sentPlayerMessages;

  @GwtTransient
  @JsonIgnore
  @OneToMany(mappedBy = "receiverGame")
  private Set<GameMessage> receivedGameMessages;

  @GwtTransient
  @JsonIgnore
  @OneToMany(mappedBy = "senderGame")
  private Set<GameMessage> sentGameMessages;

  @GwtTransient
  @JsonIgnore
  @OneToMany(mappedBy = "playerWhite")
  private Set<Game> whiteRoleGames;

  @GwtTransient
  @JsonIgnore
  @OneToMany(mappedBy = "playerBlack")
  private Set<Game> blackRoleGames;

  @Column(name = "logged_in")
  private boolean loggedIn;
  private boolean playing;
  private boolean online;

  @GwtTransient
  @JsonIgnore
  @Column(name = "register_date")
  private Date registerDate;
  @GwtTransient
  @JsonIgnore
  @Column(name = "last_visited")
  private Date lastVisited;
  @GwtTransient
  @JsonIgnore
  @Column(name = "visit_counter")
  private int visitCounter = 0;

  @GwtTransient
  @JsonIgnore
  @Column(name = "fb_id")
  private String fbId;

  @GwtTransient
  @JsonIgnore
  @Column(name = "google_sub")
  private String googleSub;

  public Player() {
  }

  public Set<Friend> getFriends() {
    return friends;
  }

  public Player setFriends(Set<Friend> friends) {
    this.friends = friends;
    return this;
  }

  public Set<Friend> getFriendOf() {
    return friendOf;
  }

  public Player setFriendOf(Set<Friend> friendOf) {
    this.friendOf = friendOf;
    return this;
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

  public int getGamePlayed() {
    return gamePlayed;
  }

  public Player setGamePlayed(int gamePlayed) {
    this.gamePlayed = gamePlayed;
    return this;
  }

  public int getGameWin() {
    return gameWin;
  }

  public Player setGameWin(int gameWin) {
    this.gameWin = gameWin;
    return this;
  }

  public int getGameLose() {
    return gameLose;
  }

  public Player setGameLose(int gameLose) {
    this.gameLose = gameLose;
    return this;
  }

  public int getGameDraw() {
    return gameDraw;
  }

  public Player setGameDraw(int gameDraw) {
    this.gameDraw = gameDraw;
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
    return "Player{" +
        "id='" + getId() + '\'' +
        ", sessionId='" + sessionId + '\'' +
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
    this.loggedIn = profile.isLoggedIn();
    this.playing = profile.isPlaying();
    this.online = profile.isOnline();
    if (StringUtils.isNotEmpty(profile.getPlayerName())) {
      this.playerName = profile.getPlayerName();
    }
  }

  public void setFbId(String fbId) {
    this.fbId = fbId;
  }

  public String getFbId() {
    return fbId;
  }

  public Player setGoogleSub(String googleSub) {
    this.googleSub = googleSub;
    return this;
  }

  public String getGoogleSub() {
    return googleSub;
  }

  public enum AuthProvider {
    VK,
    FACEBOOK,
    GOOGLE
  }
}
