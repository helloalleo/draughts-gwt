package online.draughts.rus.client.gin;

import online.draughts.rus.client.application.home.*;
import online.draughts.rus.shared.dto.GameDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.10.15
 * Time: 5:29
 */
public interface PlayShowPanelFactory {

  PlayItem createItem(int gamesInRow, GameDto game, ShowPanelEnum showPanelEnum, GamesPanelPresentable gamesPanel);

  PlayShowPanel createShowPanel(ShowPanelEnum showPanelEnum, GamesPanelViewable gamesView);
}
