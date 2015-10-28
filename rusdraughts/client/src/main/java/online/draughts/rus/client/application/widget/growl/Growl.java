package online.draughts.rus.client.application.widget.growl;

import com.google.gwt.core.client.GWT;
import online.draughts.rus.shared.locale.DraughtsMessages;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlOptions;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.10.15
 * Time: 21:45
 */
public class Growl {

  public static final DraughtsMessages messages = GWT.create(DraughtsMessages.class);

  public static void growlNotif(String message) {
    growl(messages.notification(), " " + message);
  }

  private static void growl(String notification, String message) {
    GrowlOptions growlOptions = new GrowlOptions();
    growlOptions.setAllowDismiss(false);
    org.gwtbootstrap3.extras.growl.client.ui.Growl.growl(notification, message, growlOptions);
  }

}
