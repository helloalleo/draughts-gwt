package online.draughts.rus.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.channel.ServerChannel;
import online.draughts.rus.server.config.Config;
import online.draughts.rus.server.domain.Player;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.06.16
 * Time: 20:43
 */
@Singleton
public class LogoutAll extends HttpServlet {
  @Inject
  private ServerChannel serverChannel;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String remoteIp = req.getRemoteAddr();
    if (!remoteIp.equals(Config.GAE_CRON_IP_ADDR)) {
      return;
    }

    List<Player> players = Player.getInstance().findAll();
    for (Player player : players) {
      player.setRating(0);
      player.setOnline(false);
      player.setPlaying(false);
      player.update();
    }
  }
}
