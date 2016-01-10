package online.draughts.rus.server.domain;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 22.12.15
 * Time: 19:33
 */
public class Draught extends ModelImpl<Draught> {

  private int row;
  private int col;

  private boolean white;
  private boolean queen;

  public Draught() {
    super(Draught.class);
  }

  public Draught(int row, int col, boolean white, boolean queen) {
    super(Draught.class);
    this.row = row;
    this.col = col;

    this.white = white;
    this.queen = queen;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public boolean isWhite() {
    return white;
  }

  public void setWhite(boolean white) {
    this.white = white;
  }

  public boolean isQueen() {
    return queen;
  }

  public void setQueen(boolean queen) {
    this.queen = queen;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Draught draught = (Draught) o;
    return Objects.equals(row, draught.row) &&
        Objects.equals(col, draught.col) &&
        Objects.equals(white, draught.white);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), row, col, white);
  }

  public String serializeToString() {
    return serializeToString(this);
  }

  public Draught fromString(String string) {
    return fromString(string, Draught.class);
  }

  @Override
  public void update() {
    super.update();
  }

  public static Draught getInstance() {
    return SingletonHolder.INSTANCE;
  }

  private static class SingletonHolder {
    private static final Draught INSTANCE = new Draught();
  }
}
