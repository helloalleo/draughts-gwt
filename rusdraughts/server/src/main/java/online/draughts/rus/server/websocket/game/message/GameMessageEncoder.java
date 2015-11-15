package online.draughts.rus.server.websocket.game.message;

import online.draughts.rus.server.util.Utils;
import online.draughts.rus.shared.model.GameMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.10.14
 * Time: 23:20
 */
public class GameMessageEncoder implements Encoder.Text<GameMessage> {
  @Override
  public String encode(GameMessage gameMessage) throws EncodeException {
    return Utils.serializeGameMessageToJson(gameMessage);
  }

  @Override
  public void init(EndpointConfig endpointConfig) {
  }

  @Override
  public void destroy() {
  }

}
