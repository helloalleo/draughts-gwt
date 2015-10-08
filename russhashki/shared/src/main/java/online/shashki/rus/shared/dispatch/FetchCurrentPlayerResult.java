package online.shashki.rus.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;
import online.shashki.rus.shared.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.10.15
 * Time: 15:21
 */
public class FetchCurrentPlayerResult implements Result {

  private String player;

  public FetchCurrentPlayerResult() {
  }

  public FetchCurrentPlayerResult(String player) {
    this.player = player;
  }

  public String getPlayer() {
    return player;
  }
}
