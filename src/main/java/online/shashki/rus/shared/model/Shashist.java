package online.shashki.rus.shared.model;

import com.google.gwt.user.client.rpc.GwtTransient;
import online.shashki.rus.shared.model.entity.FriendEntity;
import online.shashki.rus.shared.model.entity.GameEntity;
import online.shashki.rus.shared.model.entity.GameMessageEntity;
import online.shashki.rus.shared.model.entity.PersistableObjectImpl;
import org.hibernate.validator.constraints.Email;

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
@Table(name = "shashist")
public class Shashist extends PersistableObjectImpl {

  @Column(name = "session_id")
  private String sessionId;

  @GwtTransient
  @Column(name = "vk_uid")
  private String vkUid;

  @Email
  private String email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "player_name")
  private String playerName;

  @Column(name = "auth_provider")
  private String authProvider;

  @GwtTransient
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.friendOf", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<FriendEntity> friends;

  @GwtTransient
  @Column(name = "fiends_of")
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.friend", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<FriendEntity> friendOf;

  @GwtTransient
  @OneToMany(mappedBy = "receiver")
  private Set<GameMessageEntity> receivedPlayerMessages;

  @GwtTransient
  @OneToMany(mappedBy = "sender")
  private Set<GameMessageEntity> sentPlayerMessages;

  @GwtTransient
  @OneToMany(mappedBy = "receiver")
  private Set<GameMessageEntity> receivedGameMessages;

  @GwtTransient
  @OneToMany(mappedBy = "sender")
  private Set<GameMessageEntity> sentGameMessages;

  @GwtTransient
  @OneToMany(mappedBy = "playerWhite")
  private Set<GameEntity> whiteRoleGames;

  @GwtTransient
  @OneToMany(mappedBy = "playerBlack")
  private Set<GameEntity> blackRoleGames;

  @Column(name = "logged_in")
  private boolean loggedIn;
  private boolean playing;
  private boolean online;
  @Column(name = "register_date")
  private Date registerDate;
  @Column(name = "last_visited")
  private Date lastVisited;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Shashist that = (Shashist) o;

    if (!sessionId.equals(that.sessionId)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return sessionId.hashCode();
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

  public Set<FriendEntity> getFriends() {
    return friends;
  }

  public void setFriends(Set<FriendEntity> friends) {
    this.friends = friends;
  }

  public Set<FriendEntity> getFriendOf() {
    return friendOf;
  }

  public void setFriendOf(Set<FriendEntity> friendOf) {
    this.friendOf = friendOf;
  }

  public Set<GameMessageEntity> getReceivedPlayerMessages() {
    return receivedPlayerMessages;
  }

  public void setReceivedPlayerMessages(Set<GameMessageEntity> playerMessageEntities) {
    this.receivedPlayerMessages = playerMessageEntities;
  }

  public Set<GameMessageEntity> getSentPlayerMessages() {
    return sentPlayerMessages;
  }

  public void setSentPlayerMessages(Set<GameMessageEntity> playerMessageEntities) {
    this.sentPlayerMessages = playerMessageEntities;
  }

  public Set<GameMessageEntity> getReceivedGameMessages() {
    return receivedGameMessages;
  }

  public void setReceivedGameMessages(Set<GameMessageEntity> receivedGameMessages) {
    this.receivedGameMessages = receivedGameMessages;
  }

  public Set<GameMessageEntity> getSentGameMessages() {
    return sentGameMessages;
  }

  public void setSentGameMessages(Set<GameMessageEntity> sentGameMessages) {
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

  public Set<GameEntity> getWhiteRoleGames() {
    return whiteRoleGames;
  }

  public void setWhiteRoleGames(Set<GameEntity> whiteRoleGames) {
    this.whiteRoleGames = whiteRoleGames;
  }

  public Set<GameEntity> getBlackRoleGames() {
    return blackRoleGames;
  }

  public void setBlackRoleGames(Set<GameEntity> blackRoleGames) {
    this.blackRoleGames = blackRoleGames;
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

  public String getFullName() {
    return getFirstName() + " " + getLastName();
  }
}
