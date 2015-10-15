package online.shashki.rus.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import online.shashki.rus.shared.config.ShashkiConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.09.15
 * Time: 17:31
 */
public class SHCookies {

  private static String loc = "loc"; // куки адреса страницы
  private static ShashkiConfiguration configuration = GWT.create(ShashkiConfiguration.class);

  public static void setLocation(String nameToken) {
    Cookies.setCookie(loc, nameToken);
    SHLog.debug("set location: " + nameToken);
  }

  public static String getLocation() {
    SHLog.debug("get location: " + Cookies.getCookie(loc));
    return Cookies.getCookie(loc);
  }
}
