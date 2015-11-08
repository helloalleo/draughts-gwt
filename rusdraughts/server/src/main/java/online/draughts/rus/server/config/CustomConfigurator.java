package online.draughts.rus.server.config;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.websocket.server.ServerEndpointConfig;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.11.15
 * Time: 19:36
 */
public class CustomConfigurator extends ServerEndpointConfig.Configurator {

  @Inject
  private static Injector injector;

  public <T> T getEndpointInstance(Class<T> endpointClass) {
    return injector.getInstance(endpointClass);
  }
}