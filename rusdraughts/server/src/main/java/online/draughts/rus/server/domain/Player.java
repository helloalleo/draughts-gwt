package online.draughts.rus.server.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Stringify;
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

  private String sessionId;

  private String vkId;

  private String email;

  private String firstName;

  private String lastName;

  private String playerName;

  private int rating = 0;

  private int gamePlayed = 0;

  private int gameWin = 0;

  private int gameLose = 0;

  private int gameDraw = 0;

  private AuthProvider authProvider;

  private Set<Ref<Friend>> friends = new HashSet<>();

  private Set<Ref<Friend>> friendOf = new HashSet<>();

  private Set<Ref<GameMessage>> receivedPlayerMessages;

  private Set<Ref<GameMessage>> sentPlayerMessages;

  private Set<Ref<Game>> whiteRoleGames;

  private Set<Ref<Game>> blackRoleGames;

  private boolean loggedIn;
  private boolean playing;
  private boolean online;

  @Stringify(LongStringifier.class)
  private Map<Long, Integer> friendUnreadMessagesMap = new HashMap<>();

  private Date registerDate;
  private Date lastVisited;
  private int visitCounter = 0;

  private String fbId;

  private String googleSub;

  private boolean subscribed;

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

  private String getFullName() {
    return getFirstName() + " " + getLastName();
  }

  private String getShortName() {
    if (getPlayerName() == null) {
      return getFirstName();
    }
    return getPlayerName();
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
  public void updateSerializable(Player player) {
    if (player == null) {
      return;
    }
    this.loggedIn = player.loggedIn;
    this.playing = player.playing;
    this.online = player.online;
    this.playerName = player.playerName;
    this.lastVisited = player.lastVisited;
    this.sessionId = player.sessionId;
    this.registerDate = player.registerDate;
    this.visitCounter = player.visitCounter;
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
