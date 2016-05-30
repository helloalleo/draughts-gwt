package online.draughts.rus.server.domain;


import com.google.appengine.api.datastore.*;
import online.draughts.rus.server.annotation.Index;
import online.draughts.rus.server.annotation.Text;
import online.draughts.rus.server.annotation.Transient;
import online.draughts.rus.shared.dto.GameMessageDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.14
 * Time: 15:49
 */
//@Entity
public class GameMessage extends ModelImpl<GameMessage> {

  @Index
  private Player sender;
  @Index
  private Player receiver;
  @Text
  private String message;
  @Index
  private GameMessageDto.MessageType messageType;
  private String data;
  @Index
  private Date sentDate;
  private Move move;
  private Game game;
  @Transient
  private List<Player> playerList;

  public GameMessage() {
    super(GameMessage.class);
  }

  public static GameMessage getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public Player getSender() {
    return sender;
  }

  public void setSender(Player sender) {
    this.sender = sender;
  }

  public Player getReceiver() {
    return receiver;
  }

  public void setReceiver(Player receiver) {
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

  public GameMessageDto.MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(GameMessageDto.MessageType messageType) {
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

  public void setGame(Game game) {
    this.game = game;
  }

  public List<Player> getPlayerList() {
    return playerList;
  }

  public void setPlayerList(List<Player> playerList) {
    this.playerList = playerList;
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

  // ********* DB Queries ********* //
  public List<GameMessage> findLastMessages(Integer countLast, Long playerId, Long friendId) {
    Query.Filter chatPrivateMessageFilter =
        new Query.FilterPredicate("messageType",
            Query.FilterOperator.EQUAL,
            GameMessageDto.MessageType.CHAT_PRIVATE_MESSAGE.name());

    final Key playerKey = KeyFactory.createKey(Player.getInstance().getEntityName(), playerId);
    final Key friendKey = KeyFactory.createKey(Player.getInstance().getEntityName(), friendId);

    Query.Filter playerSenderMessageFilter =
        new Query.FilterPredicate("sender",
            Query.FilterOperator.EQUAL,
            playerKey);
    Query.Filter friendReceiverMessageFilter =
        new Query.FilterPredicate("receiver",
            Query.FilterOperator.EQUAL,
            friendKey);

    Query.Filter playerReceiverMessageFilter =
        new Query.FilterPredicate("receiver",
            Query.FilterOperator.EQUAL,
            playerKey);
    Query.Filter friendSenderMessageFilter =
        new Query.FilterPredicate("sender",
            Query.FilterOperator.EQUAL,
            friendKey);

    Query.Filter playerFriendMessageFilter = Query.CompositeFilterOperator.and(
        Query.CompositeFilterOperator.and(chatPrivateMessageFilter, playerSenderMessageFilter),
        Query.CompositeFilterOperator.and(chatPrivateMessageFilter, friendReceiverMessageFilter));
    Query.Filter friendPlayerMessageFilter = Query.CompositeFilterOperator.and(
        Query.CompositeFilterOperator.and(chatPrivateMessageFilter, playerReceiverMessageFilter),
        Query.CompositeFilterOperator.and(chatPrivateMessageFilter, friendSenderMessageFilter));

    Query.Filter playerOrFriendMessageFilter = Query.CompositeFilterOperator.or(
        playerFriendMessageFilter,
        friendPlayerMessageFilter);

    Query query = new Query(getEntityName()).setFilter(playerOrFriendMessageFilter);
    query.addSort("sentDate", Query.SortDirection.ASCENDING);
    PreparedQuery preparedQuery = getDatastore().prepare(query);
    FetchOptions fetchOptions =
        FetchOptions.Builder.withLimit(countLast);
    return getListResult(preparedQuery.asQueryResultList(fetchOptions));
  }

  /**
   * Функция ищет все ходя для заданной игры
   * @param gameId id игры ходы которой ищем
   * @return список ходов
   */
  public List<Move> findGameMoves(Long gameId) {
    Key gameKey = KeyFactory.createKey(Game.getInstance().getEntityName(), gameId);
    Query.Filter gameIdFilter =
        new Query.FilterPredicate("game",
            Query.FilterOperator.EQUAL,
            gameKey);

    Query queryGameMessages = new Query(getEntityName()).setFilter(gameIdFilter);
    queryGameMessages.addSort("sentDate", Query.SortDirection.ASCENDING);

    PreparedQuery preparedQuery = getDatastore().prepare(queryGameMessages);

    List<GameMessage> gameMessages = getListResult(preparedQuery);
    List<Move> moves = new ArrayList<>(gameMessages.size());
    for (GameMessage gameMessage : gameMessages) {
      if (null == gameMessage.getMove()) {
        continue;
      }
      moves.add(gameMessage.getMove());
    }
    return moves;
  }

  private static class SingletonHolder {
    private static final GameMessage INSTANCE = new GameMessage();
  }
}
