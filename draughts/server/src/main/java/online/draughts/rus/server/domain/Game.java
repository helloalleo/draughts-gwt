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
  private GameDto.GameType gameType;

  @Index
  private GameDto.GameEnds playEndStatus;

  @Index
  private boolean publish;

  private Date playStartDate;

  @Index
  private Date playFinishDate;

  @Text
  private String notation;

  @Text
  private String endGameScreenshot;
  private String endGameScreenshotUrl;
  @Text
  private String currentStateScreenshot;
  private String currentStateScreenshotUrl;

  // указывает является ли игра сохраненной
  private boolean gameSnapshot;

  private boolean deleted;

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

  public GameDto.GameType getGameType() {
    return gameType;
  }

  public void setGameType(GameDto.GameType gameType) {
    this.gameType = gameType;
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

  public boolean isPublish() {
    return publish;
  }

  public void setPublish(boolean publish) {
    this.publish = publish;
  }

  // ********* DB Queries ********* //
  public List<Game> findTrueRange(int offset, int limit) {
    return super.findRange(offset, limit);
  }

  public List<Game> findRange(int offset, int limit) {
    if (offset < 0 || limit < 0) {
      return null;
    }
    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(limit);
    fetchOptions.offset(offset);

    Query query = new Query(getEntityName());
    query.addSort("playFinishDate", Query.SortDirection.DESCENDING);

    Query.Filter published =
        new Query.FilterPredicate("publish",
            Query.FilterOperator.EQUAL,
            true);

    Query.Filter playFinishDateValidFilter =
        new Query.FilterPredicate("playFinishDate",
            Query.FilterOperator.NOT_EQUAL,
            null);

    Query.Filter isNotGameSnapshot =
        new Query.FilterPredicate("gameSnapshot",
            Query.FilterOperator.EQUAL,
            false);

    Query.Filter isNotDeleted =
            new Query.FilterPredicate("deleted",
                    Query.FilterOperator.EQUAL,
                    false);

    Query.Filter validPlayFinishDateAndPublished =
        Query.CompositeFilterOperator.and(playFinishDateValidFilter,
            published, isNotGameSnapshot, isNotDeleted);

    query.setFilter(validPlayFinishDateAndPublished);

    PreparedQuery preparedQuery = getDatastore().prepare(query);
    return getListResult(preparedQuery.asQueryResultList(fetchOptions));
  }

  public List<Game> findUserGames(Long playerId, int offset, int limit) {
    final Key playerKey = KeyFactory.createKey(Player.getInstance().getEntityName(), playerId);
    Query.Filter playFinishDateValidFilter =
        new Query.FilterPredicate("playFinishDate",
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

    Query.Filter isNotDeleted =
        new Query.FilterPredicate("deleted",
            Query.FilterOperator.EQUAL,
            false);

    Query.Filter blackOrWhiteFilter =
        Query.CompositeFilterOperator.or(playerBlackFilter, playerWhiteFilter);

    Query.Filter validPlayFinishDateAndPlayer =
        Query.CompositeFilterOperator.and(playFinishDateValidFilter,
            blackOrWhiteFilter, isNotDeleted);

    Query query = new Query(getEntityName()).setFilter(validPlayFinishDateAndPlayer);
    query.addSort("playFinishDate", Query.SortDirection.DESCENDING);

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

  public void setEndGameScreenshotUrl(String endGameScreenshotUrl) {
    this.endGameScreenshotUrl = endGameScreenshotUrl;
  }

  public String getEndGameScreenshotUrl() {
    return endGameScreenshotUrl;
  }

  public String getCurrentStateScreenshot() {
    return currentStateScreenshot;
  }

  public void setCurrentStateScreenshot(String currentStateScreenshot) {
    this.currentStateScreenshot = currentStateScreenshot;
  }

  public String getCurrentStateScreenshotUrl() {
    return currentStateScreenshotUrl;
  }

  public void setCurrentStateScreenshotUrl(String currentStateScreenshotUrl) {
    this.currentStateScreenshotUrl = currentStateScreenshotUrl;
  }

  public boolean isGameSnapshot() {
    return gameSnapshot;
  }

  public void setGameSnapshot(boolean gameSnapshot) {
    this.gameSnapshot = gameSnapshot;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  private static class SingletonHolder {
    private static final Game INSTANCE = new Game();
  }
}
