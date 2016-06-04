package online.draughts.rus.server.resource;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import online.draughts.rus.server.service.MailService;
import online.draughts.rus.shared.resource.ErrorHandlerResource;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.06.16
 * Time: 8:05
 */
@RequestScoped
public class ErrorHandlerResourceImpl implements ErrorHandlerResource {

  private final MailService mailService;

  @Inject
  public ErrorHandlerResourceImpl(MailService mailService) {
    this.mailService = mailService;
  }

  @Override
  public void postError(String error, Long senderId) {
    mailService.sendToAdmins(error, senderId);
  }
}
