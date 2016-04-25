package online.draughts.rus.client.application.widget.popup;

import com.gwtplatform.mvp.client.UiHandlers;

interface DraughtsPlayerUiHandlers extends UiHandlers {

  void checkWinner();

  void gameStuck(boolean isWhite);

  void toggleTurn(boolean turn);
}
