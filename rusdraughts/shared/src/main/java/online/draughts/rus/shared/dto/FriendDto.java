package online.draughts.rus.shared.dto;


/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 14:10
 */
public class FriendDto {

  private String id;

  private PlayerDto me;
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

  public PlayerDto getMe() {
    return me;
  }

  public void setMe(PlayerDto me) {
    this.me = me;
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
