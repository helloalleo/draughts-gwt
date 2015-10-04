package online.shashki.rus.client.application.home;

import com.gwtplatform.mvp.client.UiHandlers;

interface HomeUiHandlers extends UiHandlers {

  void getMoreGames(boolean myGames, int newPageSize);
}
