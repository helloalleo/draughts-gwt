package online.draughts.rus.client.application.home;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.ResetPresentersEvent;
import online.draughts.rus.testutil.PresenterTestBase;
import online.draughts.rus.testutil.PresenterTestModule;
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
public class HomePresenterTest extends PresenterTestBase {

  @Inject
  HomePresenter presenter;
  @Inject
  HomePresenter.MyView view;

  @Test
  public void onReveal(HomePresenter.MyView view) {
    assertNotNull(true);
  }

  @Test
  public void testPlay() {
    eventBus.fireEvent(new ResetPresentersEvent());
  }

  public static class Module extends PresenterTestModule {
    @Override
    protected void configurePresenterTest() {
    }
  }
}