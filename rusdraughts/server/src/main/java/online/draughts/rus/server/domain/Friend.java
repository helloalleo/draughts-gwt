package online.draughts.rus.server.domain;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 14:10
 */
//@Entity
public class Friend extends ModelImpl {

  private Player friend;
  private Player friendOf;

  private boolean favorite;

  public Friend() {
    super(Friend.class);
  }

  public Player getFriend() {
    return friend;
  }

  public void setFriend(Player friend) {
    setIndexedProperty("friend", friend);
    this.friend = friend;
  }

  public Player getFriendOf() {
    return friendOf;
  }

  public void setFriendOf(Player friendOf) {
    setIndexedProperty("friendOf", friendOf);
    this.friendOf = friendOf;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    getEntiy().setIndexedProperty("favorite", favorite);
    this.favorite = favorite;
  }
}
