//package online.shashki.rus.shared.model.key;
//
//import online.shashki.rus.shared.model.Shashist;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
///**
// * Created with IntelliJ IDEA.
// * User: alekspo
// * Date: 01.12.14
// * Time: 14:19
// */
//@Embeddable
//public class FriendId implements Serializable {
//
//  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//  private Shashist friend;
//  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//  private Shashist friendOf;
//
//  public Shashist getFriend() {
//    return friend;
//  }
//
//  public void setFriend(Shashist friend) {
//    this.friend = friend;
//  }
//
//  public Shashist getFriendOf() {
//    return friendOf;
//  }
//
//  public void setFriendOf(Shashist friendOf) {
//    this.friendOf = friendOf;
//  }
//
//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || getClass() != o.getClass()) return false;
//
//    FriendId friendId = (FriendId) o;
//
//    if (friend != null ? !friend.equals(friendId.friend) : friendId.friend != null) return false;
//    if (friendOf != null ? !friendOf.equals(friendId.friendOf) : friendId.friendOf != null) return false;
//
//    return true;
//  }
//
//  @Override
//  public int hashCode() {
//    int result = friend != null ? friend.hashCode() : 0;
//    result = 31 * result + (friendOf != null ? friendOf.hashCode() : 0);
//    return result;
//  }
//
//}
