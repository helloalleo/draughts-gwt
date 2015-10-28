package online.draughts.rus.server.guice;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.websocket.server.ServerEndpointConfig;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 20.10.15
 * Time: 17:31
 */
public class CustomConfigurator extends ServerEndpointConfig.Configurator {

  @Inject
  private static Injector injector;

  public <T> T getEndpointInstance(Class<T> endpointClass) {
    return injector.getInstance(endpointClass);
  }
}