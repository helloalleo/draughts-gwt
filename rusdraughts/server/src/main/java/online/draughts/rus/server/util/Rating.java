package online.draughts.rus.server.util;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 25.10.15
 * Time: 8:37
 */
public class Rating {

  public static final int WIN_RATING = 3;
  public static final int LOST_RATING = 1;
  public static final int DRAW_RATING = 2;

  public static int calcPlayerRating(int rating, boolean white, boolean blackWon, boolean whiteWon) {
    return rating
        + (!whiteWon && !blackWon ? DRAW_RATING
        : (white ? (whiteWon ? WIN_RATING : LOST_RATING)
        : (blackWon ? WIN_RATING : LOST_RATING)));
  }
}
