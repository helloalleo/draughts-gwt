package online.draughts.rus.server.util;

import online.draughts.rus.server.config.ServerConfiguration;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.shared.model.Player;

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

  public static Boolean isAuthenticated(HttpSession session) {
    if (session == null) {
      return false;
    }
    Object isAuth = session.getAttribute("isAuthenticated");
    return isAuth == null ? Boolean.FALSE : (Boolean) isAuth;
  }

  public static void login(HttpServletRequest req) {
    req.getSession().setAttribute("isAuthenticated", true);
  }

  public static void processUserAndRedirectToHomePage(PlayerService playerService, ServerConfiguration config,
                                                      HttpServletRequest req, HttpServletResponse resp,
                                                      Player player) throws IOException {
    player.setLastVisited(new Date());
    player.setLoggedIn(true);
    player.setPlaying(false);
    player.setOnline(false);

    HttpSession session = req.getSession();
    if (player.getSessionId() == null
        || !player.getSessionId().equals(session.getId())) {
      player.setSessionId(session.getId());
    }
    player.setVisitCounter(player.getVisitCounter() + 1);

    Rating.calcPlayerRating(player);
    playerService.saveOrCreateOnServer(player);

    if (!player.isBanned()) {
      AuthUtils.login(req);
    }
    resp.sendRedirect(config.getHomeUrl());
  }
}
