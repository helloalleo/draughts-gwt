package online.shashki.rus.shared.model.key;//package online.shashki.rus.clnt.online.shashki.rus.shared.model.key;

import online.shashki.rus.shared.model.Player;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 14:19
 */
@Embeddable
public class FriendId implements Serializable {

  @ManyToOne(fetch = FetchType.EAGER)
  private Player friend;
  @ManyToOne(fetch = FetchType.EAGER)
  private Player friendOf;

  public Player getFriend() {
    return friend;
  }

  public FriendId setFriend(Player friend) {
    this.friend = friend;
    return this;
  }

  public Player getFriendOf() {
    return friendOf;
  }

  public FriendId setFriendOf(Player friendOf) {
    this.friendOf = friendOf;
    return this;
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
