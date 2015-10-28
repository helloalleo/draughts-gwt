package online.draughts.rus.server.guice;

import com.google.inject.AbstractModule;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.10.15
 * Time: 14:03
 */
public class ServerModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new RestModule());
    install(new DispatchServletModule());
    install(new DbModule());
    install(new ServerHandlerModule());
  }
}
