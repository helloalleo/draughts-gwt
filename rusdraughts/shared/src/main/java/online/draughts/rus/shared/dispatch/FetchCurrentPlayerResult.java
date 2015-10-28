package online.draughts.rus.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;
import online.draughts.rus.shared.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.10.15
 * Time: 15:21
 */
public class FetchCurrentPlayerResult implements Result {

  private Player player;

  public FetchCurrentPlayerResult() {
  }

  public FetchCurrentPlayerResult(Player player) {
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }
}
