package online.draughts.rus.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.GwtTransient;
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

  @GwtTransient
  @JsonIgnore
  private String sessionId;

  @GwtTransient
  @JsonIgnore
  private String vkId;

  @GwtTransient
  @JsonIgnore
  private String email;

  private String firstName;

  private String lastName;

  private String playerName;

  private int rating = 0;

  @GwtTransient
  @JsonIgnore
  private int gamePlayed = 0;

  @GwtTransient
  @JsonIgnore
  private int gameWin = 0;

  @GwtTransient
  @JsonIgnore
  private int gameLose = 0;

  @GwtTransient
  @JsonIgnore
  private int gameDraw = 0;

  @GwtTransient
  @JsonIgnore
  private AuthProvider authProvider;

  @GwtTransient
  @JsonIgnore
  private Set<Ref<Friend>> friends = new HashSet<>();

  @GwtTransient
  @JsonIgnore
  private Set<Ref<Friend>> friendOf = new HashSet<>();

  @GwtTransient
  @JsonIgnore
  private Set<Ref<GameMessage>> receivedPlayerMessages;

  @GwtTransient
  @JsonIgnore
  private Set<Ref<GameMessage>> sentPlayerMessages;

  @GwtTransient
  @JsonIgnore
  private Set<Ref<Game>> whiteRoleGames;

  @GwtTransient
  @JsonIgnore
  private Set<Ref<Game>> blackRoleGames;

  private boolean loggedIn;
  private boolean playing;
  private boolean online;

  @GwtTransient
  @JsonIgnore
  @Stringify(LongStringifier.class)
  private Map<Long, Integer> friendUnreadMessagesMap = new HashMap<>();

  @GwtTransient
  @JsonIgnore
  private Date registerDate;
  @GwtTransient
  @JsonIgnore
  private Date lastVisited;
  @GwtTransient
  @JsonIgnore
  private int visitCounter = 0;

  @GwtTransient
  @JsonIgnore
  private String fbId;

  @GwtTransient
  @JsonIgnore
  private String googleSub;

  private boolean subscribed;

  @GwtTransient
  @JsonIgnore
  private boolean active;

  public Player() {
  }

  public boolean getSubscribed() {
    return subscribed;
  }

  public Player setSubscribed(boolean subscribed) {
    this.subscribed = subscribed;
    return this;
  }

  public boolean isSubscribed() {
    return subscribed;
  }

  public boolean isActive() {
    return active;
  }

  public Player setActive(boolean active) {
    this.active = active;
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

  public Player setFriendUnreadMessagesMap(Map<Long, Integer> friendUnreadMessagesMap) {
    this.friendUnreadMessagesMap = friendUnreadMessagesMap;
    return this;
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

  public Player setGoogleSub(String googleSub) {
    this.googleSub = googleSub;
    return this;
  }

  public String getGoogleSub() {
    return googleSub;
  }

  @JsonIgnore
  public String getSiteName() {
    return new SafeHtmlBuilder().appendHtmlConstant("<a href='")
        .appendHtmlConstant("#player?id=")
        .appendHtmlConstant(String.valueOf(getId()))
        .appendHtmlConstant("'>")
        .appendHtmlConstant(getShortName())
        .appendHtmlConstant("</a>").toSafeHtml().asString();
  }

  public enum AuthProvider {
    VK,
    FACEBOOK,
    GOOGLE
  }
}
