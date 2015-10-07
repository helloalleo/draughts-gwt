package online.shashki.rus.server.config;

import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import online.shashki.rus.server.dao.GameDao;
import online.shashki.rus.server.dao.GameMessageDao;
import online.shashki.rus.server.dao.PlayerDao;
import online.shashki.rus.server.dao.impl.GameDaoImpl;
import online.shashki.rus.server.dao.impl.GameMessageDaoImpl;
import online.shashki.rus.server.dao.impl.PlayerDaoImpl;
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

  private static final String SHASHKI_PU = "shashki64PU";
  private final String CONTEXT = "/rus";

  @Override
  protected void configureServlets() {
    install(new JpaPersistModule(SHASHKI_PU));
//    filter(CONTEXT + "/*").through(PersistFilter.class);

    bind(PlayerDao.class).to(PlayerDaoImpl.class);
    bind(GameDao.class).to(GameDaoImpl.class);
    bind(GameMessageDao.class).to(GameMessageDaoImpl.class);

    serve(CONTEXT + "/Application/ProfileService").with(PlayerServiceImpl.class);
    serve(CONTEXT + "/Application/GameService").with(GameServiceImpl.class);
    serve(CONTEXT + "/Application/GameMessageService").with(GameMessageServiceImpl.class);
  }
}
