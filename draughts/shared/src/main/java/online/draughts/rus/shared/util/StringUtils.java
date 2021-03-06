package online.draughts.rus.shared.util;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 20.10.15
 * Time: 16:44
 */
public class StringUtils {

  private static final RegExp hashTagPattern = RegExp.compile("#[а-яa-z]+", "i");

  public static boolean isEmpty(String s) {
    return s == null || s.isEmpty();
  }

  public static boolean isNotEmpty(String s) {
    return !isEmpty(s);
  }

  public static Set<String> getHashes(String str) {
    MatchResult matchResult = hashTagPattern.exec(str);
    Set<String> hashes = new HashSet<>();
    for (int i = 0; i < matchResult.getGroupCount(); i++) {
      hashes.add(matchResult.getGroup(i));
    }
    return hashes;
  }

  public static String join(String[] strings, String separator) {
    StringBuilder result = new StringBuilder();

    for (String s : strings) {
      if (result.length() != 0) {
        result.append(separator);
      }
      result.append(s);
    }

    return result.toString();
  }

  public static String emptyIfNull(String string) {
    return string == null ? "" : string;
  }
}
