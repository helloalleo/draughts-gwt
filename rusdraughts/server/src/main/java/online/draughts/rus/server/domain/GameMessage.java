package online.draughts.rus.server.domain;


import com.google.appengine.api.datastore.Text;
import online.draughts.rus.shared.dto.GameMessageDto;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.14
 * Time: 15:49
 */
//@Entity
public class GameMessage extends ModelImpl {

  private Player sender;

  private Player receiver;

  private String message;

  private GameMessageDto.MessageType messageType;

  private String data;

  private Date sentDate;

  private Move move;

  private Game game;

  private List<Player> playerList;

  public GameMessage() {
    super(GameMessage.class);
  }

  public Player getSender() {
    return sender;
  }

  public void setSender(Player sender) {
    this.sender = sender;
    setIndexedProperty("sender", sender);
  }

  public Player getReceiver() {
    return receiver;
  }

  public void setReceiver(Player receiver) {
    this.receiver = receiver;
    setIndexedProperty("receiver", receiver);
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
    getEntiy().setProperty("message", new Text(message));
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
    getEntiy().setProperty("data", data);
  }

  public Date getSentDate() {
    return sentDate;
  }

  public void setSentDate(Date sentDate) {
    this.sentDate = sentDate;
    getEntiy().setIndexedProperty("sentDate", sentDate);
  }

  public GameMessageDto.MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(GameMessageDto.MessageType messageType) {
    this.messageType = messageType;
    getEntiy().setIndexedProperty("messageType", messageType);
  }

  public Move getMove() {
    return move;
  }

  public void setMove(Move move) {
    this.move = move;
    setProperty("move", move);
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
    setProperty("game", game);
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
}
