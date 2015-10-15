package online.shashki.rus.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.shashki.rus.server.config.ServerConfiguration;
import online.shashki.rus.server.rest.PlayersResourceImpl;
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

  private final PlayersResourceImpl playerResource;
  private final ServerConfiguration configuration;

  @Inject
  LogoutServlet(PlayersResourceImpl playerResource, ServerConfiguration serverConfiguration) {
    this.playerResource = playerResource;
    this.configuration = serverConfiguration;
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Player player = playerResource.findBySessionId(request.getSession().getId());
    if (player != null) {
      player.setOnline(false);
      player.setPlaying(false);
      player.setLoggedIn(false);
      playerResource.saveOrCreate(player, true);
    }

    request.getSession().invalidate();
    response.sendRedirect(configuration.getContext());
  }
}
