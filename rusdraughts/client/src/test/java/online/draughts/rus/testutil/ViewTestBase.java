package online.draughts.rus.testutil;

import com.google.gwt.junit.GWTMockUtilities;
import org.junit.AfterClass;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 15:36
 */
public class ViewTestBase {

  @AfterClass
  public static void tearDown() {
    GWTMockUtilities.restore();
  }
}
