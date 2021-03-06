package online.draughts.rus.server.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.01.16
 * Time: 15:58
 */
public class RatingTest {

  @Test
  public void testCalcPlayerRating() throws Exception {
    // белые победили
    double rating = 0;
    rating = Rating.calcPlayerRating(rating, true, false, true, false);
    assertEquals(3, rating);

    // черные победили
    rating = 0;
    rating = Rating.calcPlayerRating(rating, true, true, false, false);
    assertEquals(1, rating);

    // черные победили
    rating = 0;
    rating = Rating.calcPlayerRating(rating, false, true, false, false);
    assertEquals(3, rating);

    // белые победили
    rating = 0;
    rating = Rating.calcPlayerRating(rating, false, false, true, false);
    assertEquals(1, rating);

    // игру покинули
    rating = 0;
    rating = Rating.calcPlayerRating(rating, true, false, false, false);
    assertEquals(2, rating);
  }
}