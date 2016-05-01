package online.draughts.rus.server.message;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 30.04.16
 * Time: 19:51
 */
public class Messages {
  private static ResourceBundle resourceBundleEN = ResourceBundle.getBundle("ServerMessages", Locale.ENGLISH);
  private static ResourceBundle resourceBundleRU = ResourceBundle.getBundle("ServerMessages", Locale.forLanguageTag("ru"));

}
