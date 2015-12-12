package online.draughts.rus.server.domain;

import online.draughts.rus.server.annotation.Index;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 14:10
 */
//@Entity
public class Friend extends ModelImpl<Friend> {

  @Index
  private Player friend;
  @Index
  private Player friendOf;

  @Index
  private boolean favorite;

  public Friend() {
    super(Friend.class);
  }

  public Player getFriend() {
    return friend;
  }

  public void setFriend(Player friend) {
    this.friend = friend;
  }

  public Player getFriendOf() {
    return friendOf;
  }

  public void setFriendOf(Player friendOf) {
    this.friendOf = friendOf;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }
}
