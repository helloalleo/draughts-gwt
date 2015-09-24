package online.shashki.rus.shared.model;

import online.shashki.rus.shared.model.entity.FriendEntity;
import online.shashki.rus.shared.model.entity.GameMessageEntity;

import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 12.12.14
 * Time: 9:05
 */
public interface Shashist extends PersistableObject {

  String getVkUid();

  void setVkUid(String vkUid);

  String getEmail();

  void setEmail(String email);

  String getFirstName();

  void setFirstName(String firstName);

  String getLastName();

  void setLastName(String lastName);

  String getPlayerName();

  void setPlayerName(String playerName);

  String getAuthProvider();

  void setAuthProvider(String authProvider);

  Set<FriendEntity> getFriends();

  void setFriends(Set<FriendEntity> friends);

  Set<FriendEntity> getFriendOf();

  void setFriendOf(Set<FriendEntity> friendOf);

  Set<GameMessageEntity> getReceivedPlayerMessages();

  void setReceivedPlayerMessages(Set<GameMessageEntity> playerMessageEntities);

  Set<GameMessageEntity> getSentPlayerMessages();

  void setSentPlayerMessages(Set<GameMessageEntity> playerMessageEntities);

  Set<GameMessageEntity> getReceivedGameMessages();

  void setReceivedGameMessages(Set<GameMessageEntity> receivedGameMessages);

  boolean isLoggedIn();

  void setLoggedIn(boolean loggedIn);

  boolean isPlaying();

  void setPlaying(boolean playing);

  boolean isOnline();

  void setOnline(boolean online);

  Date getRegisterDate();

  void setRegisterDate(Date registerDate);

  Date getLastVisited();

  void setLastVisited(Date lastVisited);

  int getVisitCounter();

  void setVisitCounter(int visitCounter);

  String getPublicName();

  String getFullName();

  String getSessionId();

  void setSessionId(String sessionId);
}
