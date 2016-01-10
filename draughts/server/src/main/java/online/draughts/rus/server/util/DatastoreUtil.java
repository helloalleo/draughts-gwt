package online.draughts.rus.server.util;

import com.google.appengine.api.datastore.Key;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.15
 * Time: 15:01
 */
public class DatastoreUtil {

  public static <T> T getObject(Key getter) {
    if (getter == null) {
      return null;
    }
    return (T) getter;
  }

}
