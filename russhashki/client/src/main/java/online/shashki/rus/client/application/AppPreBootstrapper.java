package online.shashki.rus.client.application;

import com.gwtplatform.mvp.client.PreBootstrapper;
import online.shashki.rus.client.utils.DebugUtils;
import online.shashki.rus.client.utils.SHLog;

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
    SHLog.info("Error handling initialized");
  }
}
