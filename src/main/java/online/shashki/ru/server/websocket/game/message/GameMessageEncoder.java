package online.shashki.ru.server.websocket.game.message;

import online.shashki.ru.server.util.Util;
import online.shashki.ru.shared.model.GameMessage;

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
    return Util.serializePlayerMessageToJson(gameMessage);
  }

  @Override
  public void init(EndpointConfig endpointConfig) {
  }

  @Override
  public void destroy() {
  }

}
