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
  private static String authenticated = "loggedIn";

  public static void setLocation(String nameToken) {
    Cookies.setCookie(loc, nameToken);
    SHLog.debug("set location: " + nameToken);
  }

  public static String getLocation() {
    SHLog.debug("get location: " + Cookies.getCookie(loc));
    return Cookies.getCookie(loc);
  }

  public static boolean isLoggedIn() {
    SHLog.debug("is logged in " + Boolean.valueOf(Cookies.getCookie(authenticated)));
    return Boolean.valueOf(Cookies.getCookie(authenticated));
  }

  public static void login() {
    SHLog.debug("login");
    Cookies.setCookie(authenticated, Boolean.TRUE.toString());
  }

  public static void logout() {
    SHLog.debug("logout");
    Cookies.removeCookie(authenticated);
  }
}
