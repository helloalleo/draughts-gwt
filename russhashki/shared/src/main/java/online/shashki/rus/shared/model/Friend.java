package online.shashki.rus.shared.model;

import online.shashki.rus.shared.model.key.FriendId;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.12.14
 * Time: 14:10
 */
@Entity
@Table(name = "friend")
@AssociationOverrides({
    @AssociationOverride(name = "pk.friend", joinColumns = @JoinColumn(name = "friend_id", insertable = false, updatable = false)),
    @AssociationOverride(name = "pk.friendOf", joinColumns = @JoinColumn(name = "friend_of_id", insertable = false, updatable = false))})
public class Friend implements BasePersistableObject {

  @EmbeddedId
  private FriendId pk = new FriendId();
  @Null
  private boolean favorite;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Friend friend = (Friend) o;

    if (favorite != friend.favorite) return false;
    if (!pk.equals(friend.pk)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = pk.hashCode();
    result = 31 * result + (favorite ? 1 : 0);
    return result;
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
