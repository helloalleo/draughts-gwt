package online.draughts.rus.server.domain;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import online.draughts.rus.server.annotation.Index;
import online.draughts.rus.server.annotation.MapKey;
import online.draughts.rus.server.annotation.MapValue;
import online.draughts.rus.shared.dto.PlayerDto;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 0:36
 */
public class Player extends ModelImpl<Player> {

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
  private int gameWon = 0;
  private int gameLost = 0;
  private int gameDraw = 0;
  @Index
  private PlayerDto.AuthProvider authProvider;
  private boolean loggedIn;
  private boolean playing;
  @Index
  private boolean online;

  @MapKey(Long.class)
  @MapValue(Integer.class)
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
  private boolean moderator;
  @Index
  private boolean admin;
  @Index
  private boolean active;
  private boolean subscribeOnNewsletter;

  public Player() {
    super(Player.class);
  }

  public static Player getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public boolean isSubscribed() {
    return subscribed;
  }

  public void setSubscribed(boolean subscribed) {
    this.subscribed = subscribed;
  }

  public boolean isModerator() {
    return moderator;
  }

  public Player setModerator(boolean moderator) {
    this.moderator = moderator;
    return this;
  }

  public boolean isAdmin() {
    return admin;
  }

  public Player setAdmin(boolean admin) {
    this.admin = admin;
    return this;
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

  public PlayerDto.AuthProvider getAuthProvider() {
    return authProvider;
  }

  public void setAuthProvider(PlayerDto.AuthProvider authProvider) {
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

  public void setFriendUnreadMessagesMap(Map<Long, Integer> friendUnreadMessagesMap) {
    this.friendUnreadMessagesMap = friendUnreadMessagesMap;
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

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public int getGamePlayed() {
    return gamePlayed;
  }

  public void setGamePlayed(int gamePlayed) {
    this.gamePlayed = gamePlayed;
  }

  public int getGameWon() {
    return gameWon;
  }

  public void setGameWon(int gameWon) {
    this.gameWon = gameWon;
  }

  public int getGameLost() {
    return gameLost;
  }

  public Player setGameLost(int gameLost) {
    this.gameLost = gameLost;
    return this;
  }

  public int getGameDraw() {
    return gameDraw;
  }

  public Player setGameDraw(int gameDraw) {
    this.gameDraw = gameDraw;
    return this;
  }

  public String getFbId() {
    return fbId;
  }

  public void setFbId(String fbId) {
    this.fbId = fbId;
  }

  public String getGoogleSub() {
    return googleSub;
  }

  public void setGoogleSub(String googleSub) {
    this.googleSub = googleSub;
  }

  public Date getRegisterDate() {
    return registerDate;
  }

  public void setRegisterDate(Date registerDate) {
    this.registerDate = registerDate;
  }

  public boolean isSubscribeOnNewsletter() {
    return subscribeOnNewsletter;
  }

  public void setSubscribeOnNewsletter(boolean subscribeOnNewsletter) {
    this.subscribeOnNewsletter = subscribeOnNewsletter;
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
    setPlayerName(playerDto.getPlayerName());
    setOnline(playerDto.isOnline());
    setPlaying(playerDto.isPlaying());
    setLoggedIn(playerDto.isLoggedIn());
    setSubscribed(playerDto.isSubscribed());
    setSubscribeOnNewsletter(playerDto.isSubscribeOnNewsletter());
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

  @Override
  public List<Player> findAll() {
    Query query = new Query(getEntityName());
    query.addSort("admin", Query.SortDirection.DESCENDING);
    query.addSort("moderator", Query.SortDirection.DESCENDING);
    query.addSort("online", Query.SortDirection.DESCENDING);
    PreparedQuery preparedQuery = getDatastore().prepare(query);
    return getListResult(preparedQuery);
  }

  public List<Player> findOnline() {
    Query.Filter onlineFilter =
        new Query.FilterPredicate("online",
            Query.FilterOperator.EQUAL,
            true);
    Query query = new Query(getEntityName()).setFilter(onlineFilter);
    PreparedQuery preparedQuery = getDatastore().prepare(query);
    return getListResult(preparedQuery);
  }

  public Player findByFbId(String userId) {
    Query.Filter fbIdFilter =
        new Query.FilterPredicate("fbId",
            Query.FilterOperator.EQUAL,
            userId);
    Query query = new Query(getEntityName()).setFilter(fbIdFilter);
    PreparedQuery pq = getDatastore().prepare(query);
    return getSingleResultObject(pq);
  }

  public Player findByGoogleSub(String sub) {
    Query.Filter googleSubFilter =
        new Query.FilterPredicate("googleSub",
            Query.FilterOperator.EQUAL,
            sub);
    Query query = new Query(getEntityName()).setFilter(googleSubFilter);
    PreparedQuery pq = getDatastore().prepare(query);
    return getSingleResultObject(pq);
  }

  private static class SingletonHolder {
    private static final Player INSTANCE = new Player();
  }
}
