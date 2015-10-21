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

  private static String location = "location"; // куки адреса страницы
  private static ShashkiConfiguration configuration = GWT.create(ShashkiConfiguration.class);
  private static String newGameButtonStateMainPage = "newGameButtonState";

  public static void setLocation(String nameToken) {
    Cookies.setCookie(location, nameToken);
    SHLog.debug("set location: " + nameToken);
  }

  public static String getLocation() {
    SHLog.debug("get location: " + Cookies.getCookie(location));
    return Cookies.getCookie(location);
  }

  public static void setNewGameButtonState(boolean newGame) {
    Cookies.setCookie(newGameButtonStateMainPage, String.valueOf(newGame));
  }

  public static boolean getNewGameButtonState() {
    return Boolean.valueOf(Cookies.getCookie(newGameButtonStateMainPage));
  }
}
