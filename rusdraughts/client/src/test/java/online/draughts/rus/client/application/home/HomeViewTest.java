package online.draughts.rus.client.application.home;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.tester.MockFactory;
import com.gwtplatform.tester.MockingBinder;
import online.draughts.rus.client.application.component.playshowpanel.PlayShowPanel;
import online.draughts.rus.testutil.MockitoMockFactory;
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

  @Inject
  HomeView homeView;

  @AfterClass
  public static void tearDown() {
    GWTMockUtilities.restore();
  }

  @Test
  public void testHomeView() {
    homeView.onNewGame(null);
    verify(homeView.playShowPanel).setVisible(true);
    verify(homeView.play).setVisible(false);

    homeView.onNewGame(null);
    verify(homeView.playShowPanel).setVisible(false);
    verify(homeView.play).setVisible(true);
  }

  public static class Module extends ViewTestModule {

    @Override
    protected void configureViewTest() {
      bind(HomeView.Binder.class).to(HomeTestBinder.class);
      bind(PlayShowPanel.Binder.class).to(PlayShowPanelTestBinder.class);
    }

    static class HomeTestBinder extends MockingBinder<Widget, HomeView> implements HomeView.Binder {
      @Inject
      public HomeTestBinder(final MockitoMockFactory mockitoMockFactory) {
        super(Widget.class, mockitoMockFactory);
      }
    }

    static class PlayShowPanelTestBinder extends MockingBinder<HTMLPanel, PlayShowPanel> implements PlayShowPanel.Binder {

      @Inject
      public PlayShowPanelTestBinder(MockFactory mockFactory) {
        super(HTMLPanel.class, mockFactory);
      }
    }
  }
}