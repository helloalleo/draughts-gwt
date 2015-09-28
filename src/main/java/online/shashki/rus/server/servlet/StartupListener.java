package online.shashki.rus.server.servlet;

import online.shashki.rus.server.service.ShashistService;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.09.15
 * Time: 23:28
 */
@WebListener
public class StartupListener implements ServletContextListener {

  @Inject
  private ShashistService shashistService;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    // сбрасываем всех пользователей как не залогиненных при старте контейнера
    shashistService.findAll().parallelStream().forEach(shashist -> {
      shashist.setOnline(false);
      shashist.setPlaying(false);
      shashist.setLoggedIn(false);
      shashistService.edit(shashist);
    });
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
