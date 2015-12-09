package online.draughts.rus.server.domain;


import online.draughts.rus.shared.dto.MoveDto;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.09.15
 * Time: 21:45
 */
public class Move extends ModelImpl {

  private int number; // номер пары хода
  private int moveOrder; // порядковый номер хода
  private boolean first; // первый ход в паре ходов. Например, ee-aa в ee-aa bb-cc

  private GameMessage gameMessage;

  private String startPos;

  private String endPos;

  private String takenPos;

  private Set<MoveDto.MoveFlags> moveFlags = new HashSet<>();

  private String title;

  private String comment;

  private Set<String> hashTags = new HashSet<>();

  private String screenshot;

  public Move() {
    super(Move.class);
  }

  public Move(int number, boolean first, GameMessage gameMessage, String startPos, String endPos, String takenPos,
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

  public GameMessage getGameMessage() {
    return gameMessage;
  }

  public void setGameMessage(GameMessage gameMessage) {
    this.gameMessage = gameMessage;
    setProperty("gameMessage", gameMessage);
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
    getEntiy().setProperty("number", number);
  }

  public boolean isFirst() {
    return first;
  }

  public void setFirst(boolean first) {
    this.first = first;
    getEntiy().setProperty("first", first);
  }

  public String getTakenPos() {
    return takenPos;
  }

  public void setTakenPos(String takenPos) {
    this.takenPos = takenPos;
    getEntiy().setProperty("takenPos", takenPos);
  }

  public String getStartPos() {
    return startPos;
  }

  public void setStartPos(String startPos) {
    this.startPos = startPos;
    getEntiy().setProperty("startPos", startPos);
  }

  public String getEndPos() {
    return endPos;
  }

  public void setEndPos(String endPos) {
    this.endPos = endPos;
    getEntiy().setProperty("endPos", endPos);
  }

  public Set<MoveDto.MoveFlags> getMoveFlags() {
    return moveFlags;
  }

  public void setMoveFlags(Set<MoveDto.MoveFlags> moveFlags) {
    this.moveFlags = moveFlags;
    getEntiy().setProperty("moveFlags", moveFlags);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
    getEntiy().setProperty("title", title);
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
    getEntiy().setProperty("comment", comment);
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
    getEntiy().setProperty("moveOrder", order);
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
}
