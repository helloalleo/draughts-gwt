package online.shashki.rus.client.utils;

import com.google.gwt.user.client.Cookies;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.09.15
 * Time: 17:31
 */
public class SHCookies {

  private static String loc = "loc"; // куки адреса страницы

  public static void setLocation(String nameToken) {
    Cookies.setCookie(loc, nameToken);
    SHLog.log("set location: " + nameToken);
  }

  public static String getLocation() {
    SHLog.log("get location: " + Cookies.getCookie(loc));
    return Cookies.getCookie(loc);
  }
}
