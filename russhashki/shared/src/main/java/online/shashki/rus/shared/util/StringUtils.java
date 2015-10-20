package online.shashki.rus.shared.util;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 20.10.15
 * Time: 16:44
 */
public class StringUtils {

  public static boolean isEmpty(String s) {
    return s == null || s.isEmpty();
  }

  public static boolean isNotEmpty(String s) {
    return !isEmpty(s);
  }
}
