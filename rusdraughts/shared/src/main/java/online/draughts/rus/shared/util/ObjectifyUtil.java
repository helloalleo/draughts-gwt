package online.draughts.rus.shared.util;

import com.googlecode.objectify.Ref;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.15
 * Time: 15:01
 */
public class ObjectifyUtil {

  public static <T> T getObject(Ref<T> getter) {
    if (getter == null) {
      return null;
    }
    return (T) getter.get();
  }

  public static <T> Ref<T> setObject(T setter) {
    if (setter == null) {
      return null;
    }
    return Ref.create(setter);
  }
}
