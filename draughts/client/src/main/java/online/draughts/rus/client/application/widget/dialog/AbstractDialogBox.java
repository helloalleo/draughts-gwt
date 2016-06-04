package online.draughts.rus.client.application.widget.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DialogBox;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.client.util.CookiesImpl;
import online.draughts.rus.shared.locale.DraughtsMessages;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.06.16
 * Time: 6:34
 */
abstract class AbstractDialogBox extends DialogBox implements BaseDialogBox {

  int WIDTH = 400;
  int HEIGHT = 60;
  final DraughtsMessages messages = GWT.create(DraughtsMessages.class);
  final AppResources resources = GWT.create(AppResources.class);
  final Cookies cookies = GWT.create(CookiesImpl.class);

  AbstractDialogBox() {
    setAnimationEnabled(true);
    getElement().addClassName(resources.style().dialogBox());
  }
}
