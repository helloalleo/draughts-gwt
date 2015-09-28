package online.shashki.rus.client.utils;

import com.google.gwt.core.client.GWT;
import online.shashki.rus.shared.config.ShashkiConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 25.11.14
 * Time: 8:29
 */
public class SHLog {

  private static ShashkiConfiguration configuration = GWT.create(ShashkiConfiguration.class);

  public static void log(String message, Throwable e) {
    if (Boolean.valueOf(configuration.debug())) {
      GWT.log(message, e);
    }
  }

  public static void log(String message) {
    log(message, null);
  }
}
