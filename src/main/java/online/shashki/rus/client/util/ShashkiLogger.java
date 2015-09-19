package online.shashki.rus.client.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 25.11.14
 * Time: 8:29
 */
public class ShashkiLogger {

  private Logger logger = Logger.getLogger(ShashkiLogger.class.getName());

  public void log(Level level, String msg) {
    logger.log(level, msg);
  }

  public void warning(String msg) {
    logger.warning(msg);
  }

  public void info(String msg) {
    logger.info(msg);
  }

  public ShashkiLogger() {}

}
