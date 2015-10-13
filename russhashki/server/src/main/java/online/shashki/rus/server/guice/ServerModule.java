package online.shashki.rus.server.guice;

import com.google.inject.AbstractModule;
import online.shashki.rus.server.api.ApiModule;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.10.15
 * Time: 14:03
 */
public class ServerModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new ApiModule());
    install(new DispatchServletModule());
    install(new DbModule());
    install(new ServerHandlerModule());
  }
}
