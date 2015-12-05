package online.draughts.rus.shared.dto;


/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 14:10
 */
public class Friend {

  private String id;

  private Player friend;
  private Player friendOf;

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

  public Friend setFavorite(boolean favorite) {
    this.favorite = favorite;
    return this;
  }
}
