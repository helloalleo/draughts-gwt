package online.draughts.rus.client.application.play.messanger;

import com.gwtplatform.mvp.client.UiHandlers;

interface MessengerUiHandlers extends UiHandlers {
  void sendMessage(String message);

  void resetOpponent();
}
