package online.shashki.rus.shashki;


import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.shared.core.types.Color;

/**
 * Created with IntelliJ IDEA.
 * Profile: alekspo
 * Date: 07.12.13
 * Time: 21:07
 */
public class Square extends Rectangle {

  private static final String TO_SEND_SEP = ",";
  private final int row;
  private final int col;
  private static final Color boardBackground = new Color(65, 133, 132);
  private boolean occupied;
  private Draught occupant;
  private static double strokeLineWidth = 0;
  private String[] alph = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};

  public Square(int row, int col) {
    super(0, 0);
    this.row = row;
    this.col = col;

    this.occupied = false;
    this.occupant = null;

    this.setFillColor(boardBackground);
//    this.setStrokeWidth(strokeLineWidth);
  }

//  public static Square fromString(int row, int col) {
//    Square square = new Square();
//
//    square.row = row;
//    square.col = col;
//
//    square.occupied = false;
//    square.occupant = null;
//
//    square.setFillColor(boardBackground);
//    square.setStrokeWidth(strokeLineWidth);
//
//    return square;
//  }

  public static Square fromString(String toSendStr) {
    if (toSendStr == null || toSendStr.isEmpty()) {
      return null;
    }
    final String[] toSendArr = toSendStr.split(TO_SEND_SEP);
    final Integer row = Integer.valueOf(toSendArr[0]);
    final Integer col = Integer.valueOf(toSendArr[1]);
    return new Square(row, col);
  }

  public static boolean isValid(int row, int col) {
    return row % 2 != col % 2;
  }

  /**
   * Get the piece that occupies this Square
   *
   * @return The piece that occupies this Square, if any
   */
  public Draught getOccupant() {
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
  public void setOccupant(Draught visitor) {
    if (visitor != null) {
      this.occupant = visitor;
      this.occupied = true;
    } else {
      this.occupant = null;
      this.occupied = false;
    }
  }

  /**
   * Return whether or not this Square is occupied
   *
   * @return Whether or not this Square is selected
   */
  public boolean isOccupied() {
    return occupied;
  }

  public String toString() {
    return "row=" + String.valueOf(row) + " col=" + String.valueOf(col)
        + "; x=" + String.valueOf(getX()) + " y=" + String.valueOf(getY());
  }

  public String toSend() {
    return String.valueOf(row) + TO_SEND_SEP + String.valueOf(col);
  }

  public String toNotation(boolean first) {
    if (first) {
      return alph[col] + String.valueOf(BoardBackgroundLayer.ROWS - row);
    }
    return alph[BoardBackgroundLayer.COLS - 1 - col] + String.valueOf(row + 1);
  }

  /**
   * Возвращает true если square на той же диагонале что и эта
   *
   * @param square
   * @return
   */
  public boolean isOnLine(Square square) {
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
  public int getCol() {
    return col;
  }

  public boolean isBetween(Square from, Square to) {
    if (isBetween(this.getRow(), from.getRow(), to.getRow())
        && isBetween(this.getCol(), from.getCol(), to.getCol())) {
      return true;
    }
    if (isBetween(this.getRow(), from.getRow(), to.getRow())
        && isBetween(this.getCol(), to.getCol(), from.getCol())) {
      return true;
    }
    if (isBetween(this.getRow(), to.getRow(), from.getRow())
        && isBetween(this.getCol(), to.getCol(), from.getCol())) {
      return true;
    }
    if (isBetween(this.getRow(), to.getRow(), from.getRow())
        && isBetween(this.getCol(), from.getCol(), to.getCol())) {
      return true;
    }
    return false;
  }

  private static boolean isBetween(int value, int min, int max) {
    return ((value > min) && (value < max));
  }

  public void updateShape(int side, int rows, int cols, double offsetX, int offsetY) {
    double x = ((double) col) * side / ((double) rows) + offsetX;
    double y = ((double) row) * side / ((double) cols) + offsetY;
    setX(x);
    setY(y);

    double squareSize = side / (double) rows;
    setWidth(squareSize);
    setHeight(squareSize);
//    this.setStrokeWidth(strokeLineWidth);
  }

  public boolean contains(double x, double y) {
    return (Math.abs(x - getX()) < getWidth()) && (Math.abs(y - getY()) < getHeight());
  }

  public double getCenterX() {
    return getX() + getWidth() / 2;
  }

  public double getCenterY() {
    return getY() + getHeight() / 2;
  }

  public Square mirror() {
    int row = BoardBackgroundLayer.ROWS - 1 - this.row;
    int col = BoardBackgroundLayer.COLS - 1 - this.col;

    return new Square(row, col);
  }
}