package online.shashki.ru.shared.model.key;

import online.shashki.ru.shared.model.entity.ShashistEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 14:19
 */
@Embeddable
public class FriendId implements Serializable {

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private ShashistEntity friend;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private ShashistEntity friendOf;

  public ShashistEntity getFriend() {
    return friend;
  }

  public void setFriend(ShashistEntity friend) {
    this.friend = friend;
  }

  public ShashistEntity getFriendOf() {
    return friendOf;
  }

  public void setFriendOf(ShashistEntity friendOf) {
    this.friendOf = friendOf;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    FriendId friendId = (FriendId) o;

    if (friend != null ? !friend.equals(friendId.friend) : friendId.friend != null) return false;
    if (friendOf != null ? !friendOf.equals(friendId.friendOf) : friendId.friendOf != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = friend != null ? friend.hashCode() : 0;
    result = 31 * result + (friendOf != null ? friendOf.hashCode() : 0);
    return result;
  }

}
