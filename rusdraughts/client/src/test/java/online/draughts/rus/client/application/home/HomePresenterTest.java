package online.draughts.rus.client.application.home;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.11.15
 * Time: 19:49
 */
@RunWith(JukitoRunner.class)
public class HomePresenterTest {

  @Test
  public void onReveal(HomePresenter.MyView view) {
    assertNotNull(true);
  }

  public static class Module extends JukitoModule {
    protected void configureTest() {
    }
  }
}