package online.draughts.rus.client.gin;

import online.draughts.rus.client.application.component.playshowpanel.PlayItem;
import online.draughts.rus.shared.model.Game;
import online.draughts.rus.shared.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.10.15
 * Time: 5:29
 */
public interface PlayShowPanelFactory {

  PlayItem createItem(Player player, Game game);
}
