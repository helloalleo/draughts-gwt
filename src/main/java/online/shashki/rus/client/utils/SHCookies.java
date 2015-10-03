package online.shashki.rus.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;
import online.shashki.rus.client.exception.InvalidCookieException;
import online.shashki.rus.client.service.ProfileRpcService;
import online.shashki.rus.shared.config.ShashkiConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.09.15
 * Time: 17:31
 */
public class SHCookies {

  private static String loc = "loc"; // куки адреса страницы
  private static String authenticated = "loggedIn";
  private static ShashkiConfiguration configuration = GWT.create(ShashkiConfiguration.class);

  public static void setLocation(String nameToken) {
    Cookies.setCookie(loc, nameToken);
    SHLog.debug("set location: " + nameToken);
  }

  public static String getLocation() {
    SHLog.debug("get location: " + Cookies.getCookie(loc));
    return Cookies.getCookie(loc);
  }

  public static boolean isLoggedIn() {
    final String authCookie = Cookies.getCookie(authenticated);
    ProfileRpcService.App.getInstance().isCookieValid(Cookies.getCookie(authenticated), new AsyncCallback<Boolean>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(caught).show();
      }

      @Override
      public void onSuccess(Boolean result) {
        if (!result && (authCookie != null && !authCookie.isEmpty())) {
          Window.Location.assign(configuration.site_url() + "/#!login");
          throw new InvalidCookieException();
        }
      }
    });
    SHLog.debug("LOG COOKIE " + authCookie);
    return authCookie != null && !authCookie.isEmpty();
  }

  public static void logout() {
    SHLog.debug("logout");
    Cookies.removeCookie(authenticated);
  }
}
