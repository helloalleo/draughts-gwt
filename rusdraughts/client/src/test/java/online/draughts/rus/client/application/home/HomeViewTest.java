package online.draughts.rus.client.application.home;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.tester.MockFactory;
import com.gwtplatform.tester.MockingBinder;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.shared.model.Player;
import online.draughts.rus.testutil.SimpleTestCase;
import online.draughts.rus.testutil.ViewTestModule;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 10:57
 */
public class HomeViewTest extends SimpleTestCase {

  public static class Module extends ViewTestModule {

    static class MyTestBinder extends MockingBinder<Widget, HomeView> implements HomeView.Binder {
      @Inject
      public MyTestBinder(final MockFactory mockitoMockFactory) {
        super(Widget.class, mockitoMockFactory);
      }
    }

    @Override
    protected void configureViewTest() {
      bind(HomeView.Binder.class).to(MyTestBinder.class);
      bindManyInstances(String.class, NameTokens.homePage);
      final Player player = new Player();
      player.setIncrementPageSize(10);
      player.setPlayerName("test");
      bindManyInstances(Player.class, player);
    }
  }

  @AfterClass
  public static void tearDown() {
    GWTMockUtilities.restore();
  }

  @Inject
  HomeView homeView;

  @Test
  public void testHomeView() {
    assertNotNull(homeView);

    homeView.onNewGame(null);
  }
}