package online.draughts.rus.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.Result;
import online.draughts.rus.shared.dto.PlayerDto;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.10.15
 * Time: 15:21
 */
public class FetchCurrentPlayerResult implements Result {

  private PlayerDto player;

  public FetchCurrentPlayerResult() {
  }

  public FetchCurrentPlayerResult(PlayerDto player) {
    this.player = player;
  }

  public PlayerDto getPlayer() {
    return player;
  }
}
