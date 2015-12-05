package online.draughts.rus.server.util;

import online.draughts.rus.server.domain.Player;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 25.10.15
 * Time: 8:37
 */
public class Rating {

  public static final int WIN_RATING = 4;
  public static final int LOSE_RATING = 2;
  public static final int DRAW_RATING = 3;
  public static final int VISIT_RATING = 1;
  public static final int TOTAL_PLAYED_RATING = 1;

  public static void calcPlayerRating(Player player) {
    int rating = player.getRating()
        + player.getGamePlayed() * TOTAL_PLAYED_RATING
        + player.getGameWin() * WIN_RATING
        + player.getGameDraw() * DRAW_RATING
        + player.getGameLose() * LOSE_RATING;
    player.setRating(rating);
  }

}
