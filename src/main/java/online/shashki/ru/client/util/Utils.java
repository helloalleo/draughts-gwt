package online.shashki.ru.client.util;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import online.shashki.ru.shared.locale.ShashkiConstants;
import online.shashki.ru.shared.model.GameEnds;
import online.shashki.ru.shared.model.GameMessage;
import online.shashki.ru.shared.websocket.message.MessageFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 22:58
 */
public class Utils {

  private MessageFactory messageFactory = GWT.create(MessageFactory.class);
  public static final String MESSAGE_SEPARATOR = " > ";

  public GameMessage makePlayerMessage() {
    AutoBean<GameMessage> playerMessageAutoBean = messageFactory.gameMessage();
    return playerMessageAutoBean.as();
  }

  public static String serializePlayerMessageToJson(GameMessage message) {
    MessageFactory chatFactory = GWT.create(MessageFactory.class);
    AutoBean<GameMessage> bean = chatFactory.create(GameMessage.class, message);
    return AutoBeanCodex.encode(bean).getPayload();
  }

  public static String hashString(String in) {
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance("SHA1");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return "";
    }
    byte bDigest[] = messageDigest.digest(in.getBytes());
    BigInteger bi = new BigInteger(bDigest);
    return bi.toString(16);
  }


  public static GameMessage deserializeFromJson(String json) {
    MessageFactory messageFactory = GWT.create(MessageFactory.class);
    AutoBean<GameMessage> bean = AutoBeanCodex.decode(messageFactory, GameMessage.class, json);
    return bean.as();
  }

  public static String getGameEnd(GameEnds gameEnd, ShashkiConstants constants) {
    if (gameEnd == null) {
      return "";
    }
    String winner = "";
    switch (gameEnd) {
      case BLACK_WON:
        winner = constants.BLACK_WON();
        break;
      case WHITE_WON:
        winner = constants.WHITE_WON();
        break;
      case BLACK_LEFT:
        winner = constants.BLACK_LEFT();
        break;
      case WHITE_LEFT:
        winner = constants.WHITE_LEFT();
        break;
      case SURRENDER_BLACK:
        winner = constants.SURRENDER_BLACK();
        break;
      case SURRENDER_WHITE:
        winner = constants.SURRENDER_WHITE();
        break;
      case DRAW:
        winner = constants.DRAW();
        break;
    }
    return winner;
  }
}
