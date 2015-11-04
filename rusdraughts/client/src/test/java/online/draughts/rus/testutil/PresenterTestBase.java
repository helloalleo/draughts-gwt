package online.draughts.rus.testutil;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import org.junit.AfterClass;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 15:35
 */
public class PresenterTestBase {

  @Inject
  protected PlaceManager placeManager;

  @Inject
  protected EventBus eventBus;

  @AfterClass
  public static void tearDown() {
    GWTMockUtilities.restore();
  }
}
