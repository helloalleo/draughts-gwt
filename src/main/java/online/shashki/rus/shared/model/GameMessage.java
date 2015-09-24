package online.shashki.rus.shared.model;

import com.google.gwt.user.client.rpc.GwtTransient;
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
public class GameMessage extends PersistableObjectImpl implements Message {

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
  @JsonIgnore
  private String data;

  @GwtTransient
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

  @Override
  public Shashist getSender() {
    return sender;
  }

  @Override
  public void setSender(Shashist entity) {
    this.sender = entity;
  }

  @Override
  public Shashist getReceiver() {
    return receiver;
  }

  @Override
  public void setReceiver(Shashist entity) {
    this.receiver = entity;
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
    return messageType;
  }

  @Override
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
}
