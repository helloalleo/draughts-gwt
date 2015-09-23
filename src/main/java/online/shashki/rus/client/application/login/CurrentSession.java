package online.shashki.rus.client.application.login;

import com.google.inject.Singleton;
import online.shashki.rus.shared.model.Shashist;

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
  }

  public Boolean isLoggedIn() {
    return loggedIn;
  }
}
