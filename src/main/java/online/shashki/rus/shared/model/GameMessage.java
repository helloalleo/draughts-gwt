package online.shashki.rus.shared.model;

import com.google.gwt.user.client.rpc.GwtTransient;
import com.google.gwt.user.client.rpc.IsSerializable;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.14
 * Time: 15:49
 */
@Entity
@Table(name = "game_message")
public class GameMessage extends PersistableObjectImpl {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id")
  private Shashist sender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id")
  private Shashist receiver;

  private String message;

  @Enumerated(EnumType.STRING)
  @Column(name = "message_type")
  private MessageType messageType;

  @GwtTransient
  @com.fasterxml.jackson.annotation.JsonIgnore
  @JsonIgnore
  private String data;

  @GwtTransient
  @com.fasterxml.jackson.annotation.JsonIgnore
  @JsonIgnore
  @Column(name = "sent_date")
  private Date sentDate;

  @OneToOne(mappedBy = "gameMessage", cascade = CascadeType.ALL)
  private Move move;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "game_id")
  private Game game;

  @Transient
  private List<Shashist> playerList;

  public Shashist getSender() {
    return sender;
  }

  public void setSender(Shashist entity) {
    this.sender = entity;
  }

  public Shashist getReceiver() {
    return receiver;
  }

  public void setReceiver(Shashist entity) {
    this.receiver = entity;
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

  public Move getMove() {
    return move;
  }

  public void setMove(Move move) {
    this.move = move;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game entity) {
    this.game = entity;
  }

  public void setPlayerList(List<Shashist> playerList) {
    this.playerList = playerList;
  }

  public List<Shashist> getPlayerList() {
    return playerList;
  }

  public enum MessageType implements IsSerializable {

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
