package online.draughts.rus.server.domain;

import online.draughts.rus.server.annotation.Converter;
import online.draughts.rus.server.annotation.Text;
import online.draughts.rus.server.domain.converter.MoveFlagsConverter;
import online.draughts.rus.shared.dto.MoveDto;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.09.15
 * Time: 21:45
 */
public class Move extends ModelImpl<Move> {

  private int number; // номер пары хода
  private int moveOrder; // порядковый номер хода
  private boolean first; // первый ход в паре ходов. Например, ee-aa в ee-aa bb-cc
  private boolean fixate; // индикатор того, что ходили белыми
  private boolean queen;
  private boolean white;

  private GameMessage gameMessage;

  private Draught movingDraught;

  private Draught movedDraught;

  private Draught takenDraught;

  @Converter(value = MoveFlagsConverter.class)
  private Set<MoveDto.MoveFlag> moveFlags = new HashSet<>();

  private String title;

  private String comment;

  private Set<String> hashTags = new HashSet<>();

  @Text
  private String screenshot;

  public Move() {
    super(Move.class);
  }

  public Move(int number, boolean first, GameMessage gameMessage, Draught movingDraught, Draught movedDraught,
              Draught takenDraught, Set<MoveDto.MoveFlag> moveFlags, String screenshot) {
    super(Move.class);
    this.number = number;
    this.first = first;
    this.moveFlags = moveFlags;

    this.gameMessage = gameMessage;

    this.movingDraught = movingDraught;
    this.movedDraught = movedDraught;
    this.takenDraught = takenDraught;

    this.screenshot = screenshot;
  }

  public Move(Move move) {
    this(move.getNumber(), move.isFirst(), move.getGameMessage(), move.getMovingDraught(), move.getMovedDraught(),
        move.getTakenDraught(), move.getMoveFlags(), move.getScreenshot());
  }

  public static Move getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public GameMessage getGameMessage() {
    return gameMessage;
  }

  public void setGameMessage(GameMessage gameMessage) {
    this.gameMessage = gameMessage;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public boolean isFirst() {
    return first;
  }

  public void setFirst(boolean first) {
    this.first = first;
  }

  public boolean isFixate() {
    return fixate;
  }

  public void setFixate(boolean fixate) {
    this.fixate = fixate;
  }

  public Draught getTakenDraught() {
    return takenDraught;
  }

  public void setTakenDraught(Draught takenDraught) {
    this.takenDraught = takenDraught;
  }

  public Draught getMovingDraught() {
    return movingDraught;
  }

  public void setMovingDraught(Draught movingDraught) {
    this.movingDraught = movingDraught;
  }

  public Draught getMovedDraught() {
    return movedDraught;
  }

  public void setMovedDraught(Draught movedDraught) {
    this.movedDraught = movedDraught;
  }

  public Set<MoveDto.MoveFlag> getMoveFlags() {
    return moveFlags;
  }

  public void setMoveFlags(Set<MoveDto.MoveFlag> moveFlags) {
    this.moveFlags = moveFlags;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Set<String> getHashTags() {
    return hashTags;
  }

  public void setHashTags(Set<String> hashTags) {
    this.hashTags = hashTags;
  }

  public int getMoveOrder() {
    return moveOrder;
  }

  public void setMoveOrder(int order) {
    this.moveOrder = order;
  }

  public String getScreenshot() {
    return screenshot;
  }

  public void setScreenshot(String screenshot) {
    this.screenshot = screenshot;
  }

  public boolean isQueen() {
    return queen;
  }

  public void setQueen(boolean queen) {
    this.queen = queen;
  }

  public boolean isWhite() {
    return white;
  }

  public void setWhite(boolean white) {
    this.white = white;
  }

  @Override
  public String toString() {
    return "Move{" +
        "number=" + number +
        ", first=" + first +
        ", movingDraught='" + movingDraught + '\'' +
        ", movedDraught='" + movedDraught + '\'' +
        ", takenDraught='" + takenDraught + '\'' +
        ", moveFlags=" + moveFlags +
        '}';
  }

  @Override
  public void update() {
    this.getMovingDraught().update();
    this.getMovedDraught().update();
    if (null != this.getTakenDraught()) {
      this.getTakenDraught().update();
    }
    super.update();
  }

  @Override
  public String serializeToString() {
    return serializeToString(this);
  }

  @Override
  public Move fromString(String json) {
    return fromString(json, Move.class);
  }

  private static class SingletonHolder {
    private static final Move INSTANCE = new Move();
  }
}
