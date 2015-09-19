package online.shashki.rus.shashki;


import com.ait.lienzo.client.core.animation.AnimationProperties;
import com.ait.lienzo.client.core.animation.AnimationProperty;
import com.ait.lienzo.client.core.animation.AnimationTweener;
import com.ait.lienzo.client.core.event.NodeMouseDownEvent;
import com.ait.lienzo.client.core.event.NodeMouseDownHandler;
import com.ait.lienzo.client.core.event.NodeTouchEndEvent;
import com.ait.lienzo.client.core.event.NodeTouchEndHandler;
import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Star;
import com.ait.lienzo.client.core.types.Shadow;
import com.ait.lienzo.shared.core.types.Color;
import com.ait.lienzo.shared.core.types.ColorName;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Profile: alekspo
 * Date: 07.12.13
 * Time: 21:08
 */
public class Draught extends Group implements Serializable {
  private final int deskSide;
  private static Draught selectedDraught;
  private Board board;

  private int row;
  private int col;
  private boolean white;

  private Circle mainCircle = new Circle(0);
  private Circle innerCircle1 = new Circle(0);
  private Circle innerCircle2 = new Circle(0);
  private Circle innerCircle3 = new Circle(0);
  private Star queenStar = new Star(5, 0, 0);

  private int rows;
  private int cols;

  private boolean queen;

  private Draught currentDraught;
  private double offsetX;

  public Draught(int deskSide, int rows, int cols, int row, int col, boolean white, double offsetX) {
    this.deskSide = deskSide;
    this.row = row;
    this.col = col;
    this.rows = rows;
    this.cols = cols;
    this.white = white;
    this.offsetX = offsetX;

    setListening(true);

    setAlpha(.8);
    add(mainCircle);
    add(innerCircle1);
    add(innerCircle2);
    add(innerCircle3);

    queenStar.setFillColor(white ? ColorName.BLUE : ColorName.RED);

    Circle[] circles = {mainCircle, innerCircle1, innerCircle2, innerCircle3};

    int i = 0;
    for (Circle circle : circles) {
      if (isWhite()) {
        if (i % 2 == 0) {
          circle.setShadow(new Shadow(ColorName.GRAY, 6, 0, 4));
        } else {
          circle.setShadow(new Shadow(ColorName.GRAY, 6, 0, -4));
        }
      } else {
        if (i % 2 == 0) {
          circle.setShadow(new Shadow(ColorName.BLACK, 6, 0, 4));
        } else {
          circle.setShadow(new Shadow(ColorName.BLACK, 6, 0, -4));
        }
      }
      i++;
      circle.setFillColor(white ? ColorName.WHITE : Color.fromColorString("#555"));
    }

    updateShape();

    // TODO: Not Compile
    addNodeMouseDownHandler(new NodeMouseDownHandler() {
      @Override
      public void onNodeMouseDown(NodeMouseDownEvent nodeMouseDownEvent) {
        onNodeTouch((Draught) nodeMouseDownEvent.getSource());
      }
    });

    addNodeTouchEndHandler(new NodeTouchEndHandler() {
      @Override
      public void onNodeTouchEnd(NodeTouchEndEvent nodeTouchEndEvent) {
        onNodeTouch((Draught) nodeTouchEndEvent.getSource());
      }
    });
  }

  private void onNodeTouch(Draught draught) {
    if (!isValidStroke()) {
      return;
    }

    board = (Board) getParent();
    currentDraught = draught;

    if (selectedDraught != null) {
      AnimationProperties props = new AnimationProperties();
      props.push(AnimationProperty.Properties.SCALE(1));

      selectedDraught.animate(AnimationTweener.LINEAR, props, 100);
    }

    AnimationProperties props = new AnimationProperties();
    props.push(AnimationProperty.Properties.SCALE(1.3));

    currentDraught.animate(AnimationTweener.LINEAR, props, 100);

    selectedDraught = currentDraught;

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
    if (isQueen()) {
      this.remove(innerCircle2);
      this.remove(innerCircle3);

      this.add(queenStar);
      queenStar.setInnerRadius(radius - radius / 1.3);
      queenStar.setOuterRadius(radius - radius / 2);
    } else {
      if (!getChildNodes().contains(innerCircle2)) {
        this.add(innerCircle2);
      }
      if (!getChildNodes().contains(innerCircle3)) {
        this.add(innerCircle3);
      }

      if (this.getChildNodes().contains(queenStar)) {
        this.remove(queenStar);
      }

      innerCircle2.setRadius(radius - radius / 2);
      innerCircle3.setRadius(radius - radius / 1.3);
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
    Board board = (Board) getParent();
    return board.isMyTurn() && isWhite() == board.isWhite() && !board.isEmulate();
  }

  private boolean isAllowed(Square newSquare) {
    return newSquare.getAlpha() < 1.0;
  }

  public void setPosition(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public static Draught getSelectedDraught() {
    return selectedDraught;
  }
}
