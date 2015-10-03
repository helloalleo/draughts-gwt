package online.shashki.rus.client.application.security;

import com.google.inject.Singleton;
import online.shashki.rus.client.utils.SHCookies;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 21.09.15
 * Time: 12:01
 */
@Singleton
public class CurrentSession {
  private boolean loggedIn;

  public void setLoggedIn(Boolean loggedIn) {
    this.loggedIn = loggedIn;
    if (loggedIn) {
      SHCookies.login();
    }
  }

  public Boolean isLoggedIn() {
    return loggedIn || isCookieLoggedIn();
  }

  private Boolean isCookieLoggedIn() {
    return SHCookies.isLoggedIn();
  }
}
