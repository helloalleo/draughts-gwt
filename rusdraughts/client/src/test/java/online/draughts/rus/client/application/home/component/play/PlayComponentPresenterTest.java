package online.draughts.rus.client.application.home.component.play;

import com.google.inject.Inject;
import online.draughts.rus.client.application.home.PlayComponentPresenter;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.11.15
 * Time: 19:46
 */
@RunWith(JukitoRunner.class)
public class PlayComponentPresenterTest {

  @Inject
  PlayComponentPresenter playComponentPresenter;

  @Test
  public void onReveal(PlayComponentPresenter.MyView view) {
  }
}