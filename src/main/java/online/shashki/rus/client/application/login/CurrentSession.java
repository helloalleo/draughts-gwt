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
  private Shashist currentPlayer;

  public boolean isLoggedIn() {
    return currentPlayer != null;
  }

  public Shashist getCurrentPlayer() {
    return currentPlayer;
  }

  public CurrentSession setCurrentPlayer(Shashist currentPlayer) {
    this.currentPlayer = currentPlayer;
    return this;
  }
}
