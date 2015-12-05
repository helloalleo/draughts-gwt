package online.draughts.rus.shared.model;


import com.googlecode.objectify.Ref;
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

  private Ref<Player> friend;
  private Ref<Player> friendOf;

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
    return friend.get();
  }

  public void setFriend(Player friend) {
    this.friend = Ref.create(friend);
  }

  public Player getFriendOf() {
    return friendOf.get();
  }

  public void setFriendOf(Player friendOf) {
    this.friendOf = Ref.create(friendOf);
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
