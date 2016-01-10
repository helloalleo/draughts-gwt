package online.draughts.rus.client.util;

import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.shared.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 22:58
 */
public class Utils {

  public static NameTokens.Link[] concatLinks(NameTokens.Link[] a, NameTokens.Link[] b) {
    if (a == null) return b;
    if (b == null) return a;
    NameTokens.Link[] r = new NameTokens.Link[a.length + b.length];
    System.arraycopy(a, 0, r, 0, a.length);
    System.arraycopy(b, 0, r, a.length, b.length);
    return r;
  }

  public static String format(final String format, final Object... args) {
    if (StringUtils.isEmpty(format) || args == null) {
      return "";
    }

    final String pattern = "%s";

    int start = 0, last = 0, argsIndex = 0;
    final StringBuilder result = new StringBuilder();
    while ((start = format.indexOf(pattern, last)) != -1) {
      if (args.length <= argsIndex) {
        throw new IllegalArgumentException("There is more replace patterns than arguments!");
      }
      result.append(format.substring(last, start));
      result.append(args[argsIndex++]);

      last = start + pattern.length();
    }

    if (args.length > argsIndex) {
      throw new IllegalArgumentException("There is more arguments than replace patterns!");
    }

    result.append(format.substring(last));
    return result.toString();
  }
}
