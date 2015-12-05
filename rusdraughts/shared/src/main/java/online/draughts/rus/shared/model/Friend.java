package online.draughts.rus.shared.model;


import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 14:10
 */
@Entity
public class Friend implements BasePersistableObject {

  @Id
  private String id;

  private Key<Player> friend;
  private Key<Player> friendOf;

  private boolean favorite;

  public Friend() {
  }

  public Friend(Long friendId, Long friendOfId) {
    this.id = friendId + "::" + friendOfId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Key<Player> getFriend() {
    return friend;
  }

  public void setFriend(Long friendId) {
    this.friend = Key.create(Player.class, friendId);
  }

  public Key<Player> getFriendOf() {
    return friendOf;
  }

  public void setFriendOf(Long friendOfId) {
    this.friendOf = Key.create(Player.class, friendOfId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Friend friend = (Friend) o;
    return Objects.equals(id, friend.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public boolean isFavorite() {
    return favorite;
  }

  public Friend setFavorite(boolean favorite) {
    this.favorite = favorite;
    return this;
  }
}
