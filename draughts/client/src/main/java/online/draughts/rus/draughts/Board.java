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
import online.draughts.rus.shared.util.StringUtils;

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
  private List<Square> capturedSquares = new ArrayList<>();
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

  private List<Square> highlightedSquares = new ArrayList<>();

  private Stack<Stroke> moveStack = new Stack<>();
  private Stack<Stroke> moveMyStack = new Stack<>();
  private Stack<Stroke> moveOpponentStack = new Stack<>();
  private int moveCounter = 0;

  private BoardBackgroundLayer backgroundLayer;

  private PlayComponent view;
  private Set<DraughtDto> initialPos = new HashSet<>();

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

  public boolean isSelfPlay() {
    return selfPlay;
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
        initialPos.add(new DraughtDto(row, col, white, false));
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
  public void highlightAllowedMoves(Draught clickedPiece) {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        try {
          Square square = backgroundLayer.getSquare(row, col);
          Draught draught = square.getOccupant();
          if (draught != null && draught.isWhite() == clickedPiece.isWhite()) {
            highlightPossibleMoves(draught, clickedPiece);
          }
        } catch (SquareNotFoundException ignore) {
//          SHLog.debug(e.getLocalizedMessage(), e);
        }
      }
    }
    // если нашли шашку, которая будет побита, удаляем прорисоку обычных ходов
    if (!capturedSquares.isEmpty()) {
      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          try {
            Square square = backgroundLayer.getSquare(row, col);
            if (null != square && isHighlightedSquare(square)) {
              fadeOutSquare(square);
              highlightedSquares.remove(square);
            }
          } catch (SquareNotFoundException ignore) {
          }
        }
      }
    }
    backgroundLayer.draw();
  }

  /**
   * Find all possible Squares to which this Draught can calcStroke
   *
   * @param p Draught for which moves should be found
   */
  private void highlightPossibleMoves(Draught p, Draught clickedDraught) {
    /* Possible moves include up-left, up-right, down-left, down-right
     * This corresponds to (row-- col--), (row-- col++),
		 * 						(row++ col--), (row++ col++) respectively
		 */
    List<Square> possibleMoves = new ArrayList<>();
    List<Square> jumpMoves = new ArrayList<>();
    boolean white = p.isWhite();
    int row = p.getRow();
    int col = p.getCol();

    //Begin checking which moves are possible, keeping in mind that only black draughts may calcStroke up
    //and only red draughts may calcStroke downwards

    boolean queen = p.isQueen();
    if (this.white) {
      possibleMovePair(Operator.SUBTRACTION, Operator.SUBTRACTION, row, col, white, white, queen,
          possibleMoves, jumpMoves, true);
      possibleMovePair(Operator.SUBTRACTION, Operator.ADDITION, row, col, white, white, queen,
          possibleMoves, jumpMoves, true);
      possibleMovePair(Operator.ADDITION, Operator.SUBTRACTION, row, col, !white, white, queen,
          possibleMoves, jumpMoves, true);
      possibleMovePair(Operator.ADDITION, Operator.ADDITION, row, col, !white, white, queen,
          possibleMoves, jumpMoves, true);
    } else {
      // top-left
      possibleMovePair(Operator.SUBTRACTION, Operator.SUBTRACTION, row, col, !white, white, queen,
          possibleMoves, jumpMoves, true);
      // top-right
      possibleMovePair(Operator.SUBTRACTION, Operator.ADDITION, row, col, !white, white, queen,
          possibleMoves, jumpMoves, true);
      // bottom-left
      possibleMovePair(Operator.ADDITION, Operator.SUBTRACTION, row, col, white, white, queen,
          possibleMoves, jumpMoves, true);
      // bottom-right
      possibleMovePair(Operator.ADDITION, Operator.ADDITION, row, col, white, white, queen,
          possibleMoves, jumpMoves, true);
    }

    if (p == clickedDraught) {
      if (!jumpMoves.isEmpty()) {
        for (Square currentSq : jumpMoves) {
          try {
            Square initSq = backgroundLayer.getSquare(clickedDraught.getRow(), clickedDraught.getCol());
            if (initSq.isOnLine(currentSq)) {
              highlightSquareToBeat(currentSq);
              highlightedSquares.add(currentSq);
            }
          } catch (SquareNotFoundException ignore) {
          }
        }
      } else if (!possibleMoves.isEmpty()) {
        for (Square square : possibleMoves) {
          fadeInSquare(square);
          highlightedSquares.add(square);
        }
      }
    }
  }

  private void fadeInSquare(Square square) {
    square.setAlpha(.5);
  }

  private void fadeOutSquare(Square square) {
    square.setAlpha(1.0);
  }

  private boolean isHighlightedSquare(Square square) {
    return .5 == square.getAlpha();
  }

  private void highlightSquareToBeat(Square square) {
    square.setAlpha(.4);
  }

  /**
   * Possible calcStroke in next and back direction
   *
   * @param sideWhite - цвет за который играет шашка
   * @return возвращает побита ли шашка дамкой
   */
  private void possibleMovePair(Operator opRow, Operator opCol, int row, int col, boolean white, boolean sideWhite,
                                boolean queen, List<Square> outPossibleMoves, List<Square> outJumpMoves,
                                boolean straightQueen) {
    if (queen) {
      QueenMoves moves = queenPossibleMoves(opRow, opCol, row, col, sideWhite, 0);
      if (!moves.possibleMoves.isEmpty()) {
        outPossibleMoves.addAll(moves.possibleMoves);
      }
      if (!moves.jumpMoves.isEmpty()) {
        outJumpMoves.addAll(moves.jumpMoves);
      }
      return;
    }

    if (inBounds(opRow.apply(row, 1), opCol.apply(col, 1)) && white) {
      Square square = null, nextSquare = null;
      try {
        square = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
        nextSquare = backgroundLayer.getSquare(opRow.apply(row, 2), opCol.apply(col, 2));
      } catch (SquareNotFoundException ignore) {
      }
      // Check moves to the op1, op2 of this Draught
      if (square != null && !square.isOccupied()) {
        outPossibleMoves.add(square);
      } else {
        //if square is occupied, and the color of the Draught in square is
        //not equal to the Draught whose moves we are checking, then
        //check to see if we can make the jump by checking
        //the next square in the same direction
        if (inBounds(opRow.apply(row, 2), opCol.apply(col, 2))) {
          if (nextSquare != null && !nextSquare.isOccupied()
              && square != null && square.getOccupant().isWhite() != sideWhite) {
            outJumpMoves.add(nextSquare);
            if (!capturedSquares.contains(square)) {
              capturedSquares.add(square);
            }
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
          && square.getOccupant().isWhite() != sideWhite) {
        outJumpMoves.add(nextSquare);
        if (!capturedSquares.contains(square)) {
          capturedSquares.add(square);
        }
      }
    }
  }

  private QueenMoves queenPossibleMoves(Operator opRow, Operator opCol, int row, int col, boolean sideWhite, int depth) {
    List<Square> possibleMoves = new ArrayList<>(rows * cols);
    List<Square> jumpMoves = new ArrayList<>(rows * cols);
    boolean foundAllMoves = false;
    while (!foundAllMoves) {
      Square sq = null;
      try {
        sq = backgroundLayer.getSquare(row, col);
        if (depth == 0) {
          possibleMoves.add(sq);
        }
      } catch (SquareNotFoundException e) {
      }
      if (!inBounds(opRow.apply(row, 1), opCol.apply(col, 1))) {
        break;
      }
      if (inBounds(opRow.apply(row, 2), opCol.apply(col, 2))) {
        Square square = null, nextSquare = null;
        try {
          square = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
          nextSquare = backgroundLayer.getSquare(opRow.apply(row, 2), opCol.apply(col, 2));
        } catch (SquareNotFoundException ignore) {
        }
        if (nextSquare != null && !nextSquare.isOccupied() && square != null && square.isOccupied()
            && square.getOccupant().isWhite() != sideWhite) {
          if (!capturedSquares.contains(square)) {
            capturedSquares.add(square);
            int r = opRow.apply(row, 2), c = opCol.apply(col, 2);
            while (inBounds(r, c)) {
              try {
                Square s = backgroundLayer.getSquare(opRow.apply(r, 0), opCol.apply(c, 0));
                if (s.isOccupied()) {
                  foundAllMoves = true;
                  break;
                }
                jumpMoves.add(s);
                if (depth == 1) {
                  foundAllMoves = true;
                  break;
                }
              } catch (SquareNotFoundException ignore) {
              }
              r = opRow.apply(r, 1);
              c = opCol.apply(c, 1);
            }
            break;
          }
        }
      }

      row = opRow.apply(row, 1);
      col = opCol.apply(col, 1);
    }

    depth++;
    if (!jumpMoves.isEmpty()) {
      List<Square> subMove = new ArrayList<>();
      for (Square move : jumpMoves) {
        if (opRow.equals(PossibleOperators.SUB) && opCol.equals(PossibleOperators.SUB)) { // top-left & bottom-right
          subMove.addAll(queenPossibleMoves(Operator.SUBTRACTION, Operator.ADDITION, move.getRow(), move.getCol(), sideWhite, depth).jumpMoves);
          subMove.addAll(queenPossibleMoves(Operator.ADDITION, Operator.SUBTRACTION, move.getRow(), move.getCol(), sideWhite, depth).jumpMoves);
        } else if (opRow.equals(PossibleOperators.SUB) && opCol.equals(PossibleOperators.ADD)) { // bottom-left & top-right
          subMove.addAll(queenPossibleMoves(Operator.SUBTRACTION, Operator.SUBTRACTION, move.getRow(), move.getCol(), sideWhite, depth).jumpMoves);
          subMove.addAll(queenPossibleMoves(Operator.ADDITION, Operator.ADDITION, move.getRow(), move.getCol(), sideWhite, depth).jumpMoves);
        } else if (opRow.equals(PossibleOperators.ADD) && opCol.equals(PossibleOperators.ADD)) { // top-left & bottom-right
          subMove.addAll(queenPossibleMoves(Operator.SUBTRACTION, Operator.ADDITION, move.getRow(), move.getCol(), sideWhite, depth).jumpMoves);
          subMove.addAll(queenPossibleMoves(Operator.ADDITION, Operator.SUBTRACTION, move.getRow(), move.getCol(), sideWhite, depth).jumpMoves);
        } else if (opRow.equals(PossibleOperators.ADD) && opCol.equals(PossibleOperators.SUB)) { // bottom-left & top-right
          subMove.addAll(queenPossibleMoves(Operator.SUBTRACTION, Operator.SUBTRACTION, move.getRow(), move.getCol(), sideWhite, depth).jumpMoves);
          subMove.addAll(queenPossibleMoves(Operator.ADDITION, Operator.ADDITION, move.getRow(), move.getCol(), sideWhite, depth).jumpMoves);
        }
      }
      if (!subMove.isEmpty()) {
        List<Square> resultMove = new ArrayList<>();
        for (Square move : jumpMoves) {
          for (Square square : subMove) {
            if (move.isOnLine(square) && !resultMove.contains(move)) {
              resultMove.add(move);
            }
          }
        }
        return new QueenMoves().setJumpMoves(resultMove);
      }
    }
    if (jumpMoves.isEmpty()) {
      return new QueenMoves().setPossibleMoves(possibleMoves);
    }
    return new QueenMoves().setJumpMoves(jumpMoves);
  }

  public void resetDesk() {
    backgroundLayer.resetDeskDrawing();
    highlightedSquares.clear();
  }

  public Square getSquare(double x, double y) throws SquareNotFoundException {
    return backgroundLayer.getSquare(x, y);
  }

  public Square getSquare(int row, int col) throws SquareNotFoundException {
    return backgroundLayer.getSquare(row, col);
  }

  private void possibleNextMovePair(Square takenSquare, Operator opRow, Operator opCol, int row, int col,
                                    boolean queen, List<Square> outJumpMoves) {
    if (inBounds(opRow.apply(row, 1), opCol.apply(col, 1))) {
      // Check moves to the op1, op2 of this Draught
      Square jumpSquare;
      try {
        jumpSquare = backgroundLayer.getSquare(opRow.apply(row, 2), opCol.apply(col, 2));
      } catch (SquareNotFoundException e) {
        return;
      }
      Square capturedSquare;
      try {
        capturedSquare = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
      } catch (SquareNotFoundException e) {
        return;
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
            return;
          }
          if (capturedSquare.isBetween(square, takenSquare)) {
            return;
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
              return;
            }
            try {
              jumpSquare = backgroundLayer.getSquare(opRow.apply(r, 2), opCol.apply(c, 2));
            } catch (SquareNotFoundException e) {
              return;
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
              return;
            }
            while (inBounds(i, j) && !squareStartLoop.isOccupied()) {
              final Square squareLoop;
              try {
                squareLoop = backgroundLayer.getSquare(i, j);
              } catch (SquareNotFoundException e) {
                continue;
              }
              outJumpMoves.add(squareLoop);
              i = opRow.apply(i, 1);
              j = opCol.apply(j, 1);
            }
            if (!capturedSquares.contains(capturedSquare)) {
              capturedSquares.add(capturedSquare);
            }
          }
        }
      } else {
        final Square square;
        try {
          square = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
        } catch (SquareNotFoundException e) {
          return;
        }
        if (square.isOccupied()) {
          // нельзя возвращяться назад
          if (takenSquare == jumpSquare) {
            return;
          }
          if (inBounds(opRow.apply(row, 2), opCol.apply(col, 2))) {
            if (!jumpSquare.isOccupied()
                && capturedSquare.getOccupant().isWhite() != white) {
              outJumpMoves.add(jumpSquare);
              if (!capturedSquares.contains(capturedSquare)) {
                capturedSquares.add(capturedSquare);
              }
            }
          }
        }
      }
    }
  }

  /**
   * получаем флаги передвижения и взятую шашку
   *
   * @param from The square from which we are moving
   * @param to   The square to which we are moving
   * @return возвращете передвижение с установленными флагами и взятой шашкой
   */
  public Stroke calcStroke(Square from, Square to) {
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
      List<Square> jumpMoves = new ArrayList<>();
      if (this.white) {
        possibleNextMovePair(from, Operator.SUBTRACTION, Operator.SUBTRACTION, row, col, queen,
            jumpMoves);
        possibleNextMovePair(from, Operator.SUBTRACTION, Operator.ADDITION, row, col, queen,
            jumpMoves);
        possibleNextMovePair(from, Operator.ADDITION, Operator.SUBTRACTION, row, col, queen,
            jumpMoves);
        possibleNextMovePair(from, Operator.ADDITION, Operator.ADDITION, row, col, queen,
            jumpMoves);
      } else {
        // top-left
        possibleNextMovePair(from, Operator.SUBTRACTION, Operator.SUBTRACTION, row, col, queen,
            jumpMoves);
        // top-right
        possibleNextMovePair(from, Operator.SUBTRACTION, Operator.ADDITION, row, col, queen,
            jumpMoves);
        // bottom-left
        possibleNextMovePair(from, Operator.ADDITION, Operator.SUBTRACTION, row, col, queen,
            jumpMoves);
        // bottom-right
        possibleNextMovePair(from, Operator.ADDITION, Operator.ADDITION, row, col, queen,
            jumpMoves);
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

  public void removeDraughtFrom(Square takenSquare) {
    removeDraughtFrom(takenSquare, false);
  }

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
    capturedSquares = new ArrayList<>();
  }

  public boolean inBounds(int row, int col) {
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

    doMove(stroke); // сделать ход
  }

  private void doMove(Stroke stroke) {
    doMove(stroke, -1, false);
  }

  public void doMove(Stroke stroke, int stepCursor) {
    doMove(stroke, stepCursor, false);
  }

  public void doMove(final Stroke stroke, final int stepCursor, final boolean back) {
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
  public void doMove(final Stroke stroke, final int stepCursor, final boolean back, final boolean cancel) {
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

  public void toggleTurn() {
    turn = !turn;
    getView().toggleTurn(turn);
  }

  public List<Draught> getMyDraughts() {
    return myDraughtList;
  }

  public List<Draught> getOpponentDraughts() {
    return opponentDraughtList;
  }

  public boolean isEmulate() {
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
  public void moveDraught(double clickX, double clickY) {
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

        final AnimationCallback animationCallback = new AnimationCallback();
        selectedDraught.animate(AnimationTweener.LINEAR, props, ANIMATION_DURATION, new IAnimationCallback() {
          @Override
          public void onStart(IAnimation animation, IAnimationHandle handle) {
          }

          @Override
          public void onFrame(IAnimation animation, IAnimationHandle handle) {
          }

          @Override
          public void onClose(IAnimation animation, IAnimationHandle handle) {
            final MoveDto move = MoveFactory.createMoveFromStroke(stroke)
                .setTitle(stroke.toNotation())
                .setHashTags(StringUtils.getHashes(getComment()));

//            if (isWhite() && getView().getPlayer().isSubscribed() || getView().getOpponent().isSubscribed()) {
//              move.setScreenshot(getView().takeScreenshot());
//            }
            backgroundLayer.resetDeskDrawing();

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

  public void strokeCanceled(Stroke stroke) {
    strokeCanceled(stroke, -1, false);
  }

  public void strokeCanceled(Stroke stroke, int stepCursor, boolean back) {
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

  public String getComment() {
    return "sdfsd #dsffsd, sdfdsf #123";
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

  public Set<DraughtDto> getInitialPosition() {
    return initialPos;
  }

  private static class QueenMoves {
    private List<Square> possibleMoves = new ArrayList<>();
    private List<Square> jumpMoves = new ArrayList<>();

    public QueenMoves() {
    }

    public List<Square> getPossibleMoves() {
      return possibleMoves;
    }

    public QueenMoves setPossibleMoves(List<Square> possibleMoves) {
      this.possibleMoves = possibleMoves;
      return this;
    }

    public List<Square> getJumpMoves() {
      return jumpMoves;
    }

    public QueenMoves setJumpMoves(List<Square> jumpMoves) {
      this.jumpMoves = jumpMoves;
      return this;
    }
  }
}
