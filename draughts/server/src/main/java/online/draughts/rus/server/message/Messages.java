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
  private static final ResourceBundle resourceBundleEN = ResourceBundle.getBundle("ServerMessages", Locale.ENGLISH);
  private static final ResourceBundle resourceBundleRU = ResourceBundle.getBundle("ServerMessages", Locale.forLanguageTag("ru"));

  public static final String SERVER_ERROR = resourceBundleEN.getString("server_error");
  public static final String MESSAGE_TO_ADMINS = resourceBundleEN.getString("message_to_admins");
  public static final String NEW_APPLY_FOR_COACHING = resourceBundleEN.getString("new_apply_for_coaching");
}
