package online.shashki.rus.server.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.10.15
 * Time: 20:15
 */

public class GuiceServletConfig extends GuiceServletContextListener {

  private static final String SHASHKI_PU = "shashki64PU";
  private PersistService persistService;

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new DispatchServletModule(), new ServerModule() /*, new AbstractModule() {
      @Override
      protected void configure() {
        bind(PlayerDao.class).to(PlayerDaoImpl.class);
        bind(GameDao.class).to(GameDaoImpl.class);
        bind(GameMessageDao.class).to(GameMessageDaoImpl.class);

        bind(PlayerService.class).to(PlayerServiceImpl.class);
        bind(GameService.class).to(GameServiceImpl.class);
        bind(GameMessageService.class).to(GameMessageServiceImpl.class);
      }
    }*/
    );
  }

//  @Override
//  public void contextInitialized(ServletContextEvent servletContextEvent) {
////    super.contextInitialized(servletContextEvent);
////    persistService = getInjector().getInstance(PersistService.class);
////    try {
////      persistService.start();
////    } catch (Exception e) {
////      e.printStackTrace();
////    }
//
////    Injector injector = getInjector();
////
////    PlayerDao playerDao = new PlayerDaoImpl();
////    injector.injectMembers(playerDao);
////    PlayerServiceImpl playerService = new PlayerServiceImpl(playerDao);
////    injector.injectMembers(playerService);
////
////    // сбрасываем всех пользователей как не залогиненных при старте контейнера
////    final List<Player> playerList = playerService.findAll();
////    for (Player player : playerList) {
////      player.setOnline(false);
////      player.setPlaying(false);
////      player.setLoggedIn(false);
////      playerService.save(player, true);
////    }
//  }

//  @Override
//  public void contextDestroyed(ServletContextEvent contextEvent) {
////    if (persistService != null) {
////      persistService.stop();
////    }
//  }
}
