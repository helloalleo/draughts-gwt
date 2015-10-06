/*
 * Created by IntelliJ IDEA.
 * User: alekspo
 * Date: 04.10.15
 * Time: 19:44
 */
package online.shashki.rus.server.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import online.shashki.rus.client.service.GameMessageRpcService;
import online.shashki.rus.client.service.GameRpcService;
import online.shashki.rus.client.service.PlayerService;
import online.shashki.rus.server.dao.PlayerDao;
import online.shashki.rus.server.dao.impl.PlayerDaoImpl;
import online.shashki.rus.server.service.GameMessageServiceImpl;
import online.shashki.rus.server.service.GameServiceImpl;
import online.shashki.rus.server.service.PlayerServiceImpl;


public class ServerModule extends AbstractModule {
  protected void configure() {
    bind(PlayerDao.class).to(PlayerDaoImpl.class).in(Singleton.class);

    bind(PlayerService.class).to(PlayerServiceImpl.class).in(Singleton.class);
    bind(GameRpcService.class).to(GameServiceImpl.class).in(Singleton.class);
    bind(GameMessageRpcService.class).to(GameMessageServiceImpl.class).in(Singleton.class);
  }
}
