package online.draughts.rus.client.application;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.ResetPresentersEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.testutil.PresenterTestBase;
import online.draughts.rus.testutil.PresenterTestModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 15:44
 */
@RunWith(JukitoRunner.class)
public class ApplicationPresenterTest extends PresenterTestBase {

  public static class Module extends PresenterTestModule {

    @Override
    protected void configurePresenterTest() {
    }
  }

  @Inject
  ApplicationPresenter presenter;
  @Inject
  ApplicationPresenter.MyView view;

  @Before
  public void setUp() throws Exception {

  }

  @Test
  public void showActiveNavigationItemWhenPlaceRequest() {
    PlaceRequest request = new PlaceRequest.Builder().nameToken(NameTokens.HOME_PAGE).build();
    placeManager.revealPlace(request);
    when(placeManager.getCurrentPlaceRequest()).thenReturn(request);
    eventBus.fireEvent(new ResetPresentersEvent());
  }
}