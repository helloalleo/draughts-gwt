package online.draughts.rus.server.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Stringify;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.util.LongStringifier;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 0:36
 */
@Entity
public class Player extends PersistableObjectImpl {

  @Index
  private String sessionId;

  @Index
  private String vkId;

  @Index
  private String fbId;

  @Index
  private String googleSub;

  @Index
  private String email;

  @Index
  private String firstName;

  @Index
  private String lastName;

  @Index
  private String playerName;

  @Index
  private int rating = 0;

  private int gamePlayed = 0;

  private int gameWin = 0;

  private int gameLose = 0;

  private int gameDraw = 0;

  @Index
  private AuthProvider authProvider;

  private Set<Ref<Friend>> friends = new HashSet<>();

  private Set<Ref<Friend>> friendOf = new HashSet<>();

  private Set<Ref<GameMessage>> receivedPlayerMessages = new HashSet<>();

  private Set<Ref<GameMessage>> sentPlayerMessages = new HashSet<>();

  private Set<Ref<Game>> whiteRoleGames = new HashSet<>();

  private Set<Ref<Game>> blackRoleGames = new HashSet<>();

  private boolean loggedIn;
  private boolean playing;
  @Index
  private boolean online;

  @Stringify(LongStringifier.class)
  private Map<Long, Integer> friendUnreadMessagesMap = new HashMap<>();

  @Index
  private Date registerDate;
  @Index
  private Date lastVisited;
  @Index
  private int visitCounter = 0;

  @Index
  private boolean subscribed;

  @Index
  private boolean active;

  public Player() {
  }

  public boolean getSubscribed() {
    return subscribed;
  }

  public void setSubscribed(boolean subscribed) {
    this.subscribed = subscribed;
  }

  public boolean isSubscribed() {
    return subscribed;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
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

  public Map<Long, Integer> getFriendUnreadMessagesMap() {
    return friendUnreadMessagesMap;
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

  public int getGamePlayed() {
    return gamePlayed;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public void setGamePlayed(int gamePlayed) {
    this.gamePlayed = gamePlayed;
  }

  public int getGameWin() {
    return gameWin;
  }

  public void setGameWin(int gameWin) {
    this.gameWin = gameWin;
  }

  public int getGameLose() {
    return gameLose;
  }

  public void setGameLose(int gameLose) {
    this.gameLose = gameLose;
  }

  public int getGameDraw() {
    return gameDraw;
  }

  public void setGameDraw(int gameDraw) {
    this.gameDraw = gameDraw;
  }

  public String getPublicName() {
    if (getPlayerName() == null) {
      return getFullName().trim();
    }
    return getPlayerName();
  }

  private String getFullName() {
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
   * @param player
   */
  public void updateSerializable(PlayerDto player) {
    if (player == null) {
      return;
    }
    this.playerName = player.getPlayerName();
  }

  public void setFbId(String fbId) {
    this.fbId = fbId;
  }

  public String getFbId() {
    return fbId;
  }

  public void setGoogleSub(String googleSub) {
    this.googleSub = googleSub;
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
