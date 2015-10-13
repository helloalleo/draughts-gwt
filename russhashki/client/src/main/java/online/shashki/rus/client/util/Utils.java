package online.shashki.rus.client.util;

import online.shashki.rus.client.place.NameTokens;

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
}
