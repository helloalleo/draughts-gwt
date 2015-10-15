package online.shashki.rus.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.shashki.rus.server.config.ServerConfiguration;
import online.shashki.rus.server.service.PlayerServiceImpl;
import online.shashki.rus.shared.model.Player;

import javax.servlet.ServletException;
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

  private final PlayerServiceImpl playerService;
  private final ServerConfiguration configuration;

  @Inject
  LogoutServlet(PlayerServiceImpl playerService, ServerConfiguration serverConfiguration) {
    this.playerService = playerService;
    this.configuration = serverConfiguration;
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Player player = playerService.findBySessionId(request.getSession().getId());
    if (player != null) {
      player.setOnline(false);
      player.setPlaying(false);
      player.setLoggedIn(false);
      playerService.save(player);
    }

    request.getSession().invalidate();
    response.sendRedirect(configuration.getContext());
  }
}
