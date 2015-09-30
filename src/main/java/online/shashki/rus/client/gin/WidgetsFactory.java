package online.shashki.rus.client.gin;

import online.shashki.rus.client.application.component.playshowpanel.PlayShowPanelPresenter;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.09.15
 * Time: 11:13
 */
public interface WidgetsFactory {

//  PlayItemPresenter createPlayItemPresenter(Game game);
//  PlayRowPresenter createPlayRowPresenter(List<Game> gameList);
//  PlayShowPanelPresenter createPlayShowPanelPresenter(List<Game> gameList);
  PlayShowPanelPresenter createPlayShowPanelPresenter(String testPojo);
}
