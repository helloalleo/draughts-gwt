package online.draughts.rus.server.util;

import online.draughts.rus.server.config.Config;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.server.domain.Player;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.10.15
 * Time: 6:51
 */
public class AuthUtils {

  public static final String AUTHENTICATED = "isAuthenticated";

  private static boolean isAuthenticated(HttpSession session) {
    if (session == null) {
      return false;
    }
    Object isAuth = session.getAttribute(AUTHENTICATED);
    return isAuth == null ? Boolean.FALSE : (Boolean) isAuth;
  }

  public static boolean isNotAuthenticated(HttpSession session) {
    return !isAuthenticated(session);
  }

  private static void login(HttpServletRequest req) {
    req.getSession().setAttribute(AUTHENTICATED, true);
  }

  public static void processUserAndRedirectToPlayPage(PlayerService playerService,
                                                      HttpServletRequest req, HttpServletResponse resp,
                                                      Player player) throws IOException {
    player.setLastVisited(new Date());
    player.setLoggedIn(true);
    player.setPlaying(false);
    player.setOnline(false);
    player.setSubscribeOnNewsletter(true);
    if (player.getPublicName().length() > Config.STRIP_PLAYER_NAME) {
      player.setPlayerName(player.getPublicName().substring(0, Config.STRIP_PLAYER_NAME));
    }

    HttpSession session = req.getSession();
    player.setSessionId(session.getId());
    if (player.getId() == 0) {
      player.setRegisterDate(new Date());
    }
    player.setActive(true);
    player.setVisitCounter(player.getVisitCounter() + 1);
    player.setRating(player.getRating() + 2);

    playerService.save(player);

    if (player.isActive()) {
      AuthUtils.login(req);
    }

    String locale = "ru";
    for (Cookie cookie : req.getCookies()) {
      if ("locale".equalsIgnoreCase(cookie.getName())) {
        locale = cookie.getValue();
        break;
      }
    }
    resp.addCookie(new Cookie("loggedIn", "1"));

    resp.sendRedirect(String.format("%s/?locale=%s%s", Config.CONTEXT, locale, Config.PLAY_HASH));
  }
}
