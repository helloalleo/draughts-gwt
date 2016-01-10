package online.draughts.rus.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.channel.ServerChannel;
import online.draughts.rus.server.config.ServerConfiguration;
import online.draughts.rus.server.domain.Player;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 13.12.15
 * Time: 14:58
 */
@Singleton
public class CheckOnlineServlet extends HttpServlet {

  @Inject
  private ServerChannel serverChannel;
  @Inject
  private ServerConfiguration config;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String remoteIp = req.getRemoteAddr();
    if (!remoteIp.equals(config.getGaeCronIpAddr())) {
      return;
    }

    List<Player> players = Player.getInstance().findAll();
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, -Integer.valueOf(config.getGaeCronCheckInterval()));
    for (Player player : players) {
      Calendar lastVisited = Calendar.getInstance();
      lastVisited.setTime(player.getLastVisited());
      if (calendar.after(lastVisited)) {
        serverChannel.handleClose(String.valueOf(player.getId()));
      }
    }
  }
}
