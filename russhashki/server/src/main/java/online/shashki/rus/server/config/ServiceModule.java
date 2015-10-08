/*
 * Created by IntelliJ IDEA.
 * User: alekspo
 * Date: 04.10.15
 * Time: 19:44
 */
package online.shashki.rus.server.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import online.shashki.rus.server.service.GameMessageServiceImpl;
import online.shashki.rus.server.service.GameServiceImpl;
import online.shashki.rus.server.service.PlayerServiceImpl;
import online.shashki.rus.shared.service.GameMessageService;
import online.shashki.rus.shared.service.GameService;
import online.shashki.rus.shared.service.PlayerService;

public class ServiceModule extends AbstractModule {
  protected void configure() {
    bind(PlayerService.class).to(PlayerServiceImpl.class).in(Singleton.class);
    bind(GameService.class).to(GameServiceImpl.class).in(Singleton.class);
    bind(GameMessageService.class).to(GameMessageServiceImpl.class).in(Singleton.class);
  }
}
