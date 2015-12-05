package online.draughts.rus.shared.dto;


/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 14:10
 */
public class FriendDto {

  private String id;

  private PlayerDto friend;
  private PlayerDto friendOf;

  private boolean favorite;

  public FriendDto() {
  }

  public FriendDto(Long friendId, Long friendOfId) {
    this.id = friendId + "::" + friendOfId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PlayerDto getFriend() {
    return friend;
  }

  public void setFriend(PlayerDto friend) {
    this.friend = friend;
  }

  public PlayerDto getFriendOf() {
    return friendOf;
  }

  public void setFriendOf(PlayerDto friendOf) {
    this.friendOf = friendOf;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public FriendDto setFavorite(boolean favorite) {
    this.favorite = favorite;
    return this;
  }
}
