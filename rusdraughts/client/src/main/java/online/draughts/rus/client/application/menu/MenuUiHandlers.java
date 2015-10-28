package online.draughts.rus.client.application.menu;

import com.gwtplatform.mvp.client.UiHandlers;

interface MenuUiHandlers extends UiHandlers {
  void displayPage(String token);

  boolean isLoggedIn();
}
