package online.shashki.rus.server.config;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import online.shashki.rus.server.service.GameMessageServiceImpl;
import online.shashki.rus.server.service.GameServiceImpl;
import online.shashki.rus.server.service.PlayerServiceImpl;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 06.10.15
 * Time: 8:55
 */
public class ServerServletModule extends ServletModule {

  @Override
  protected void configureServlets() {
    install(new JpaPersistModule("shashki64PU"));

    filter("/*").through(PersistFilter.class);

    serve("/Application/ProfileRpcService").with(PlayerServiceImpl.class);
    serve("/Application/GameMessageRpcService").with(GameMessageServiceImpl.class);
    serve("/Application/GameRpcService").with(GameServiceImpl.class);
  }
}
