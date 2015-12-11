package online.draughts.rus.server.domain;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import online.draughts.rus.shared.dto.PlayerDto;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 0:36
 */
public class Player extends ModelImpl<Player> {

  private static Player INSTENCE;
  //  @Index
  private String sessionId;

  //  @Index
  private String vkId;

  //  @Index
  private String fbId;

  //  @Index
  private String googleSub;

  //  @Index
  private String email;

  //  @Index
  private String firstName;

  //  @Index
  private String lastName;

  //  @Index
  private String playerName;

  //  @Index
  private int rating = 0;

  private int gamePlayed = 0;

  private int gameWin = 0;

  //  @Index
  private PlayerDto.AuthProvider authProvider;

  private Set<Friend> friends = new HashSet<>();

  private Set<Friend> friendOf = new HashSet<>();

  private Set<GameMessage> receivedPlayerMessages = new HashSet<>();

  private Set<GameMessage> sentPlayerMessages = new HashSet<>();

  private Set<Game> whiteRoleGames = new HashSet<>();

  private Set<Game> blackRoleGames = new HashSet<>();

  private boolean loggedIn;
  private boolean playing;
  //  @Index
  private boolean online;

  //  @Stringify(LongStringifier.class)
  private Map<Long, Integer> friendUnreadMessagesMap = new HashMap<>();

  //  @Index
  private Date registerDate;
  //  @Index
  private Date lastVisited;
  //  @Index
  private int visitCounter = 0;

  //  @Index
  private boolean subscribed;

  //  @Index
  private boolean active;

  public Player() {
    super(Player.class);
  }

  public boolean isSubscribed() {
    return subscribed;
  }

  public void setSubscribed(boolean subscribed) {
    this.subscribed = subscribed;
    getEntiy().setIndexedProperty("subscribed", subscribed);
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
    getEntiy().setIndexedProperty("active", active);
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
    getEntiy().setIndexedProperty("sessionId", sessionId);
  }

  public String getVkId() {
    return vkId;
  }

  public void setVkId(String vkId) {
    this.vkId = vkId;
    getEntiy().setIndexedProperty("vkId", vkId);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
    getEntiy().setIndexedProperty("email", email);
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
    getEntiy().setProperty("firstName", firstName);
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
    getEntiy().setProperty("lastName", lastName);
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
    getEntiy().setIndexedProperty("playerName", playerName);
  }

  public PlayerDto.AuthProvider getAuthProvider() {
    return authProvider;
  }

  public void setAuthProvider(PlayerDto.AuthProvider authProvider) {
    this.authProvider = authProvider;
    getEntiy().setIndexedProperty("authProvider", authProvider.name());
  }

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
    getEntiy().setProperty("loggedIn", loggedIn);
  }

  public boolean isPlaying() {
    return playing;
  }

  public void setPlaying(boolean playing) {
    this.playing = playing;
    getEntiy().setProperty("playing", playing);
  }

  public boolean isOnline() {
    return online;
  }

  public void setOnline(boolean online) {
    this.online = online;
    getEntiy().setIndexedProperty("online", online);
  }

  public Map<Long, Integer> getFriendUnreadMessagesMap() {
    return friendUnreadMessagesMap;
  }

  public void setRegisterDate(Date registerDate) {
    this.registerDate = registerDate;
    getEntiy().setIndexedProperty("registerDate", registerDate);
  }

  public Date getLastVisited() {
    return lastVisited;
  }

  public void setLastVisited(Date lastVisited) {
    this.lastVisited = lastVisited;
    getEntiy().setIndexedProperty("lastVisited", lastVisited);
  }

  public int getVisitCounter() {
    return visitCounter;
  }

  public void setVisitCounter(int visitCounter) {
    this.visitCounter = visitCounter;
    getEntiy().setIndexedProperty("visitCounter", visitCounter);
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
    getEntiy().setProperty("rating", rating);
  }

  public int getGamePlayed() {
    return gamePlayed;
  }

  public void setGamePlayed(int gamePlayed) {
    this.gamePlayed = gamePlayed;
    getEntiy().setProperty("gamePlayed", gamePlayed);
  }

  public int getGameWin() {
    return gameWin;
  }

  public void setGameWin(int gameWin) {
    this.gameWin = gameWin;
    getEntiy().setProperty("gameWin", gameWin);
  }

  public String getFbId() {
    return fbId;
  }

  public void setFbId(String fbId) {
    this.fbId = fbId;
    getEntiy().setIndexedProperty("fbId", fbId);
  }

  public String getGoogleSub() {
    return googleSub;
  }

  public void setGoogleSub(String googleSub) {
    this.googleSub = googleSub;
    getEntiy().setIndexedProperty("googleSub", googleSub);
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
   * @param playerDto
   */
  public void updateSerializable(PlayerDto playerDto) {
    if (playerDto == null) {
      return;
    }
    this.playerName = playerDto.getPlayerName();
    this.online = playerDto.isOnline();
    this.playing = playerDto.isPlaying();
    this.loggedIn = playerDto.isLoggedIn();
    this.subscribed = playerDto.isSubscribed();
  }

  public static Player getInstance() {
    if (INSTENCE == null) {
      INSTENCE = new Player();
    }
    return INSTENCE;
  }

  // ********* DB Queries ********* //
  public Player findBySessionId(String sessionId) {
    Query.Filter sessionIdFilter =
        new Query.FilterPredicate("sessionId",
            Query.FilterOperator.EQUAL,
            sessionId);
    Query query = new Query(getEntityName()).setFilter(sessionIdFilter);
    PreparedQuery pq = getDatastore().prepare(query);
    return getSingleResultObject(pq);
  }

  public Player findByVkId(String vkId) {
    Query.Filter sessionIdFilter =
        new Query.FilterPredicate("vkId",
            Query.FilterOperator.EQUAL,
            vkId);
    Query query = new Query(getEntityName()).setFilter(sessionIdFilter);
    PreparedQuery pq = getDatastore().prepare(query);
    return getSingleResultObject(pq);
  }
}
