package online.draughts.rus.shared.dto;


import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.14
 * Time: 15:49
 */
public class GameMessageDto extends BaseDto {

  private PlayerDto sender;

  private PlayerDto receiver;

  private String message;

  private MessageType messageType;

  private String data;

  private Date sentDate;

  private MoveDto move;

  private GameDto game;

  private List<PlayerDto> playerList;

  public PlayerDto getSender() {
    return sender;
  }

  public void setSender(PlayerDto sender) {
    this.sender = sender;
  }

  public PlayerDto getReceiver() {
    return receiver;
  }

  public void setReceiver(PlayerDto receiver) {
    this.receiver = receiver;
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

  public MoveDto getMove() {
    return move;
  }

  public void setMove(MoveDto move) {
    this.move = move;
  }

  public GameDto getGame() {
    return game;
  }

  public void setGame(GameDto game) {
    this.game = game;
  }

  public void setPlayerList(List<PlayerDto> playerList) {
    this.playerList = playerList;
  }

  public List<PlayerDto> getPlayerList() {
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
        '}';
  }

  public static final String GAME_END = "game_end";
  public static final String GAME_UPDATE = "game_update";

  public enum MessageType implements IsSerializable {

    CHAT_MESSAGE,
    CHAT_PRIVATE_MESSAGE,
    USER_LIST_UPDATE,
    GAME_LIST_UPDATE,
    PLAY_INVITE,
    PLAY_REJECTED_INVITE,
    PLAY_ALREADY_PLAYING,
    PLAY_START,
    PLAY_OPPONENT_MOVE,
    PLAY_SAVE_MOVE,
    PLAY_CANCEL_MOVE,
    PLAY_CANCEL_MOVE_RESPONSE,
    PLAY_PROPOSE_DRAW,
    PLAY_ACCEPT_DRAW,
    // вызывается на второй стороне чтобы обновить список игр
    PLAY_GAME_UPDATE,
    PLAY_SURRENDER,
    PLAY_TIMEOUT,
    // не получили ответа на приглашение
    PLAY_DID_NOT_RESPONSE,
    NOTIFICATION_ADDED_TO_FAVORITE,
    CHANNEL_CLOSE,
    PLAYER_REGISTER
  }
}
