package online.draughts.rus.server.domain;


import online.draughts.rus.server.annotation.Enumerated;
import online.draughts.rus.server.annotation.Text;
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

  private GameMessage gameMessage;

  private Draught startPos;

  private Draught endPos;

  private Draught takenPos;

  @Enumerated(value = MoveDto.MoveFlags.class)
  private Set<MoveDto.MoveFlags> moveFlags = new HashSet<>();

  private String title;

  private String comment;

  private Set<String> hashTags = new HashSet<>();

  @Text
  private String screenshot;

  public Move() {
    super(Move.class);
  }

  public Move(int number, boolean first, GameMessage gameMessage, Draught startPos, Draught endPos, Draught takenPos,
              Set<MoveDto.MoveFlags> moveFlags, String screenshot) {
    super(Move.class);
    this.number = number;
    this.first = first;
    this.moveFlags = moveFlags;

    this.gameMessage = gameMessage;

    this.startPos = startPos;
    this.endPos = endPos;
    this.takenPos = takenPos;

    this.screenshot = screenshot;
  }

  public Move(Move move) {
    this(move.getNumber(), move.isFirst(), move.getGameMessage(), move.getStartPos(), move.getEndPos(),
        move.getTakenPos(), move.getMoveFlags(), move.getScreenshot());
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

  public Draught getTakenPos() {
    return takenPos;
  }

  public void setTakenPos(Draught takenPos) {
    this.takenPos = takenPos;
  }

  public Draught getStartPos() {
    return startPos;
  }

  public void setStartPos(Draught startPos) {
    this.startPos = startPos;
  }

  public Draught getEndPos() {
    return endPos;
  }

  public void setEndPos(Draught endPos) {
    this.endPos = endPos;
  }

  public Set<MoveDto.MoveFlags> getMoveFlags() {
    return moveFlags;
  }

  public void setMoveFlags(Set<MoveDto.MoveFlags> moveFlags) {
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

  @Override
  public String toString() {
    return "Move{" +
        "number=" + number +
        ", first=" + first +
        ", startPos='" + startPos + '\'' +
        ", endPos='" + endPos + '\'' +
        ", takenPos='" + takenPos + '\'' +
        ", moveFlags=" + moveFlags +
        '}';
  }

  @Override
  public void update() {
    this.getStartPos().update();
    this.getEndPos().update();
    if (null != this.getTakenPos()) {
      this.getTakenPos().update();
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
