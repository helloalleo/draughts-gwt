package online.draughts.rus.server.websocket.game.message;

import online.draughts.rus.server.util.Utils;
import online.draughts.rus.shared.model.GameMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.10.14
 * Time: 23:16
 */
public class GameMessageDecoder implements Decoder.Text<GameMessage> {
  @Override
  public void init(EndpointConfig endpointConfig) {
  }

  @Override
  public void destroy() {
  }

  @Override
  public GameMessage decode(String s) throws DecodeException {
    try {
      return Utils.deserializeGameMessageFromJson(s);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean willDecode(String s) {
    GameMessage gameMessage;
    try {
      gameMessage = Utils.deserializeGameMessageFromJson(s);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return Arrays.asList(GameMessage.MessageType.values()).contains(gameMessage.getMessageType());
  }

}
