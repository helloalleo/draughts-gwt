package online.draughts.rus.shared.model;

import online.draughts.rus.shared.model.key.FriendId;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 14:10
 */
@Entity
@Table(name = "friend", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"friend_id", "friend_of_id"})
})
@AssociationOverrides({
    @AssociationOverride(name = "pk.friend", joinColumns = @JoinColumn(name = "friend_id", insertable = false, updatable = false)),
    @AssociationOverride(name = "pk.friendOf", joinColumns = @JoinColumn(name = "friend_of_id", insertable = false, updatable = false))})
public class Friend implements BasePersistableObject {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  @EmbeddedId
  private FriendId pk = new FriendId();
  private boolean favorite;

  public Friend() {
  }

  public Long getId() {
    return id;
  }

  public Friend setId(Long id) {
    this.id = id;
    return this;
  }

  public void setFriendId(Long friendId) {
    this.id = friendId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Friend friend = (Friend) o;
    return Objects.equals(pk, friend.pk);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pk);
  }

  public boolean isFavorite() {
    return favorite;
  }

  public Friend setFavorite(boolean favorite) {
    this.favorite = favorite;
    return this;
  }

  public FriendId getPk() {
    return pk;
  }

  public Friend setPk(FriendId pk) {
    this.pk = pk;
    return this;
  }
}
