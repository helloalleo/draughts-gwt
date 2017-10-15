package online.draughts.rus.client.application.widget.growl;

import com.google.gwt.core.client.GWT;
import online.draughts.rus.shared.locale.DraughtsMessages;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.10.15
 * Time: 21:45
 */
public class Growl {

  public static final DraughtsMessages messages = GWT.create(DraughtsMessages.class);

  public static void growlNotif(String message) {
    growl(message);
  }

  private static void growl(String message) {
    NotifySettings notifySettings = NotifySettings.newSettings();
    notifySettings.setAllowDismiss(false);
    Notify.notify("", message, notifySettings);
  }
}
