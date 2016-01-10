package online.draughts.rus.client.util;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 18:13
 */
public interface Log {

  void error(String message, Throwable e);
  void debug(String message);
  void warn(String message);
  void info(String message);
  void prod(String message);
}
