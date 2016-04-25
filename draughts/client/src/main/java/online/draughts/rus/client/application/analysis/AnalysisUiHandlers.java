package online.draughts.rus.client.application.analysis;

import com.gwtplatform.mvp.client.UiHandlers;

interface AnalysisUiHandlers extends UiHandlers {
  boolean isMyTurn();

  void checkWinner();

  void gameStuck(boolean isWhite);
}
