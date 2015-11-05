package online.draughts.rus.client.application.home;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.tester.MockFactory;
import com.gwtplatform.tester.MockingBinder;
import online.draughts.rus.testutil.ViewTestBase;
import online.draughts.rus.testutil.ViewTestModule;
import org.jukito.JukitoRunner;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.verify;


/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 10:57
 */
@RunWith(JukitoRunner.class)
public class HomeViewTest extends ViewTestBase {

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
    homeView.onNewGame(null);
    verify(homeView.playShowPanel).setVisible(true);
    verify(homeView.play).setVisible(false);

    homeView.onNewGame(null);
    verify(homeView.playShowPanel).setVisible(false);
    verify(homeView.play).setVisible(true);
  }
}