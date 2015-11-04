package online.draughts.rus.client.util;

import com.google.gwt.core.client.GWT;
import online.draughts.rus.shared.config.ClientConfiguration;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 25.11.14
 * Time: 8:29
 */
public class DTLogImpl implements Log {

  private final String DEBUG_PREFIX = "DEBUG: ";
  private final String WARN_PREFIX = "WARN: ";
  private final String ERROR_PREFIX = "ERROR: ";
  private final String INFO_PREFIX = "INFO: ";
  private final String PROD_PREFIX = "PROD: ";
  private final String DEBUG_LEVEL_NAME = "debug";
  private final String WARN_LEVEL_NAME = "warn";
  private final String ERROR_LEVEL_NAME = "error";
  private final String INFO_LEVEL_NAME = "info";
  private final String PROD_LEVEL_NAME = "prod";
  private final LogLevels debugLevel = new LogLevels(DEBUG_LEVEL_NAME, 1);
  private final LogLevels warnLevel = new LogLevels(WARN_LEVEL_NAME, 2);
  private final LogLevels errorLevel = new LogLevels(ERROR_LEVEL_NAME, 3);
  private final LogLevels infoLevel = new LogLevels(INFO_LEVEL_NAME, 4);
  private final LogLevels prodLevel = new LogLevels(PROD_LEVEL_NAME, 5);

  private ClientConfiguration configuration = GWT.create(ClientConfiguration.class);

  private void log(String prefix, String message, Throwable e) {
    if (Boolean.valueOf(configuration.debug())) {
      if (e == null) {
        GWT.log(prefix + message);
      } else {
        GWT.log(prefix + message, e);
      }
    }
  }

  public void error(String message, Throwable e) {
    if (!showLog(errorLevel.level)) {
      return;
    }
    log(ERROR_PREFIX, message, e);
  }

  public void debug(String message) {
    if (!showLog(debugLevel.level)) {
      return;
    }
    log(DEBUG_PREFIX, message, null);
  }

  public void warn(String message) {
    if (!showLog(warnLevel.level)) {
      return;
    }
    log(WARN_PREFIX, message, null);
  }

  public void info(String message) {
    if (!showLog(infoLevel.level)) {
      return;
    }
    log(INFO_PREFIX, message, null);
  }

  public void prod(String message) {
    if (!showLog(prodLevel.level)) {
      return;
    }
    log(PROD_PREFIX, message, null);
  }

  private boolean showLog(int level) {
    switch (configuration.level()) {
      case DEBUG_LEVEL_NAME:
        return Arrays.asList(1, 2, 3, 4).contains(level);
      case WARN_LEVEL_NAME:
        return Arrays.asList(2, 3, 4).contains(level);
      case ERROR_LEVEL_NAME:
        return Arrays.asList(3, 4).contains(level);
      case INFO_LEVEL_NAME:
        return Collections.singleton(4).contains(level);
      case PROD_LEVEL_NAME:
        return Collections.singleton(5).contains(level);
      default:
        return Collections.singletonList(3).contains(level);
    }
  }

  private class LogLevels {
    public String name;
    public int level;

    public LogLevels(String name, int level) {
      this.name = name;
      this.level = level;
    }
  }
}
