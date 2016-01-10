package online.draughts.rus.draughts;

import com.ait.lienzo.client.core.animation.AnimationProperties;
import com.ait.lienzo.client.core.animation.AnimationProperty;
import com.ait.lienzo.client.core.animation.AnimationTweener;
import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Star;
import com.ait.lienzo.client.core.types.Shadow;
import com.ait.lienzo.shared.core.types.ColorName;

/**
 * Created with IntelliJ IDEA.
 * Profile: alekspo
 * Date: 07.12.13
 * Time: 21:08
 */
public class Draught extends Group {
  private int deskSide;
  private static Draught selectedDraught;

  private static Board board;

  private int row;
  private int col;
  private boolean white;

  private Circle mainCircle = new Circle(0);
  private Circle innerCircle1 = new Circle(0);
  private Circle innerCircle2 = new Circle(0);
  private Star queenStar = new Star(5, 0, 0);

  private int rows;
  private int cols;

  private boolean queen;

  private double offsetX;

  public Draught(Board board,
                 Integer deskSide,
                 Integer rows,
                 Integer cols,
                 Integer row,
                 Integer col,
                 Boolean white,
                 Double offsetX) {
    this.deskSide = deskSide;
    this.row = row;
    this.col = col;
    this.rows = rows;
    this.cols = cols;
    this.white = white;
    this.offsetX = offsetX;
    Draught.board = board;

    setListening(true);

    add(mainCircle);
    add(innerCircle1);
    add(innerCircle2);

    queenStar.setFillColor(white ? ColorName.BLUE : ColorName.RED);

    Circle[] circles = {mainCircle, innerCircle1, innerCircle2};

    for (Circle circle : circles) {
      circle.setFillColor(ColorName.TRANSPARENT);
      circle.setStrokeColor(white ? ColorName.DARKGRAY : ColorName.DARKSLATEGRAY);
      circle.setStrokeWidth(1.4);
    }
    mainCircle.setFillColor(white ? ColorName.WHITESMOKE : ColorName.SLATEGRAY);
    mainCircle.setShadow(new Shadow(ColorName.BLACK, 12, 2, 2));

    updateShape();
  }

  public void onNodeTouch(boolean selfPlay) {
    if (!(isValidStroke() || selfPlay)) {
      return;
    }

    if (selectedDraught != null) {
      AnimationProperties props = new AnimationProperties();
      props.push(AnimationProperty.Properties.SCALE(1));

      selectedDraught.animate(AnimationTweener.LINEAR, props, 100);
    }

    AnimationProperties props = new AnimationProperties();
    props.push(AnimationProperty.Properties.SCALE(1.3));

    animate(AnimationTweener.LINEAR, props, 100);

    selectedDraught = this;

    board.resetDesk();
    board.highlightAllowedMoves(selectedDraught);
  }

  public boolean isWhite() {
    return white;
  }

  public void setWhite(boolean white) {
    this.white = white;
  }

  public void updateShape() {
    double x = col * deskSide / rows;
    double y = row * deskSide / cols;
    double squareSize = deskSide / rows;
    setX(x + squareSize / 2 + offsetX);
    setY(y + squareSize / 2 + offsetX - 2);

    double radius = squareSize / 2 - 5;

    mainCircle.setRadius(radius);
    innerCircle1.setRadius(radius - radius / 4);
    innerCircle2.setRadius(radius - radius / 2);
    if (isQueen()) {
      this.remove(innerCircle1);
      this.remove(innerCircle2);

      this.add(queenStar);
      queenStar.setInnerRadius(radius - radius / 1.3);
      queenStar.setOuterRadius(radius - radius / 2);
    } else {
      if (!getChildNodes().contains(innerCircle1)) {
        this.add(innerCircle1);
      }

      if (!getChildNodes().contains(innerCircle2)) {
        this.add(innerCircle2);
      }

      if (this.getChildNodes().contains(queenStar)) {
        this.remove(queenStar);
      }
    }
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public void setQueen(boolean queen) {
    this.queen = queen;
    updateShape();
  }

  public boolean isQueen() {
    return queen;
  }

  private boolean isValidStroke() {
    return board.isMyTurn() && white == board.isWhite() && !board.isEmulate();
  }

  public void setPosition(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public static Draught getSelectedDraught() {
    return selectedDraught;
  }

  @Override
  public String toString() {
    return "Draught{" +
        "deskSide=" + deskSide +
        ", row=" + row +
        ", col=" + col +
        ", white=" + white +
        ", queen=" + queen +
        '}';
  }
}
