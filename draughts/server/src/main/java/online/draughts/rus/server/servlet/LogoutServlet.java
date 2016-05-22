package online.draughts.rus.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.config.Config;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.service.PlayerService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 21:46
 */
@Singleton
public class LogoutServlet extends HttpServlet {

  private final PlayerService playerService;

  @Inject
  LogoutServlet(PlayerService playerService) {
    this.playerService = playerService;
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    Boolean remove = Boolean.valueOf(req.getParameter("remove"));
    Player player = playerService.findBySessionId(req.getSession().getId());
    if (player != null) {
      if (remove) {
        player.setActive(false);
      }
      player.setOnline(false);
      player.setPlaying(false);
      player.setLoggedIn(false);
      playerService.save(player);
    }

    req.getSession().invalidate();
    resp.addCookie(new Cookie("loggedIn", "0"));
    final String draughtsContext = "/" + Config.CONTEXT + "/";
    if (remove) {
      resp.sendRedirect(draughtsContext);
    } else {
      resp.sendRedirect(draughtsContext + Config.LOGIN_HASH);
    }
  }
}
