package online.shashki.rus.client.application.security;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.DefaultGatekeeper;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 21.09.15
 * Time: 11:39
 */
@DefaultGatekeeper
public class LoggedInGatekeeper implements Gatekeeper {
  private final CurrentSession currentSession;

  @Inject
  LoggedInGatekeeper(CurrentSession currentSession) {
    this.currentSession = currentSession;
  }

  @Override
  public boolean canReveal() {
    return currentSession.isLoggedIn();
  }
}
