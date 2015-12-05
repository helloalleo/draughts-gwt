package online.draughts.rus.shared.model;


import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.14
 * Time: 15:49
 */
@Entity
public class GameMessage extends PersistableObjectImpl {

  private Key<Player> sender;

  private Key<Player> receiver;

  private String message;

  private MessageType messageType;

  private String data;

  private Date sentDate;

  private Ref<Move> move;

  private Key<Game> game;

  @Ignore
  private List<Player> playerList;

  public Key<Player> getSender() {
    return sender;
  }

  public void setSender(Long senderId) {
    this.sender = Key.create(Player.class, senderId);
  }

  public Key<Player> getReceiver() {
    return receiver;
  }

  public void setReceiver(Long receiverId) {
    this.receiver = Key.create(Player.class, receiverId);
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Date getSentDate() {
    return sentDate;
  }

  public void setSentDate(Date sentDate) {
    this.sentDate = sentDate;
  }

  public MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }

  public Ref<Move> getMove() {
    return move;
  }

  public void setMove(Move move) {
    this.move = Ref.create(move);
  }

  public Key<Game> getGame() {
    return game;
  }

  public void setGame(Long gameId) {
    this.game = Key.create(Game.class, gameId);
  }

  public void setPlayerList(List<Player> playerList) {
    this.playerList = playerList;
  }

  public List<Player> getPlayerList() {
    return playerList;
  }

  @Override
  public String toString() {
    return "GameMessage{" +
        "sender=" + sender +
        ", receiver=" + receiver +
        ", message='" + message + '\'' +
        ", messageType=" + messageType +
        ", data='" + data + '\'' +
        ", sentDate=" + sentDate +
        ", move=" + move +
        ", game=" + game +
        ", playerList=" + playerList +
        '}';
  }

  public enum MessageType implements IsSerializable {

    CHAT_MESSAGE,
    CHAT_PRIVATE_MESSAGE,
    USER_LIST_UPDATE,
    PLAY_INVITE,
    PLAY_REJECT_INVITE,
    PLAY_ALREADY_PLAYING,
    PLAY_START,
    PLAY_OPPONENT_MOVE,
    PLAY_CANCEL_MOVE,
    PLAY_CANCEL_MOVE_RESPONSE,
    PLAY_PROPOSE_DRAW,
    PLAY_ACCEPT_DRAW,
    PLAY_END,
    PLAY_SURRENDER,
    NOTIFICATION_ADDED_TO_FAVORITE,
    CHANNEL_CLOSE,
    PLAYER_REGISTER;
  }
}
