//package online.shashki.rus.shared.model.entity;
//
//import com.google.gwt.user.client.rpc.IsSerializable;
//import online.shashki.rus.shared.model.key.FriendId;
//
//import javax.annotation.Nullable;
//import javax.persistence.*;
//
///**
// * Created with IntelliJ IDEA.
// * User: alekspo
// * Date: 01.12.14
// * Time: 14:10
// */
//@Entity
//@Table(name = "friend")
//@AssociationOverrides({
//    @AssociationOverride(name = "pk.friend", joinColumns = @JoinColumn(name = "friend_id", insertable = false, updatable = false)),
//    @AssociationOverride(name = "pk.friendOf", joinColumns = @JoinColumn(name = "friend_of_id", insertable = false, updatable = false)) })
//public class FriendEntity implements IsSerializable {
//
//  @EmbeddedId
//  private FriendId pk = new FriendId();
//  @Nullable
//  private boolean favorite;
//
//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || getClass() != o.getClass()) return false;
//
//    FriendEntity friend = (FriendEntity) o;
//
//    if (favorite != friend.favorite) return false;
//    if (!pk.equals(friend.pk)) return false;
//
//    return true;
//  }
//
//  @Override
//  public int hashCode() {
//    int result = pk.hashCode();
//    result = 31 * result + (favorite ? 1 : 0);
//    return result;
//  }
//
//  public boolean isFavorite() {
//    return favorite;
//  }
//
//  public void setFavorite(boolean favorite) {
//    this.favorite = favorite;
//  }
//
//  public FriendId getPk() {
//    return pk;
//  }
//
//  public void setPk(FriendId pk) {
//    this.pk = pk;
//  }
//}
