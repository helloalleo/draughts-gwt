package online.draughts.rus.client.application;

import com.gwtplatform.mvp.client.PreBootstrapper;
import online.draughts.rus.client.util.DebugUtils;
import online.draughts.rus.client.util.DTLog;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.10.15
 * Time: 7:58
 */
public class AppPreBootstrapper implements PreBootstrapper {
  @Override
  public void onPreBootstrap() {
    DebugUtils.initDebugAndErrorHandling();
    DTLog.info("Error handling initialized");
  }
}
