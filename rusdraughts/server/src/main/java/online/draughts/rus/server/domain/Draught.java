package online.draughts.rus.server.domain;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 22.12.15
 * Time: 19:33
 */
public class Draught extends ModelImpl<Draught> {

  private Position pos;
  private boolean white;
  private boolean queen;

  public Draught() {
    super(Draught.class);
  }

  public Draught(Position pos, boolean white, boolean queen) {
    super(Draught.class);
    this.pos = pos;
    this.white = white;
    this.queen = queen;
  }

  public Position getPos() {
    return pos;
  }

  public void setPos(Position pos) {
    this.pos = pos;
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
    Draught draught = (Draught) o;
    return Objects.equals(white, draught.white) &&
        Objects.equals(queen, draught.queen) &&
        Objects.equals(pos, draught.pos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), pos, white, queen);
  }

  public String serializeToString() {
    return serializeToString(this);
  }

  public Draught fromString(String string) {
    return fromString(string, Draught.class);
  }

  @Override
  public void update() {
    this.getPos().update();
    super.update();
  }

  public static Draught getInstance() {
    return SingletonHolder.INSTANCE;
  }

  private static class SingletonHolder {
    private static final Draught INSTANCE = new Draught();
  }
}
