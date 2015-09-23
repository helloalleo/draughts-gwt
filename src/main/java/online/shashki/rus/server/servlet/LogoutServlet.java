package online.shashki.rus.server.servlet;

import online.shashki.rus.server.config.ServerConfiguration;
import online.shashki.rus.server.service.ShashistService;
import online.shashki.rus.shared.model.entity.ShashistEntity;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 05.12.14
 * Time: 21:46
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

  @Inject
  private ShashistService shashistService;
  @Inject
  private ServerConfiguration configuration;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ShashistEntity shashistEntity = shashistService.findBySessionId(request.getSession().getId());
    shashistEntity.setOnline(false);
    shashistEntity.setPlaying(false);
    shashistEntity.setOnline(false);
    shashistService.edit(shashistEntity);

    request.getSession().invalidate();
    response.sendRedirect(configuration.getContext());
  }

}
