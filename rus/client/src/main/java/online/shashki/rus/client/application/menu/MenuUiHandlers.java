package online.shashki.rus.client.application.menu;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.mvp.client.UiHandlers;

interface MenuUiHandlers extends UiHandlers {
  void displayPage(String token);

  void isAuthenticated(AsyncCallback<Boolean> async);
}
