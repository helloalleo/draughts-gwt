//package online.shashki.rus.shared.dto;
//
//import online.shashki.rus.shared.model.Move;
//import online.shashki.rus.shashki.Square;
//
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Created with IntelliJ IDEA.
// * User: alekspo
// * Date: 02.09.15
// * Time: 17:25
// */
//public class MoveDto implements Move {
//  private static final String SIMPLE_MOVE_SEP = "-";
//  private static final String BEAT_MOVE_SEP = ":";
//
//  private int number;
//  private boolean first; // первый ход в паре ходов. Например, ee-aa в ee-aa bb-cc
//
//  private Square startSquare;
//  private Square endSquare;
//  private Square takenSquare;
//  private Set<Move.MoveFlags> moveFlags = new HashSet<>();
//
//  public MoveDto() {
//    startSquare = null;
//    endSquare = null;
//    takenSquare = null;
//  }
//
//  public MoveDto(Move move) {
//    this.number = move.getNumber();
//    this.first = move.isFirst();
//    this.startSquare = Square.fromString(move.getStartPos());
//    this.endSquare = Square.fromString(move.getEndPos());
//    this.takenSquare = Square.fromString(move.getTakenPos());
//    this.moveFlags = move.getMoveFlags();
//  }
//
//  public int getNumber() {
//    return number;
//  }
//
//  public Move setNumber(int number) {
//    this.number = number;
//    return this;
//  }
//
//  public boolean isFirst() {
//    return first;
//  }
//
//  public Move setFirst(boolean first) {
//    this.first = first;
//    return this;
//  }
//
//  public String getTakenPos() {
//    if (takenSquare == null) {
//      return "";
//    }
//    return takenSquare.toSend();
//  }
//
//  public Move setTakenSquare(Square takenSquare) {
//    this.takenSquare = takenSquare;
//    return this;
//  }
//
//  public Square getTakenSquare() {
//    return takenSquare;
//  }
//
//  public String getStartPos() {
//    if (startSquare == null) {
//      return "";
//    }
//    return startSquare.toSend();
//  }
//
//  public Square getStartSquare() {
//    return startSquare;
//  }
//
//  public Move setStartSquare(Square startSquare) {
//    this.startSquare = startSquare;
//    return this;
//  }
//
//  public String getEndPos() {
//    if (endSquare == null) {
//      return "";
//    }
//    return endSquare.toSend();
//  }
//
//  public Square getEndSquare() {
//    return endSquare;
//  }
//
//  public Move setEndSquare(Square endSquare) {
//    this.endSquare = endSquare;
//    return this;
//  }
//
//  public boolean isCancel() {
//    return moveFlags.contains(MoveFlags.CANCEL_MOVE);
//  }
//
//  public void setOnCancelMove() {
//    moveFlags.add(MoveFlags.CANCEL_MOVE);
//  }
//
//  public void setOffCancelMove() {
//    moveFlags.remove(MoveFlags.CANCEL_MOVE);
//  }
//
//  public boolean isSimple() {
//    return moveFlags.contains(MoveFlags.SIMPLE_MOVE);
//  }
//
//  public void setOnSimpleMove() {
//    moveFlags.add(MoveFlags.SIMPLE_MOVE);
//  }
//
//  public void setOffSimpleMove() {
//    moveFlags.remove(MoveFlags.SIMPLE_MOVE);
//  }
//
//  public boolean isContinueBeat() {
//    return moveFlags.contains(MoveFlags.CONTINUE_BEAT);
//  }
//
//  public void setOnContinueBeat() {
//    moveFlags.add(MoveFlags.CONTINUE_BEAT);
//  }
//
//  public void turnOffContinueBeat() {
//    moveFlags.remove(MoveFlags.CONTINUE_BEAT);
//  }
//
//  public boolean isStopBeat() {
//    return moveFlags.contains(MoveFlags.STOP_BEAT);
//  }
//
//  public void setOnStopBeat() {
//    moveFlags.add(MoveFlags.STOP_BEAT);
//  }
//
//  public void setOffStopBeat() {
//    moveFlags.remove(MoveFlags.STOP_BEAT);
//  }
//
//  public boolean isStartBeat() {
//    return moveFlags.contains(MoveFlags.START_BEAT);
//  }
//
//  public void setOnStartBeat() {
//    moveFlags.add(MoveFlags.START_BEAT);
//  }
//
//  public void setOffStartBeat() {
//    moveFlags.remove(MoveFlags.START_BEAT);
//  }
//
//  public Set<MoveFlags> getMoveFlags() {
//    return moveFlags;
//  }
//
//  public String toNotation() {
//    return isSimple() ? startSquare.toNotation(first) + SIMPLE_MOVE_SEP + endSquare.toNotation(first)
//        : startSquare.toNotation(first) + BEAT_MOVE_SEP + endSquare.toNotation(first);
//  }
//
//  public String toNotationLastMove() {
//    return endSquare.toNotation(first);
//  }
//
//  @Override
//  public String toString() {
//    return "Move{" +
//        "number=" + number +
//        ", first=" + first +
//        ", startSquare=" + startSquare +
//        ", endSquare=" + endSquare +
//        ", takenSquare=" + takenSquare +
//        ", moveFlags=" + moveFlags +
//        '}';
//  }
//
//  /**
//   * Отражает ход на доске не изменяя сам объект
//   *
//   * @return moveDto отраженный объект
//   */
//  public Move mirror() {
//    Move moveDto = new Move(this);
//
//    if (moveDto.getStartSquare() != null) {
//      moveDto.setStartSquare(startSquare.mirror());
//    }
//    if (moveDto.getEndSquare() != null) {
//      moveDto.setEndSquare(endSquare.mirror());
//    }
//    if (moveDto.getTakenSquare() != null) {
//      moveDto.setTakenSquare(takenSquare.mirror());
//    }
//    return moveDto;
//  }
//}
