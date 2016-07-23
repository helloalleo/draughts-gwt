package online.draughts.rus.draughts;

import online.draughts.rus.shared.dto.MoveDto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static online.draughts.rus.client.util.Utils.format;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.10.15
 * Time: 15:13
 */
public class Stroke {

  static final String SIMPLE_MOVE_SEP = "-";
  public static final String BEAT_MOVE_SEP = ":";

  private Square takenSquare;
  private Square startSquare;
  private Square endSquare;

  private Set<MoveDto.MoveFlag> moveFlags = new HashSet<>();
  private boolean first;
  private int order;
  private int number;
  private String comment;
  private String title;
  private boolean queen;
  private boolean white;
  private Stroke nextStroke;
  private Stroke prevStroke;
  private String index;

  public Stroke() {
  }

  Square getTakenSquare() {
    return takenSquare;
  }

  Stroke setTakenSquare(Square takenSquare) {
    this.takenSquare = takenSquare;
    return this;
  }

  Square getStartSquare() {
    return startSquare;
  }

  Stroke setStartSquare(Square startSquare) {
    this.startSquare = startSquare;
    return this;
  }

  public Square getEndSquare() {
    return endSquare;
  }

  Stroke setEndSquare(Square endSquare) {
    this.endSquare = endSquare;
    return this;
  }

  public String toNotation() {
    final String s = startSquare.toNotation();
    final String e = endSquare.toNotation();
    return format(isSimple() ? s + SIMPLE_MOVE_SEP + e
        : s + BEAT_MOVE_SEP + e);
  }

  public String toNotationLastMove() {
    return endSquare.toNotation();
  }

  public int getOrder() {
    return order;
  }

  Stroke setOrder(int order) {
    this.order = order;
    return this;
  }

  public boolean isCancel() {
    return moveFlags.contains(MoveDto.MoveFlag.CANCEL_MOVE);
  }

  public boolean isSimple() {
    return moveFlags.contains(MoveDto.MoveFlag.SIMPLE_MOVE);
  }

  public boolean isContinueBeat() {
    return moveFlags.contains(MoveDto.MoveFlag.CONTINUE_BEAT);
  }

  public boolean isStopBeat() {
    return moveFlags.contains(MoveDto.MoveFlag.STOP_BEAT);
  }

  public boolean isStartBeat() {
    return moveFlags.contains(MoveDto.MoveFlag.START_BEAT);
  }

  public void setOnCancelMove() {
    moveFlags.add(MoveDto.MoveFlag.CANCEL_MOVE);
  }

  void setOnSimpleMove() {
    moveFlags.add(MoveDto.MoveFlag.SIMPLE_MOVE);
  }

  void setOnContinueBeat() {
    moveFlags.add(MoveDto.MoveFlag.CONTINUE_BEAT);
  }

  void setOnStopBeat() {
    moveFlags.add(MoveDto.MoveFlag.STOP_BEAT);
  }

  void setOnStartBeat() {
    moveFlags.add(MoveDto.MoveFlag.START_BEAT);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Stroke stroke = (Stroke) o;
    return queen == stroke.queen &&
        white == stroke.white &&
        Objects.equals(takenSquare, stroke.takenSquare) &&
        Objects.equals(startSquare, stroke.startSquare) &&
        Objects.equals(endSquare, stroke.endSquare);
  }

  @Override
  public int hashCode() {
    return Objects.hash(takenSquare, startSquare, endSquare, queen, white);
  }

  /**
   * Отражает ход на доске не изменяя сам объект
   *
   * @return moveDto отраженный объект
   */
  public Stroke flip() {
    Stroke stroke = new Stroke();

    stroke.setStartSquare(startSquare.mirror());
    stroke.setEndSquare(endSquare.mirror());
    if (takenSquare != null) {
      stroke.setTakenSquare(takenSquare.mirror());
    }
    stroke.setMoveFlags(moveFlags);
    stroke.setFirst(first);
    stroke.setOrder(order);
    stroke.setNumber(number);
    return stroke;
  }

  Stroke setMoveFlags(Set<MoveDto.MoveFlag> moveFlags) {
    this.moveFlags = moveFlags;
    return this;
  }

  Stroke setFirst(boolean first) {
    this.first = first;
    return this;
  }

  Stroke setNumber(int number) {
    this.number = number;
    return this;
  }

  public int getNumber() {
    return number;
  }

  public boolean isFirst() {
    return first;
  }

  Set<MoveDto.MoveFlag> getMoveFlags() {
    return moveFlags;
  }

  @Override
  public String toString() {
    return "Stroke{" + "takenSquare=" + takenSquare +
        ", startSquare=" + startSquare +
        ", endSquare=" + endSquare +
        ", moveFlags=" + moveFlags +
        ", first=" + first +
        ", order=" + order +
        ", number=" + number +
        ", comment='" + comment + '\'' +
        ", title='" + title + '\'' +
        ", queen=" + queen +
        ", white=" + white +
        '}';
  }

  Stroke setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public String getComment() {
    return comment;
  }

  Stroke setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public Stroke setQueen(boolean queen) {
    this.queen = queen;
    return this;
  }

  public boolean isQueen() {
    return queen;
  }

  public Stroke setWhite(boolean white) {
    this.white = white;
    return this;
  }

  public boolean isWhite() {
    return white;
  }

  public Stroke setNextStroke(Stroke nextStroke) {
    this.nextStroke = nextStroke;
    return this;
  }

  public Stroke getNextStroke() {
    return nextStroke;
  }

  public Stroke setPrevStroke(Stroke prevStroke) {
    this.prevStroke = prevStroke;
    return this;
  }

  public Stroke getPrevStroke() {
    return prevStroke;
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }
}
