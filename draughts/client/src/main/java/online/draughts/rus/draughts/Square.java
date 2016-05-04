package online.draughts.rus.draughts;

import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.shared.core.types.Color;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gwt.core.client.GWT;
import online.draughts.rus.shared.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Profile: alekspo
 * Date: 07.12.13
 * Time: 21:07
 */
@SuppressWarnings("GwtInconsistentSerializableClass")
public class Square implements Serializable {

  @JsonIgnore
  private static final String POSITION_DELIMITER = ",";

  @JsonIgnore
  private Rectangle shape;
  @JsonIgnore
  private static BoardBackgroundLayer board;
  private int row;
  private int col;
  @JsonIgnore
  private static final Color boardBackground = new Color(65, 133, 132);
  @JsonIgnore
  private Draught occupant;
  @JsonIgnore
  private static List<String> alph = new ArrayList<String>() {{
    add("a");
    add("b");
    add("c");
    add("d");
    add("e");
    add("f");
    add("g");
    add("h");
  }};

  private Square(int row, int col) {
    if (GWT.isClient()) {
      this.shape = new Rectangle(0, 0);
      this.shape.setFillColor(boardBackground);
    }
    this.row = row;
    this.col = col;

    this.occupant = null;
  }

  Square(BoardBackgroundLayer backgroundLayer, int row, int col) {
    this(row, col);
    Square.board = backgroundLayer;
  }

//  public static Square fromString(String toSendStr) {
//    if (StringUtils.isEmpty(toSendStr)) {
//      return null;
//    }
//    final String[] toSendArr = toSendStr.split(POSITION_DELIMITER);
//    final Integer row = Integer.valueOf(toSendArr[0]);
//    final Integer col = Integer.valueOf(toSendArr[1]);
//    try {
//      return board.getSquare(row, col);
//    } catch (SquareNotFoundException e) {
//      return null;
//    }
//  }

  static Square fromPosition(int row, int col) {
    try {
      return board.getSquare(row, col);
    } catch (SquareNotFoundException e) {
      return null;
    }
  }

  Rectangle getShape() {
    return shape;
  }

  static boolean isValid(int row, int col) {
    return row % 2 != col % 2;
  }

  /**
   * Get the piece that occupies this Square
   *
   * @return The piece that occupies this Square, if any
   */
  Draught getOccupant() {
    if (this.isOccupied()) {
      return occupant;
    }

    return null;
  }

  /**
   * Set the occupant of this Square
   *
   * @param visitor The Piece that should now reside here
   */
  void setOccupant(Draught visitor) {
    if (visitor != null) {
      this.occupant = visitor;
    } else {
      this.occupant = null;
    }
  }

  /**
   * Return whether or not this Square is occupied
   *
   * @return Whether or not this Square is selected
   */
  boolean isOccupied() {
    return occupant != null;
  }

  @Override
  public String toString() {
    return "Square{" +
        "row=" + row +
        ", col=" + col +
        ", occupant=" + occupant +
        '}';
  }

  /**
   * переводит клетку в нотацию
   *
   * @return нотация для клетки
   */
  String toNotation() {
    return alph.get(col) + String.valueOf(BoardBackgroundLayer.ROWS - row);
  }

  /**
   * Возвращает true если square на той же диагонале что и эта
   *
   * @param square шашка на линии с которой эта шашка
   * @return шашка на линии с этой и переданной шашкой?
   */
  boolean isOnLine(Square square) {
    // равенство абсолютной велечины строки и колонки говорит что мы на одной диагонали
    if (Math.abs(this.getRow() - square.getRow()) == Math.abs(this.getCol() - square.getCol())) {
      // на главной диагонали
      if (this.getRow() - square.getRow() < 0 && this.getCol() - square.getCol() > 0
          || this.getRow() - square.getRow() > 0 && this.getCol() - square.getCol() < 0) {
        return true;
      }
      // на побочной (математической главной)
      if (this.getRow() - square.getRow() < 0 && this.getCol() - square.getCol() < 0
          || this.getRow() - square.getRow() > 0 && this.getCol() - square.getCol() > 0) {
        return true;
      }
    }
    return false;
  }

  /**
   * Get the row of the game board that this square represents
   *
   * @return The row on the game board represented by this Square
   */
  public int getRow() {
    return row;
  }

  /**
   * Get the column of the game board that this square represents
   *
   * @return The column on the game board represented by this Square
   */
  int getCol() {
    return col;
  }

  boolean isBetween(Square from, Square to) {
    return isBetween(this.getRow(), from.getRow(), to.getRow())
        && isBetween(this.getCol(), from.getCol(), to.getCol()) || isBetween(this.getRow(), from.getRow(), to.getRow())
        && isBetween(this.getCol(), to.getCol(), from.getCol()) || isBetween(this.getRow(), to.getRow(), from.getRow())
        && isBetween(this.getCol(), to.getCol(), from.getCol()) || isBetween(this.getRow(), to.getRow(), from.getRow())
        && isBetween(this.getCol(), from.getCol(), to.getCol());
  }

  private static boolean isBetween(int value, int min, int max) {
    return ((value > min) && (value < max));
  }

  void updateShape(int side, int rows, int cols, double offsetX, int offsetY) {
    double x = ((double) col) * side / ((double) rows) + offsetX;
    double y = ((double) row) * side / ((double) cols) + offsetY;
    shape.setX(x);
    shape.setY(y);

    double squareSize = side / (double) rows;
    shape.setWidth(squareSize);
    shape.setHeight(squareSize);
  }

  boolean contains(double x, double y) {
    return (x > shape.getX()) && (x < (shape.getX() + shape.getWidth()))
        && (y > shape.getY()) && (y < (shape.getY() + shape.getHeight()));
  }

  double getCenterX() {
    return shape.getX() + shape.getWidth() / 2;
  }

  double getCenterY() {
    return shape.getY() + shape.getHeight() / 2;
  }

  void setAlpha(double alpha) {
    shape.setAlpha(alpha);
  }

  double getAlpha() {
    return shape.getAlpha();
  }

  Square mirror() {
    int col = BoardBackgroundLayer.COLS - 1 - this.col;
    int row = BoardBackgroundLayer.ROWS - 1 - this.row;
    return new Square(row, col);
  }

  static Square fromNotation(String pos) {
    if (StringUtils.isEmpty(pos)) {
      return null;
    }

    int row = alph.indexOf(String.valueOf(pos.charAt(0)));
    int col = 8 - Integer.valueOf(String.valueOf(pos.charAt(1)));

    try {
      return board.getSquare(col, row);
    } catch (SquareNotFoundException e) {
      return null;
    }
  }
}
