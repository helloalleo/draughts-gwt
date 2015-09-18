package online.shashki.ru.shared.dto;

import online.shashki.ru.shared.model.Game;
import online.shashki.ru.shared.model.GameMessage;
import online.shashki.ru.shared.model.Move;
import online.shashki.ru.shared.model.Shashist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 18:56
 */
public class GameMessageDto implements GameMessage {

  private Shashist sender;
  private Shashist receiver;
  private String message;
  private MessageType type;
  private List<Shashist> playerList = new ArrayList<>();
  private String data;
  private Date sentDate;
  private String startStep;
  private String endStep;
  private String captured;
  private Game game;
  private Long id;
  private Move move;

  @Override
  public Shashist getSender() {
    return sender;
  }

  @Override
  public void setSender(Shashist sender) {
    this.sender = sender;
  }

  @Override
  public Shashist getReceiver() {
    return receiver;
  }

  @Override
  public void setReceiver(Shashist receiver) {
    this.receiver = receiver;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String getData() {
    return data;
  }

  @Override
  public void setData(String data) {
    this.data = data;
  }

  @Override
  public Date getSentDate() {
    return sentDate;
  }

  @Override
  public void setSentDate(Date sentDate) {
    this.sentDate = sentDate;
  }

  @Override
  public MessageType getMessageType() {
    return type;
  }

  @Override
  public void setMessageType(MessageType messageType) {
    this.type = messageType;
  }

  @Override
  public Game getGame() {
    return game;
  }

  @Override
  public void setGame(Game game) {
    this.game = game;
  }

  @Override
  public List<Shashist> getPlayerList() {
    return playerList;
  }

  @Override
  public void setPlayerList(List<Shashist> playerList) {
    this.playerList = playerList;
  }

  @Override
  public Move getMove() {
    return move;
  }

  @Override
  public void setMove(Move move) {
    this.move = move;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public Integer getVersion() {
    return null;
  }

}
