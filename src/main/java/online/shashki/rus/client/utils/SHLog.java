package online.shashki.rus.client.utils;

import com.google.gwt.core.client.GWT;
import online.shashki.rus.shared.config.ShashkiConfiguration;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 25.11.14
 * Time: 8:29
 */
public class SHLog {

  private static final String DEBUG_PREFIX = "DEBUG: ";
  private static final String INFO_PREFIX = "INFO: ";
  private static final String WARN_PREFIX = "WARN: ";
  private static final String ERROR_PREFIX = "ERROR: ";
  private static final String DEBUG_LEVEL_NAME = "debug";
  private static final String WARN_LEVEL_NAME = "warn";
  private static final String INFO_LEVEL_NAME = "info";
  private static final String ERROR_LEVEL_NAME = "error";
  private static final LogLevels debugLevel = new LogLevels(DEBUG_LEVEL_NAME, 1);
  private static final LogLevels warnLevel = new LogLevels(WARN_LEVEL_NAME, 2);
  private static final LogLevels infoLevel = new LogLevels(INFO_LEVEL_NAME, 3);
  private static final LogLevels errorLevel = new LogLevels(ERROR_LEVEL_NAME, 4);
  private static ShashkiConfiguration configuration = GWT.create(ShashkiConfiguration.class);

  private static void log(String prefix, String message, Throwable e) {
    if (Boolean.valueOf(configuration.debug())) {
      if (e == null) {
        GWT.log(prefix + message);
      } else {
        GWT.log(prefix + message, e);
      }
    }
  }

  public static void error(String message, Throwable e) {
    if (!showLog(errorLevel.level)) {
      return;
    }
    log(ERROR_PREFIX, message, e);
  }

  public static void debug(String message) {
    if (!showLog(debugLevel.level)) {
      return;
    }
    log(DEBUG_PREFIX, message, null);
  }

  public static void warn(String message) {
    if (!showLog(warnLevel.level)) {
      return;
    }
    log(WARN_PREFIX, message, null);
  }

  public static void info(String message) {
    if (!showLog(infoLevel.level)) {
      return;
    }
    log(INFO_PREFIX, message, null);
  }

  private static boolean showLog(int level) {
    switch (configuration.level()) {
      case DEBUG_LEVEL_NAME:
        return Arrays.asList(1, 2, 3).contains(level);
      case WARN_LEVEL_NAME:
        return Arrays.asList(2, 3).contains(level);
      case INFO_LEVEL_NAME:
        return Collections.singletonList(3).contains(level);
      case ERROR_LEVEL_NAME:
        return Collections.singleton(4).contains(level);
      default:
        return Collections.singletonList(3).contains(level);
    }
  }

  private static class LogLevels {
    public String name;
    public int level;

    public LogLevels(String name, int level) {
      this.name = name;
      this.level = level;
    }
  }
}
