package online.shashki.rus.server.listener;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import online.shashki.rus.server.config.ServerModule;
import online.shashki.rus.server.config.ServerServletModule;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.10.15
 * Time: 20:15
 */
@WebListener
public class GuiceListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServerModule(), new ServerServletModule());
  }

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    super.contextInitialized(servletContextEvent);

//    Injector injector = getInjector();
//
//    PlayerDao playerDao = new PlayerDaoImpl();
//    injector.injectMembers(playerDao);
//    PlayerServiceImpl playerService = new PlayerServiceImpl(playerDao);
//    injector.injectMembers(playerService);
//
//    // сбрасываем всех пользователей как не залогиненных при старте контейнера
//    final List<Player> playerList = playerService.findAll();
//    for (Player player : playerList) {
//      player.setOnline(false);
//      player.setPlaying(false);
//      player.setLoggedIn(false);
//      playerService.save(player, true);
//    }
  }
}
