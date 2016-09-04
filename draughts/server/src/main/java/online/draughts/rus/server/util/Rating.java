package online.draughts.rus.server.util;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 25.10.15
 * Time: 8:37
 */
public class Rating {

  private static final double WIN_RATING = 2;
  private static final double LOST_RATING = -2;
  private static final double DRAW_RATING = 1;

  public static double calcPlayerRating(double rating, boolean white, boolean blackWon, boolean whiteWon, boolean draw) {
    return rating
        + (draw ? DRAW_RATING
        : (white ? (whiteWon ? WIN_RATING : LOST_RATING)
        : (blackWon ? WIN_RATING : LOST_RATING)));
  }
}
