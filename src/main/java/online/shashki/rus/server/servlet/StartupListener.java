package online.shashki.rus.server.servlet;

import online.shashki.rus.server.service.ProfileRpcServiceImpl;
import online.shashki.rus.shared.model.Shashist;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.09.15
 * Time: 23:28
 */
@WebListener
public class StartupListener implements ServletContextListener {

  @Inject
  private ProfileRpcServiceImpl shashistService;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    // сбрасываем всех пользователей как не залогиненных при старте контейнера
    final List<Shashist> shashistList = shashistService.findAll();
    for (Shashist shashist : shashistList) {
      shashist.setOnline(false);
      shashist.setPlaying(false);
      shashist.setLoggedIn(false);
      shashistService.save(shashist, true);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
