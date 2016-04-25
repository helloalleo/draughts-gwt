package online.draughts.rus.draughts;

import com.ait.lienzo.client.core.animation.*;
import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.event.NodeTouchEndEvent;
import com.ait.lienzo.client.core.event.NodeTouchEndHandler;
import com.ait.lienzo.client.core.shape.Layer;
import online.draughts.rus.draughts.util.Operator;
import online.draughts.rus.draughts.util.PossibleOperators;
import online.draughts.rus.shared.dto.DraughtDto;
import online.draughts.rus.shared.dto.MoveDto;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Profile: alekspo
 * Date: 08.12.13
 * Time: 13:26
 */
public class Board extends Layer {
  private static final double ANIMATION_DURATION = 50;
  public static int DRAUGHTS_ON_SIDE = 12;
  private boolean selfPlay;
  // шашки, которые надо побить
  private List<Draught> myDraughtList;
  private List<Draught> opponentDraughtList;

  private boolean white;
  private boolean turn;
  private int rows;
  private int cols;
  private static final double removeDraughtFade = 200;
  private static final double moveDraughtDuration = 200;
  private boolean emulate = false; // эмулировать шашки
  // стек ходов шашек, когда они становятся дамками
  private Stack<Integer> queenStepStack = new Stack<>();

  private Set<Square> highlightedSquares = new HashSet<>();
  private Set<Square> capturedSquares = new HashSet<>();

  private Stack<Stroke> moveStack = new Stack<>();
  private Stack<Stroke> moveMyStack = new Stack<>();
  private Stack<Stroke> moveOpponentStack = new Stack<>();
  private int moveCounter = 0;

  private BoardBackgroundLayer backgroundLayer;

  private PlayComponent view;
  private Set<DraughtDto> currentPosition = new HashSet<>();
  private boolean fakeHighlight = false;

  public Board(BoardBackgroundLayer backgroundLayer,
               int rows,
               int cols,
               boolean white) {
    this.backgroundLayer = backgroundLayer;
    this.white = white;
    this.turn = white;

    this.rows = rows;
    this.cols = cols;

    myDraughtList = new ArrayList<>(rows / 2 * 3);
    opponentDraughtList = new ArrayList<>(rows / 2 * 3);

    placeDraughts();
    bindEvents();
  }

  public void setSelfPlay(boolean selfPlay) {
    this.selfPlay = selfPlay;
  }

  private void bindEvents() {
    addNodeMouseClickHandler(new NodeMouseClickHandler() {
      @Override
      public void onNodeMouseClick(NodeMouseClickEvent nodeMouseClickEvent) {
        Board.this.moveDraught(nodeMouseClickEvent.getX(), nodeMouseClickEvent.getY());
      }
    });

    addNodeTouchEndHandler(new NodeTouchEndHandler() {
      @Override
      public void onNodeTouchEnd(NodeTouchEndEvent nodeTouchEndEvent) {
        Board.this.moveDraught(nodeTouchEndEvent.getX(), nodeTouchEndEvent.getY());
      }
    });
  }

  private void placeDraughts() {
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 8; col++) {
        if (Square.isValid(row, col)) {
          opponentDraughtList.add(addDraught(row, col, !white));
        }
      }
    }

    // Now establish the Black side
    for (int row = 5; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        if (Square.isValid(row, col)) {
          myDraughtList.add(addDraught(row, col, white));
        }
      }
    }

//    if (!isMyTurn()) {
////      final Draught e1 = addDraught(4, 3, !white);
////      e1.setQueen(true);
////      opponentDraughtList.add(e1);
//////      opponentDraughtList.add(addDraught(1, 6, !white));
////      opponentDraughtList.add(addDraught(1, 2, !white));
//      opponentDraughtList.add(addDraught(3, 4, true));
////      final Draught e = addDraught(6, 1, white);
////      e.setQueen(true);
//      myDraughtList.add(addDraught(4,3, false));
//    }
//
//    if (isMyTurn()) {
////      final Draught e = addDraught(1, 6, !white);
////      e.setQueen(true);
//      opponentDraughtList.add(addDraught(3,4 , false));
////      final Draught e1 = addDraught(3, 4, white);
////      e1.setQueen(true);
//      myDraughtList.add(addDraught(4,3,true));
////      myDraughtList.add(addDraught(6, 1, white));
////      myDraughtList.add(addDraught(2, 1, white));
////      myDraughtList.add(addDraught(4, 5, white));
//    }
  }

  private Draught addDraught(int row, int col, boolean white) {
    try {
      Square square = backgroundLayer.getSquare(row, col);
      if (Square.isValid(row, col)) {
        Draught draught = new Draught(this,
            backgroundLayer.getDeskSide(),
            rows, cols,
            row, col,
            white,
            backgroundLayer.getOffsetX());
        add(draught);
        square.setOccupant(draught);
        return draught;
      }
    } catch (SquareNotFoundException ignore) {
    }
    return null;
  }

  /**
   * Функция заполняет массив highlightedSquares
   *
   * @param clickedPiece нажатая шашка
   */
  void highlightAllowedMoves(Draught clickedPiece) {
    Set<Square> capturedSquares = getCapturedSquares();
    if (!capturedSquares.isEmpty()) {
      Square clickedSquare;
      try {
        clickedSquare = backgroundLayer.getSquare(clickedPiece.getRow(), clickedPiece.getCol());
      } catch (SquareNotFoundException ignore) {
        return;
      }
      for (Square current : capturedSquares) {
        if (clickedSquare.isOnLine(current)) {
          highlightSquareToBeat(current);
          highlightedSquares.add(current);
        }
      }
      this.capturedSquares = capturedSquares;
    } else {
      Set<Square> possibleMoves = getPossibleMoves(clickedPiece);
      if (!possibleMoves.isEmpty()) {
        for (Square square : possibleMoves) {
          fadeInSquare(square);
          highlightedSquares.add(square);
        }
      }
    }

    backgroundLayer.draw();
  }

  public void checkIfGameStuck() {
    fakeHighlight = true;
    for (Draught draught : myDraughtList) {
      highlightAllowedMoves(draught);
    }
    fakeHighlight = false;
  }

  /**
   * Find all possible Squares to which this Draught can calcStroke
   *
   * @param clickedDraught Draught for which moves should be found
   */
  private Set<Square> getPossibleMoves(Draught clickedDraught) {
    Set<Square> possibleMoves = new HashSet<>();
    for (Draught draught : myDraughtList) {
      if (!clickedDraught.equals(draught)) {
        continue;
      }
      int row = draught.getRow();
      int col = draught.getCol();

      //Begin checking which moves are possible, keeping in mind that only black draughts may calcStroke up
      //and only red draughts may calcStroke downwards

      boolean queen = draught.isQueen();
      if (this.white) {
        possibleMoves.addAll(possibleMoves(Operator.SUBTRACTION, Operator.SUBTRACTION, row, col, queen));
        possibleMoves.addAll(possibleMoves(Operator.SUBTRACTION, Operator.ADDITION, row, col, queen));
        possibleMoves.addAll(possibleMoves(Operator.ADDITION, Operator.SUBTRACTION, row, col, queen));
        possibleMoves.addAll(possibleMoves(Operator.ADDITION, Operator.ADDITION, row, col, queen));
      } else {
        // top-left
        possibleMoves.addAll(possibleMoves(Operator.SUBTRACTION, Operator.SUBTRACTION, row, col, queen));
        // top-right
        possibleMoves.addAll(possibleMoves(Operator.SUBTRACTION, Operator.ADDITION, row, col, queen));
        // bottom-left
        possibleMoves.addAll(possibleMoves(Operator.ADDITION, Operator.SUBTRACTION, row, col, queen));
        // bottom-right
        possibleMoves.addAll(possibleMoves(Operator.ADDITION, Operator.ADDITION, row, col, queen));
      }

    }
    return possibleMoves;
  }

  private void fadeInSquare(Square square) {
    if (!fakeHighlight) {
      square.setAlpha(.5);
    }
  }

  private void highlightSquareToBeat(Square square) {
    if (!fakeHighlight) {
      square.setAlpha(.4);
    }
  }

  /**
   * Possible calcStroke in next and back direction
   */
  private Set<Square> possibleMoves(Operator opRow, Operator opCol, int row, int col, boolean queen) {
    if (queen) {
      return queenPossibleMoves(opRow, opCol, row, col);
    } else {
      if (inBounds(opRow.apply(row, 1), opCol.apply(col, 1)) && white) {
        Square square = null;
        try {
          square = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
        } catch (SquareNotFoundException ignore) {
        }
        // Check moves to the op1, op2 of this Draught
        if (square != null && !square.isOccupied()) {
          return Collections.singleton(square);
        }
      }
    }

    return new HashSet<>();
  }

  private Set<Square> queenPossibleMoves(Operator opRow, Operator opCol, int row, int col) {
    Set<Square> possibleMoves = new HashSet<>(rows * cols);

    Square square = null;
    try {
      square = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
    } catch (SquareNotFoundException ignore) {
    }
    while (true) {
      if (!inBounds(opRow.apply(row, 0), opCol.apply(col, 0))) {
        break;
      }
      if (square == null || square.isOccupied()) {
        break;
      }
      possibleMoves.add(square);

      row = opRow.apply(row, 1);
      col = opCol.apply(col, 1);

      try {
        square = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
      } catch (SquareNotFoundException ignore) {
      }
    }

    return possibleMoves;
  }

  void resetDesk() {
    backgroundLayer.resetDeskDrawing();
    highlightedSquares.clear();
  }

  private Square getSquare(double x, double y) throws SquareNotFoundException {
    return backgroundLayer.getSquare(x, y);
  }

  private Square getSquare(int row, int col) throws SquareNotFoundException {
    return backgroundLayer.getSquare(row, col);
  }

  private Set<Square> possibleNextMovePair(Square takenSquare, Operator opRow, Operator opCol, int row, int col,
                                    boolean queen) {
    Set<Square> nextMoves = new HashSet<>();
    if (inBounds(opRow.apply(row, 1), opCol.apply(col, 1))) {
      // Check moves to the op1, op2 of this Draught
      Square jumpSquare;
      try {
        jumpSquare = backgroundLayer.getSquare(opRow.apply(row, 2), opCol.apply(col, 2));
      } catch (SquareNotFoundException e) {
        return emptySet();
      }
      Square capturedSquare;
      try {
        capturedSquare = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
      } catch (SquareNotFoundException e) {
        return emptySet();
      }
      //if square is occupied, and the color of the Draught in square is
      //not equal to the Draught whose moves we are checking, then
      //check to see if we can make the jump by checking
      //the next square in the same direction
      if (queen) {
        if (inBounds(opRow.apply(row, 2), opCol.apply(col, 2))) {
          final Square square;
          try {
            square = backgroundLayer.getSquare(row, col);
          } catch (SquareNotFoundException e) {
            return emptySet();
          }
          if (capturedSquare.isBetween(square, takenSquare)) {
            return emptySet();
          }
          int r = row;
          int c = col;
          while (inBounds(opRow.apply(r, 2), opCol.apply(c, 2))
              && !capturedSquare.isOccupied()) {
            r = opRow.apply(r, 1);
            c = opCol.apply(c, 1);
            try {
              capturedSquare = backgroundLayer.getSquare(opRow.apply(r, 1), opCol.apply(c, 1));
            } catch (SquareNotFoundException e) {
              return emptySet();
            }
            try {
              jumpSquare = backgroundLayer.getSquare(opRow.apply(r, 2), opCol.apply(c, 2));
            } catch (SquareNotFoundException e) {
              return emptySet();
            }
          }
          if (jumpSquare != null && !jumpSquare.isOccupied()
              && capturedSquare.getOccupant().isWhite() != white) {
            int i = jumpSquare.getRow();
            int j = jumpSquare.getCol();
            Square squareStartLoop;
            try {
              squareStartLoop = backgroundLayer.getSquare(i, j);
            } catch (SquareNotFoundException e) {
              return emptySet();
            }
            while (inBounds(i, j) && !squareStartLoop.isOccupied()) {
              final Square squareLoop;
              try {
                squareLoop = backgroundLayer.getSquare(i, j);
              } catch (SquareNotFoundException e) {
                continue;
              }
              nextMoves.add(squareLoop);
              i = opRow.apply(i, 1);
              j = opCol.apply(j, 1);
            }
          }
        }
      } else {
        final Square square;
        try {
          square = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
        } catch (SquareNotFoundException e) {
          return emptySet();
        }
        if (square.isOccupied()) {
          // нельзя возвращяться назад
          if (takenSquare == jumpSquare) {
            return emptySet();
          }
          if (inBounds(opRow.apply(row, 2), opCol.apply(col, 2))) {
            if (!jumpSquare.isOccupied()
                && capturedSquare.getOccupant().isWhite() != white) {
              nextMoves.add(jumpSquare);
            }
          }
        }
      }
    }
    return nextMoves;
  }

  private HashSet<Square> emptySet() {
    return new HashSet<>();
  }

  /**
   * получаем флаги передвижения и взятую шашку
   *
   * @param from The square from which we are moving
   * @param to   The square to which we are moving
   * @return возвращете передвижение с установленными флагами и взятой шашкой
   */
  private Stroke calcStroke(Square from, Square to) {
    Stroke stroke = new Stroke();
    Draught beingMoved = from.getOccupant();

    from.setOccupant(null);
    beingMoved.setPosition(to.getRow(), to.getCol());
    to.setOccupant(beingMoved);

    stroke.setStartSquare(from);
    stroke.setEndSquare(to);
    stroke.setWhite(isWhite());

    final boolean first = isFirstMoveFlag();
    stroke.setFirst(first);

    if (!capturedSquares.isEmpty()) {
      //A jump has been performed, so getProvider the Square that lies between from and to
      Square takenSquare = null;
      // проходим по сбитым шашкам из метода getCapturedSquares()
      for (Square s : capturedSquares) {
        if (to.isOnLine(s) && s.isBetween(from, to)) {
          takenSquare = s;
          break;
        }
      }

      if (takenSquare == null) {
        stroke.setOnSimpleMove();
        return stroke;
      }

      int row = to.getRow();
      int col = to.getCol();
      boolean queen = beingMoved.isQueen();
      if (!queen && (0 == row && beingMoved.isWhite() || (rows - 1) == row && !beingMoved.isWhite())) {
        queen = true;
      }
      stroke.setQueen(queen);
      Set<Square> jumpMoves = new HashSet<>();
      if (this.white) {
        jumpMoves.addAll(possibleNextMovePair(from, Operator.SUBTRACTION, Operator.SUBTRACTION, row, col, queen));
        jumpMoves.addAll(possibleNextMovePair(from, Operator.SUBTRACTION, Operator.ADDITION, row, col, queen));
        jumpMoves.addAll(possibleNextMovePair(from, Operator.ADDITION, Operator.SUBTRACTION, row, col, queen));
        jumpMoves.addAll(possibleNextMovePair(from, Operator.ADDITION, Operator.ADDITION, row, col, queen));
      } else {
        // top-left
        jumpMoves.addAll(possibleNextMovePair(from, Operator.SUBTRACTION, Operator.SUBTRACTION, row, col, queen));
        // top-right
        jumpMoves.addAll(possibleNextMovePair(from, Operator.SUBTRACTION, Operator.ADDITION, row, col, queen));
        // bottom-left
        jumpMoves.addAll(possibleNextMovePair(from, Operator.ADDITION, Operator.SUBTRACTION, row, col, queen));
        // bottom-right
        jumpMoves.addAll(possibleNextMovePair(from, Operator.ADDITION, Operator.ADDITION, row, col, queen));
      }

      stroke.setTakenSquare(takenSquare);
      Stroke prev = null;
      if (!moveStack.empty()) {
        prev = moveStack.peek();
      }
      if (jumpMoves.isEmpty()) { // если нет шашек которые бьются дальше
        stroke.setOnStopBeat();
        if (null == prev || prev.isStopBeat() || prev.isSimple()) {
          stroke.setOnStartBeat();
        }
      } else {
        stroke.setOnContinueBeat();
        if (null == prev || prev.isStopBeat() || prev.isSimple()) {
          stroke.setOnStartBeat();
        }
      }
      removeDraughtFrom(takenSquare);
    } else {
      stroke.setOnSimpleMove();
    }

    if (stroke.isSimple() || stroke.isStartBeat()) {
      moveCounter++;
    }
    stroke.setNumber(moveCounter);

    return stroke;
  }

  private void removeDraughtFrom(Square takenSquare) {
    removeDraughtFrom(takenSquare, false);
  }

  /**
   * Удаляет шашку с клетки
   *
   * @param takenSquare клетка, на которой находится шашка
   * @param clearDesk   очистка доски, указывается в методах где выполняется очистка доски от шашек, а
   *                    не взятие шашки
   */
  private void removeDraughtFrom(Square takenSquare, final boolean clearDesk) {
    opponentDraughtList.remove(takenSquare.getOccupant());
    final Draught takenDraught = takenSquare.getOccupant();
    if (takenDraught == null) {
      return;
    }
    myDraughtList.remove(takenDraught);
    takenSquare.setOccupant(null);
    AnimationProperties props = new AnimationProperties();
    props.push(AnimationProperty.Properties.ALPHA(0));
    takenDraught.animate(AnimationTweener.LINEAR, props,
        removeDraughtFade, new IAnimationCallback() {
          @Override
          public void onStart(IAnimation iAnimation, IAnimationHandle iAnimationHandle) {
          }

          @Override
          public void onFrame(IAnimation iAnimation, IAnimationHandle iAnimationHandle) {
          }

          @Override
          public void onClose(IAnimation iAnimation, IAnimationHandle iAnimationHandle) {
            remove(takenDraught);
            if (!isEmulate() && !clearDesk) {
              getView().checkWinner();
            }
          }
        });
    capturedSquares = new HashSet<>();
  }

  private boolean inBounds(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }

  /**
   * Функция используется в плеере. Назанчение добавить сбитую шашку
   *
   * @param stroke     ход
   * @param stepCursor курсор для дамки
   */
  public void doEmulatedMoveBack(Stroke stroke, int stepCursor) {
    if (stroke.isSimple()) {
      Square tmp = stroke.getStartSquare();
      stroke.setStartSquare(stroke.getEndSquare());
      stroke.setEndSquare(tmp);
      doMove(stroke, stepCursor, true);
    } else {
      final Square takenSquare = stroke.getTakenSquare();
      if (takenSquare != null) {
        if (stroke.isFirst()) {
          opponentDraughtList.add(addDraught(takenSquare.getRow(), takenSquare.getCol(), false));
        } else {
          myDraughtList.add(addDraught(takenSquare.getRow(), takenSquare.getCol(), true));
        }
      }
      strokeCanceled(stroke, stepCursor, true);
    }
  }

  public void moveOpponent(Stroke stroke) {
    Square startSquare, endSquare, takenSquare = null;
    try {
      startSquare = getSquare(stroke.getStartSquare().getRow(), stroke.getStartSquare().getCol());
      endSquare = getSquare(stroke.getEndSquare().getRow(), stroke.getEndSquare().getCol());
      if (stroke.getTakenSquare() != null) {
        takenSquare = getSquare(stroke.getTakenSquare().getRow(), stroke.getTakenSquare().getCol());
      }
    } catch (SquareNotFoundException e) {
      return;
    }

    if (stroke.isCancel()) {
      if (stroke.isStopBeat()) {
        stroke.setStartSquare(endSquare);
        stroke.setEndSquare(startSquare);
      } else {
        stroke.setStartSquare(endSquare);
        stroke.setEndSquare(startSquare);
      }
      moveOpponentStack.pop();
      moveStack.pop();
    } else {
      stroke.setStartSquare(startSquare);
      stroke.setEndSquare(endSquare);
      moveOpponentStack.push(stroke);
      moveStack.push(stroke);
    }

    stroke.setTakenSquare(takenSquare);

    if (!stroke.isCancel()) {
      final boolean first = isFirstMoveFlag();
      stroke.setNumber(stroke.getNumber())
          .setFirst(first);
      Stroke strokeForNotation = stroke;
      if (!isWhite()) {
        strokeForNotation = stroke.flip();
      }
      getView().addNotationStroke(strokeForNotation);
    }

    // отправляем ход на сервер и сохраняем его там
//    if (isWhite()) {
//      MoveDto move = MoveFactory.createMoveFromStroke(stroke);
//      move.setScreenshot(getView().takeScreenshot());
//      move.setFixate(true);
//      getView().doSaveMove(move);
//    }

    doMove(stroke); // сделать ход
  }

  private void doMove(Stroke stroke) {
    doMove(stroke, -1, false);
  }

  public void doMove(Stroke stroke, int stepCursor) {
    doMove(stroke, stepCursor, false);
  }

  private void doMove(final Stroke stroke, final int stepCursor, final boolean back) {
    doMove(stroke, stepCursor, back, false);
  }

  /**
   * Функция, которая выполняет физическое перемещение шашек
   * Используется в эмуляторе
   *
   * @param stroke     ход
   * @param stepCursor для дамок
   * @param back       ход назад
   */
  private void doMove(final Stroke stroke, final int stepCursor, final boolean back, final boolean cancel) {
    final Draught occupant = stroke.getStartSquare().getOccupant();

    // вычисляем координаты для перемещения шашки относительно её центра
    occupant.updateShape();

    final Square endSquare = stroke.getEndSquare();
    final Square startSquare = stroke.getStartSquare();
    final Square taken = stroke.getTakenSquare();

    final double mouseMovedX = occupant.getX() + endSquare.getCenterX() - startSquare.getCenterX();
    final double mouseMovedY = occupant.getY() + endSquare.getCenterY() - startSquare.getCenterY();

    occupant.animate(AnimationTweener.LINEAR, new AnimationProperties(
        AnimationProperty.Properties.X(mouseMovedX),
        AnimationProperty.Properties.Y(mouseMovedY)
    ), moveDraughtDuration, new AnimationCallback() {
      @Override
      public void onClose(IAnimation animation, IAnimationHandle handle) {
        super.onClose(animation, handle);
        // if draught achieved start of desk mark it as Queen
        if (!occupant.isQueen()) {
          if (isEmulate()) {
            if (null != endSquare.getOccupant() && endSquare.getOccupant().isWhite() && 0 == endSquare.getRow()
                || null != endSquare.getOccupant() && !endSquare.getOccupant().isWhite()
                && (rows - 1) == endSquare.getRow()) {
              occupant.setPosition(endSquare.getRow(), endSquare.getCol());
              occupant.setQueen(true);
              queenStepStack.push(stepCursor);
            }
          } else if ((0 == occupant.getRow()
              && (occupant.isWhite() && isWhite()
              || !occupant.isWhite() && !isWhite()))
              || ((rows - 1) == occupant.getRow()
              && (!occupant.isWhite() && isWhite()
              || occupant.isWhite() && !isWhite()))) {
            occupant.setPosition(endSquare.getRow(), endSquare.getCol());
            occupant.setQueen(true);
          }
        } else if (cancel) {
          if (((1 == occupant.getRow() || 2 == occupant.getRow())
              && (occupant.isWhite() && isWhite()
              || !occupant.isWhite() && !isWhite()))
              || (((rows - 2) == occupant.getRow() || (rows - 3) == occupant.getRow())
              && (!occupant.isWhite() && isWhite()
              || occupant.isWhite() && !isWhite()))) {
            occupant.setPosition(endSquare.getRow(), endSquare.getCol());
            occupant.setQueen(false);
          }
        }
      }
    });

    endSquare.setOccupant(occupant);
    occupant.setPosition(endSquare.getRow(), endSquare.getCol());

    if (taken != null && !back) {
      if (stroke.isCancel()) {
        if (isMyTurn()) {
          if (stroke.isContinueBeat()) {
            opponentDraughtList.add(addDraught(taken.getRow(), taken.getCol(), !isWhite()));
          } else {
            myDraughtList.add(addDraught(taken.getRow(), taken.getCol(), isWhite()));
          }
        } else {
          if (stroke.isContinueBeat()) {
            myDraughtList.add(addDraught(taken.getRow(), taken.getCol(), isWhite()));
          } else {
            opponentDraughtList.add(addDraught(taken.getRow(), taken.getCol(), !isWhite()));
          }
        }
        // обновляем количество побитых шашек
        getView().checkWinner();
      } else {
        removeDraughtFrom(taken);
      }
    }

    if (!stroke.isContinueBeat() && !isEmulate()) {
      toggleTurn();
    }

    // переносим этот код из конца анимации сюда, потому что в ускоренной промотки он не выполняется.
    if (isEmulate() && occupant.isQueen()) {
      if ((null != endSquare.getOccupant() && endSquare.getOccupant().isWhite()
          && 0 == startSquare.getRow()
          || null != endSquare.getOccupant() && !endSquare.getOccupant().isWhite()
          && (rows - 1) == startSquare.getRow())
          && queenStepStack.peek() == stepCursor + 1) {
        occupant.setPosition(endSquare.getRow(), endSquare.getCol());
        occupant.setQueen(false);
        queenStepStack.pop();
      }
    }
    startSquare.setOccupant(null);
    highlightedSquares.clear();
  }

  public boolean isWhite() {
    return white;
  }

  public boolean isMyTurn() {
    return turn;
  }

  private void toggleTurn() {
    turn = !turn;
    getView().toggleTurn(turn);
  }

  public List<Draught> getMyDraughts() {
    return myDraughtList;
  }

  public List<Draught> getOpponentDraughts() {
    return opponentDraughtList;
  }

  boolean isEmulate() {
    return emulate;
  }

  public void setEmulate(boolean emulate) {
    this.emulate = emulate;
  }

  /**
   * Функция перемещает шашку игрока на заданные координаты
   *
   * @param clickX нажатие мышки X
   * @param clickY нажатие мышки Y
   */
  private void moveDraught(double clickX, double clickY) {
    Draught selectedDraught = findClickedDraught(clickX, clickY);
    if (selectedDraught != null) {
      selectedDraught.onNodeTouch(selfPlay);
    } else {
      selectedDraught = Draught.getSelectedDraught();
    }
    if (selectedDraught != null && !highlightedSquares.isEmpty()) {
      Square endSquare = null, startSquare = null;
      try {
        endSquare = getSquare(clickX, clickY);
      } catch (SquareNotFoundException ignore) {
      }

      try {
        startSquare = getSquare(selectedDraught.getRow(), selectedDraught.getCol());
      } catch (SquareNotFoundException ignore) {
      }

      if (highlightedSquares.contains(endSquare) && startSquare != null && startSquare.isOnLine(endSquare)
          && endSquare != null) {
        // получаем флаги передвижения и взятую шашку
        final Stroke stroke = calcStroke(startSquare, endSquare);

        boolean isSimpleMove = stroke.isSimple();
        if (isSimpleMove || stroke.isStopBeat()) {
          toggleTurn();
        }
        if (!selectedDraught.isQueen()) {
          if ((0 == selectedDraught.getRow()
              && (selectedDraught.isWhite() && isWhite()
              || !selectedDraught.isWhite() && !isWhite())
              || (rows - 1) == selectedDraught.getRow() && !selectedDraught.isWhite())) {
            selectedDraught.setQueen(true);
          }
        }
        Stroke strokeForNotation = stroke;
        if (!isWhite()) {
          strokeForNotation = stroke.flip();
        }
        getView().addNotationStroke(strokeForNotation);

        AnimationProperties props = new AnimationProperties();
        props.push(AnimationProperty.Properties.X(endSquare.getCenterX()));
        props.push(AnimationProperty.Properties.Y(endSquare.getCenterY()));

        selectedDraught.animate(AnimationTweener.LINEAR, props, ANIMATION_DURATION, new IAnimationCallback() {
          @Override
          public void onStart(IAnimation animation, IAnimationHandle handle) {
          }

          @Override
          public void onFrame(IAnimation animation, IAnimationHandle handle) {
          }

          @Override
          public void onClose(IAnimation animation, IAnimationHandle handle) {
            final MoveDto move = MoveFactory.createMoveFromStroke(stroke);

//            if (isWhite()) {
//              move.setScreenshot(getView().takeScreenshot());
//              move.setFixate(true);
//            }
            backgroundLayer.resetDeskDrawing();

            // перемещаем шашку на стороне оппонента
            getView().doPlayerMove(move);

            moveMyStack.push(stroke);
            moveStack.push(stroke);
          }
        });

        props = new AnimationProperties();
        props.push(AnimationProperty.Properties.SCALE(1.0));

        selectedDraught.animate(AnimationTweener.LINEAR, props, ANIMATION_DURATION);
      }
    }
  }

  private Draught findClickedDraught(double clickX, double clickY) {
    final Square square;
    try {
      square = getSquare(clickX, clickY);
    } catch (SquareNotFoundException e) {
      return null;
    }
    if (turn && getClickedDraughtFromList(square, myDraughtList)) {
      return square.getOccupant();
    }
    if (!turn && selfPlay && getClickedDraughtFromList(square, opponentDraughtList)) {
      return square.getOccupant();
    }
    return null;
  }

  private boolean getClickedDraughtFromList(Square square, List<Draught> draughtList) {
    for (Draught draught : draughtList) {
      final Square current;
      try {
        current = backgroundLayer.getSquare(draught.getRow(), draught.getCol());
      } catch (SquareNotFoundException e) {
        continue;
      }
      if (square == current) {
        return true;
      }
    }
    return false;
  }

  /**
   * Вычисляем является ли ход первым в полном ходе a1-b2 b2-b3. а1-b2 первый ход
   *
   * @return первый ход?
   */
  private boolean isFirstMoveFlag() {
    if (isWhite()) {
      return isMyTurn();
    } else {
      return !isMyTurn();
    }
  }

  public void clearDesk() {
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        try {
          Square square = getSquare(i, j);
          removeDraughtFrom(square, true);
        } catch (SquareNotFoundException ignore) {
        }
      }
    }
  }

  private void strokeCanceled(Stroke stroke) {
    strokeCanceled(stroke, -1, false);
  }

  private void strokeCanceled(Stroke stroke, int stepCursor, boolean back) {
    if (!isEmulate()) {
      stroke = stroke.flip();
    }
    int startRow = stroke.getStartSquare().getRow();
    int startCol = stroke.getStartSquare().getCol();

    int endRow = stroke.getEndSquare().getRow();
    int endCol = stroke.getEndSquare().getCol();

    Square startSquare, endSquare;
    try {
      startSquare = getSquare(startRow, startCol);
      endSquare = getSquare(endRow, endCol);
    } catch (SquareNotFoundException e) {
      return;
    }

    stroke.setStartSquare(endSquare);
    stroke.setEndSquare(startSquare);

    Square taken = null;
    if (!stroke.isSimple()) {
      int beatenRow = stroke.getTakenSquare().getRow();
      int beatenCol = stroke.getTakenSquare().getCol();
      try {
        taken = backgroundLayer.getSquare(beatenRow, beatenCol);
      } catch (SquareNotFoundException ignore) {
      }
      stroke.setTakenSquare(taken);
    }
    doMove(stroke, stepCursor, back, true);
  }

  public void moveOpponentCanceled(Stroke stroke) {
    strokeCanceled(stroke);
    Stroke canceled = moveOpponentStack.pop();
    moveStack.pop();
    if (isCancelFirstMove(canceled)) {
      moveCounter--;
    }
  }

  public void moveMyCanceled(Stroke stroke) {
    strokeCanceled(stroke);
    Stroke canceled = moveMyStack.pop();
    moveStack.pop();
    if (isCancelFirstMove(canceled)) {
      moveCounter--;
    }
  }

  /**
   * Отменяем первый ход? Тот который стоит за номером.
   * Отменяем если ход первый и простой или ход первый и начало взятия
   *
   * @param canceled отменяемы ход
   * @return отменяем ли первый ход
   */
  private boolean isCancelFirstMove(Stroke canceled) {
    return canceled.isFirst() && canceled.isSimple() || canceled.isFirst() && canceled.isStartBeat();
  }

  public Stroke getLastMove() {
    if (moveMyStack.isEmpty()) {
      return null;
    }
    return moveMyStack.lastElement();
  }

  public Stroke getLastOpponentMove() {
    if (moveOpponentStack.isEmpty()) {
      return null;
    }
    return moveOpponentStack.lastElement();
  }

  public void setView(PlayComponent view) {
    this.view = view;
  }

  public PlayComponent getView() {
    return view;
  }

  public void addDraught(double x, double y, boolean white, boolean queen) {
    Square square;
    try {
      square = backgroundLayer.getSquare(x, y);
    } catch (SquareNotFoundException ignore) {
      return;
    }
    if (square.isOccupied()) {
      return;
    }
    final Draught draught = addDraught(square.getRow(), square.getCol(), white);
    if (draught != null) {
      draught.setQueen(queen);
    } else {
      return;
    }
    if (white == isWhite()) {
      myDraughtList.add(draught);
    } else {
      opponentDraughtList.add(draught);
    }
    draw();
  }

  public void removeDraughtFrom(double x, double y) {
    Square square;
    try {
      square = backgroundLayer.getSquare(x, y);
    } catch (SquareNotFoundException ignore) {
      return;
    }
    if (!square.isOccupied()) {
      return;
    }
    removeDraughtFrom(square);
    resetDesk();
  }

  public Set<DraughtDto> getCurrentPosition() {
    return currentPosition;
  }

  private Set<Square> getCapturedSquares() {
    Set<Square> capturedSquares = new HashSet<>();
    for (Draught draught : myDraughtList) {
      final Set<Square> capturedSquaresByDraught = getCapturedSquaresByDraught(draught);
      if (null != capturedSquaresByDraught) {
        capturedSquares.addAll(capturedSquaresByDraught);
      }
    }
    return capturedSquares;
  }

  private Set<Square> getCapturedSquaresByDraught(Draught draught) {
        /* Possible moves include up-left, up-right, down-left, down-right
     * This corresponds to (row-- col--), (row-- col++),
		 * 						(row++ col--), (row++ col++) respectively
		 */
    if (null == draught) {
      return null;
    }
    Set<Square> jumpMoves = new HashSet<>();
    int row = draught.getRow();
    int col = draught.getCol();

    //Begin checking which moves are possible, keeping in mind that only black draughts may calcStroke up
    //and only red draughts may calcStroke downwards

    boolean draughtColor = draught.isWhite();
    boolean queen = draught.isQueen();
    if (this.white) {
      jumpMoves.addAll(jumpMoves(Operator.SUBTRACTION, Operator.SUBTRACTION, row, col, draughtColor, queen));
      jumpMoves.addAll(jumpMoves(Operator.SUBTRACTION, Operator.ADDITION, row, col, draughtColor, queen));
      jumpMoves.addAll(jumpMoves(Operator.ADDITION, Operator.SUBTRACTION, row, col, draughtColor, queen));
      jumpMoves.addAll(jumpMoves(Operator.ADDITION, Operator.ADDITION, row, col, draughtColor, queen));
    } else {
      // top-left
      jumpMoves.addAll(jumpMoves(Operator.SUBTRACTION, Operator.SUBTRACTION, row, col, draughtColor, queen));
      // top-right
      jumpMoves.addAll(jumpMoves(Operator.SUBTRACTION, Operator.ADDITION, row, col, draughtColor, queen));
      // bottom-left
      jumpMoves.addAll(jumpMoves(Operator.ADDITION, Operator.SUBTRACTION, row, col, draughtColor, queen));
      // bottom-right
      jumpMoves.addAll(jumpMoves(Operator.ADDITION, Operator.ADDITION, row, col, draughtColor, queen));
    }
    return jumpMoves;
  }

  /**
   * Вычисляет ходы в которых шашка бьёт
   *
   * @param opRow        проба по вертикали
   * @param opCol        проба по горизонтали
   * @param row          координаты шашки которая бьёт
   * @param col          координаты шашки которая бьёт
   * @param draughtColor цвет шашки которая бьёт. Не должен совпадать с цветом побитой шашки
   * @param queen        дамка?
   * @return список клеток куда можно побить
   */
  private List<Square> jumpMoves(Operator opRow, Operator opCol, int row, int col, boolean draughtColor,
                                 boolean queen) {
    if (queen) {
      return queenJumpMoves(opRow, opCol, row, col, draughtColor, 0);
    } else {
      if (inBounds(opRow.apply(row, 1), opCol.apply(col, 1)) && white) {
        Square square = null, nextSquare = null;
        try {
          square = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
          nextSquare = backgroundLayer.getSquare(opRow.apply(row, 2), opCol.apply(col, 2));
        } catch (SquareNotFoundException ignore) {
        }
        // Check moves to the op1, op2 of this Draught
        if (square != null && square.isOccupied() && square.getOccupant().isWhite() != draughtColor) {
          //if square is occupied, and the color of the Draught in square is
          //not equal to the Draught whose moves we are checking, then
          //check to see if we can make the jump by checking
          //the next square in the same direction
          if (inBounds(opRow.apply(row, 2), opCol.apply(col, 2))) {
            if (nextSquare != null && !nextSquare.isOccupied()) {
              return Collections.singletonList(nextSquare);
            }
          }
        }
      }

      if (inBounds(opRow.apply(row, 2), opCol.apply(col, 2))) {
        Square square = null, nextSquare = null;
        try {
          square = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
          nextSquare = backgroundLayer.getSquare(opRow.apply(row, 2), opCol.apply(col, 2));
        } catch (SquareNotFoundException ignore) {
        }
        if (nextSquare != null && !nextSquare.isOccupied() && square != null && square.isOccupied()
            && square.getOccupant().isWhite() != draughtColor) {
          return Collections.singletonList(nextSquare);
        }
      }
    }
    return new ArrayList<>();
  }

  private List<Square> queenJumpMoves(Operator opRow, Operator opCol, int row, int col, boolean draughtColor, int depth) {
    List<Square> jumpMoves = new ArrayList<>(rows * cols);

    Square square = null, nextSquare = null;
    try {
      square = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
      nextSquare = backgroundLayer.getSquare(opRow.apply(row, 2), opCol.apply(col, 2));
    } catch (SquareNotFoundException ignore) {
    }
    while (true) {
      if (!inBounds(opRow.apply(row, 0), opCol.apply(col, 0))) {
        break;
      }
      if (nextSquare != null && !nextSquare.isOccupied() && square != null && square.isOccupied()
          && square.getOccupant().isWhite() != draughtColor) {
        int r = opRow.apply(row, 2), c = opCol.apply(col, 2);
        while (inBounds(r, c)) {
          try {
            Square s = backgroundLayer.getSquare(opRow.apply(r, 0), opCol.apply(c, 0));
            if (s.isOccupied()) {
              break;
            }
            jumpMoves.add(s);
            if (depth == 1) {
              break;
            }
          } catch (SquareNotFoundException ignore) {
          }
          r = opRow.apply(r, 1);
          c = opCol.apply(c, 1);
        }
        break;
      } else if (square != null && square.isOccupied()) {
        break;
      }

      row = opRow.apply(row, 1);
      col = opCol.apply(col, 1);

      try {
        square = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
        nextSquare = backgroundLayer.getSquare(opRow.apply(row, 2), opCol.apply(col, 2));
      } catch (SquareNotFoundException ignore) {
      }
    }

    depth++;
    if (!jumpMoves.isEmpty()) {
      List<Square> subMove = new ArrayList<>();
      for (Square move : jumpMoves) {
        if (opRow.equals(PossibleOperators.SUB) && opCol.equals(PossibleOperators.SUB)) { // top-left & bottom-right
          subMove.addAll(queenJumpMoves(Operator.SUBTRACTION, Operator.ADDITION, move.getRow(), move.getCol(), draughtColor, depth));
          subMove.addAll(queenJumpMoves(Operator.ADDITION, Operator.SUBTRACTION, move.getRow(), move.getCol(), draughtColor, depth));
        } else if (opRow.equals(PossibleOperators.SUB) && opCol.equals(PossibleOperators.ADD)) { // bottom-left & top-right
          subMove.addAll(queenJumpMoves(Operator.SUBTRACTION, Operator.SUBTRACTION, move.getRow(), move.getCol(), draughtColor, depth));
          subMove.addAll(queenJumpMoves(Operator.ADDITION, Operator.ADDITION, move.getRow(), move.getCol(), draughtColor, depth));
        } else if (opRow.equals(PossibleOperators.ADD) && opCol.equals(PossibleOperators.ADD)) { // top-left & bottom-right
          subMove.addAll(queenJumpMoves(Operator.SUBTRACTION, Operator.ADDITION, move.getRow(), move.getCol(), draughtColor, depth));
          subMove.addAll(queenJumpMoves(Operator.ADDITION, Operator.SUBTRACTION, move.getRow(), move.getCol(), draughtColor, depth));
        } else if (opRow.equals(PossibleOperators.ADD) && opCol.equals(PossibleOperators.SUB)) { // bottom-left & top-right
          subMove.addAll(queenJumpMoves(Operator.SUBTRACTION, Operator.SUBTRACTION, move.getRow(), move.getCol(), draughtColor, depth));
          subMove.addAll(queenJumpMoves(Operator.ADDITION, Operator.ADDITION, move.getRow(), move.getCol(), draughtColor, depth));
        }
      }
      if (!subMove.isEmpty()) {
        List<Square> resultMove = new ArrayList<>();
        for (Square move : jumpMoves) {
          for (Square sq : subMove) {
            if (move.isOnLine(sq) && !resultMove.contains(move)) {
              resultMove.add(move);
            }
          }
        }
        return resultMove;
      }
    }
    return jumpMoves;
  }
}
