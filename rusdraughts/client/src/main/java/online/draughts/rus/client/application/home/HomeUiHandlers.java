package online.draughts.rus.client.application.home;

import com.gwtplatform.mvp.client.UiHandlers;
import online.draughts.rus.shared.model.Player;

interface HomeUiHandlers extends UiHandlers {

  void getMoreGames(boolean myGames, int newPageSize);

  Player getPlayer();
}
