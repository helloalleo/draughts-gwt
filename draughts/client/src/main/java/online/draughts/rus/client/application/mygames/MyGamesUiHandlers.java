package online.draughts.rus.client.application.mygames;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyGamesUiHandlers extends UiHandlers {
  void getMoreGames(boolean myGames, int newPageSize);
}
