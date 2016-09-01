package online.draughts.rus.server.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.channel.ServerChannel;
import online.draughts.rus.server.config.Config;
import online.draughts.rus.server.domain.Game;
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

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String remoteIp = req.getRemoteAddr();
    if (!remoteIp.equals(Config.GAE_CRON_IP_ADDR)) {
      return;
    }

    List<Game> games = Game.getInstance().findTrueRange(0, 100);
    for (Game game : games) {
      game.setDeleted(false);
      game.setGameSnapshot(false);
      game.update();
    }

    List<Player> players = Player.getInstance().findOnline();
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR, -Integer.valueOf(Config.GAE_CRON_RESET_USERS_INTERVAL_HOUR));
    for (Player player : players) {
      Calendar lastVisited = Calendar.getInstance();
      lastVisited.setTime(player.getLastVisited());
      if (calendar.after(lastVisited)) {
        serverChannel.handleClose(String.valueOf(player.getId()));
      }
    }
  }
}
