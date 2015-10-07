package online.shashki.rus.server.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.10.15
 * Time: 6:51
 */
public class AuthUtils {

  public static Boolean isAuthenticated(HttpSession session) {
    Object isAuth = session.getAttribute("isAuthenticated");
    return isAuth == null ? Boolean.FALSE : (Boolean) isAuth;
  }

  public static void login(HttpServletRequest req, HttpServletResponse resp) {
    req.getSession().setAttribute("isAuthenticated", true);
    resp.addCookie(new Cookie("loggedIn", req.getSession().getId()));
  }
}
