package online.shashki.rus.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.10.14
 * Time: 22:51
 */
public interface Message extends PersistableObject {

  Shashist getSender();

  void setSender(Shashist sender);

  Shashist getReceiver();

  void setReceiver(Shashist receiver);

  MessageType getMessageType();

  void setMessageType(MessageType messageType);

  String getMessage();

  void setMessage(String message);

  String getData();

  void setData(String data);

  Date getSentDate();

  void setSentDate(Date sentDate);

  enum MessageType implements IsSerializable {

    CHAT_MESSAGE,
    CHAT_PRIVATE_MESSAGE,
    USER_LIST_UPDATE,
    PLAY_INVITE,
    PLAY_REJECT_INVITE,
    PLAY_ALREADY_PLAYING,
    PLAY_START,
    PLAY_MOVE,
    PLAY_CANCEL_MOVE,
    PLAY_CANCEL_MOVE_RESPONSE,
    PLAY_PROPOSE_DRAW,
    PLAY_ACCEPT_DRAW,
    PLAY_END,
    PLAY_SURRENDER,
    PLAYER_REGISTER
  }
}
