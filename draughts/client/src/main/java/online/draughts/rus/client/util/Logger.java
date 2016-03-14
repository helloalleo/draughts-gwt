package online.draughts.rus.client.util;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 14.11.15
 * Time: 21:21
 */
public class Logger {

  private static Logger INSTANCE;
  private Log log;

  private Logger() {
    this.log = new DTLogImpl();
  }

  private static Logger getInstance() {
    return LoggerHolder.INSTANCE;
  }

  public static void debug(String message) {
    getInstance().log.debug(message);
  }

  public static void debug(int msg) {
    debug(String.valueOf(msg));
  }

  /**
   * SingletonHolder is loaded on the first execution of Singleton.getInstance()
   * or the first access to SingletonHolder.INSTANCE, not before.
   */
  private static class LoggerHolder {
    private static final Logger INSTANCE = new Logger();
  }
}
