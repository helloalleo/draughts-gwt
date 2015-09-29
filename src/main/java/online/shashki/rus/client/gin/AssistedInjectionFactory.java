package online.shashki.rus.client.gin;

import online.shashki.rus.client.application.component.playitem.PlayItemPresenter;
import online.shashki.rus.client.application.component.playrow.PlayRowPresenter;
import online.shashki.rus.client.application.component.playshowpanel.PlayShowPanelPresenter;
import online.shashki.rus.shared.model.Game;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.09.15
 * Time: 11:13
 */
public interface AssistedInjectionFactory {

  PlayItemPresenter createPlayItemPresenter(Game game);
  PlayRowPresenter createPlayRowPresenter(List<Game> gameList);
  PlayShowPanelPresenter createPlayShowPanelPresenter(List<Game> gameList);
}
