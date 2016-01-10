package online.draughts.rus.server.domain;

import com.google.appengine.api.datastore.*;
import online.draughts.rus.server.annotation.Index;
import online.draughts.rus.server.annotation.Text;
import online.draughts.rus.shared.dto.GameDto;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 16:18
 */
public class Game extends ModelImpl<Game> {

  @Index
  private Player playerWhite;
  @Index
  private Player playerBlack;

  @Index
  private GameDto.GameEnds playEndStatus;

  private Date playStartDate;

  @Index
  private Date playFinishDate;

  @Text
  private String notation;

  @Text
  private String endGameScreenshot;

  private Set<GameMessage> gameMessages = new HashSet<>();

  private Set<Draught> initialPos = new HashSet<>();

  public Game() {
    super(Game.class);
  }

  public Game(Player playerWhiteId,
              Player playerBlackId,
              GameDto.GameEnds playEndStatus,
              Date playStartDate,
              Date playFinishDate,
              String notation,
              String endGameScreenshot) {
    super(Game.class);
    this.playerWhite = playerWhiteId;
    this.playerBlack = playerBlackId;
    this.playEndStatus = playEndStatus;
    this.playStartDate = playStartDate;
    this.playFinishDate = playFinishDate;
    this.notation = notation;
    this.endGameScreenshot = endGameScreenshot;
  }

  public static Game getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public Set<Draught> getInitialPos() {
    return initialPos;
  }

  public void setInitialPos(Set<Draught> initialPos) {
    this.initialPos = initialPos;
  }

  public Player getPlayerWhite() {
    return playerWhite;
  }

  public void setPlayerWhite(Player playerWhite) {
    this.playerWhite = playerWhite;
  }

  public Player getPlayerBlack() {
    return playerBlack;
  }

  public void setPlayerBlack(Player playerBlack) {
    this.playerBlack = playerBlack;
  }

  public GameDto.GameEnds getPlayEndStatus() {
    return playEndStatus;
  }

  public void setPlayEndStatus(GameDto.GameEnds playEndStatus) {
    this.playEndStatus = playEndStatus;
  }

  public Date getPlayStartDate() {
    return playStartDate;
  }

  public void setPlayStartDate(Date playStartDate) {
    this.playStartDate = playStartDate;
  }

  public Date getPlayFinishDate() {
    return playFinishDate;
  }

  public void setPlayFinishDate(Date playEndDate) {
    this.playFinishDate = playEndDate;
  }

  public String getNotation() {
    return notation;
  }

  public void setNotation(String notation) {
    this.notation = notation;
  }

  public String getEndGameScreenshot() {
    return endGameScreenshot;
  }

  public void setEndGameScreenshot(String endGameScreenshot) {
    this.endGameScreenshot = endGameScreenshot;
  }

  public Set<GameMessage> getGameMessages() {
    return gameMessages;
  }

  // ********* DB Queries ********* //
  public List<Game> findRange(int offset, int limit) {
    if (offset < 0 || limit < 0) {
      return null;
    }
    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(limit);
    fetchOptions.offset(offset);

    Query query = new Query(getEntityName());
    query.addSort("playFinishDate", Query.SortDirection.DESCENDING);

    PreparedQuery preparedQuery = getDatastore().prepare(query);
    return getListResult(preparedQuery.asQueryResultList(fetchOptions));
  }

  public List<Game> findUserGames(Long playerId, int offset, int limit) {
    final Key playerKey = KeyFactory.createKey(Player.getInstance().getEntityName(), playerId);
    Query.Filter playStartDateValidFilter =
        new Query.FilterPredicate("playStartDate",
            Query.FilterOperator.NOT_EQUAL,
            null);
    Query.Filter playerBlackFilter =
        new Query.FilterPredicate("playerBlack",
            Query.FilterOperator.EQUAL,
            playerKey);
    Query.Filter playerWhiteFilter =
        new Query.FilterPredicate("playerWhite",
            Query.FilterOperator.EQUAL,
            playerKey);

    Query.Filter blackOrWhiteFilter =
        Query.CompositeFilterOperator.or(playerBlackFilter, playerWhiteFilter);

    Query.Filter validPlayStartDateAndPlayer =
        Query.CompositeFilterOperator.and(playStartDateValidFilter, blackOrWhiteFilter);

    Query query = new Query(getEntityName()).setFilter(validPlayStartDateAndPlayer);
    query.addSort("playStartDate", Query.SortDirection.DESCENDING);

    PreparedQuery preparedQuery = getDatastore().prepare(query);

    FetchOptions fetchOptions =
        FetchOptions.Builder.withLimit(limit);
    fetchOptions.offset(offset);
    return getListResult(preparedQuery.asQueryResultList(fetchOptions));
  }

  @Override
  public String serializeToString() {
    return serializeToString(this);
  }

  @Override
  public Game fromString(String s) {
    return fromString(s, Game.class);
  }

  private static class SingletonHolder {
    private static final Game INSTANCE = new Game();
  }
}
