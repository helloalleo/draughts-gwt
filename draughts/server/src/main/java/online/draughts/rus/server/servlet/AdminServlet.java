package online.draughts.rus.server.servlet;

import com.google.inject.Singleton;
import online.draughts.rus.server.domain.Player;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by aleksey on 04.09.16.
 */
@Singleton
public class AdminServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    List<Player> players = Player.getInstance().findAll();
    for (Player player : players) {
      player.setGameWon(0);
      player.setGameLost(0);
      player.setGameDraw(0);
      player.setGamePlayed(0);
      player.setRating(1000);
      player.update();
    }
    // удаляет игры
//    List<Game> games = Game.getInstance().findTrueRange(200, 100);
//    for (Game game : games) {
//      game.setDeleted(true);
//      game.update();
//    }
  }
}
