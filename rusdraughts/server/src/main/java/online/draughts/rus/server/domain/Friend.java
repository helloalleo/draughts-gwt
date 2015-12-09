package online.draughts.rus.server.domain;


import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Objects;

import static online.draughts.rus.shared.util.ObjectifyUtil.getObject;
import static online.draughts.rus.shared.util.ObjectifyUtil.setObject;

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

  @Index
  private Ref<Player> friend;
  @Index
  private Ref<Player> friendOf;

  @Index
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

  public Player getFriend() {
    return getObject(friend);
  }

  public void setFriend(Player friend) {
    this.friend = setObject(friend);
  }

  public Player getFriendOf() {
    return getObject(friendOf);
  }

  public void setFriendOf(Player friendOf) {
    this.friendOf = setObject(friendOf);
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
