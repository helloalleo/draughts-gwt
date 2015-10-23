package online.shashki.rus.shashki;

import com.ait.lienzo.client.core.animation.*;
import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.event.NodeTouchEndEvent;
import com.ait.lienzo.client.core.event.NodeTouchEndHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import online.shashki.rus.client.event.*;
import online.shashki.rus.client.util.SHLog;
import online.shashki.rus.shared.model.Move;
import online.shashki.rus.shared.util.StringUtils;
import online.shashki.rus.shashki.util.Operator;
import online.shashki.rus.shashki.util.PossibleOperators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * Profile: alekspo
 * Date: 08.12.13
 * Time: 13:26
 */
public class Board extends Layer {
  private List<Square> capturedSquares = new ArrayList<>();
  private List<Draught> myDraughtList;
  private List<Draught> opponentDraughtList;

  private boolean white;
  private boolean turn;
  private int rows;
  private int cols;
  private final double removeDraughtFade = 200;
  private final double moveDraughtDuration = 200;
  private boolean emulate = false; // эмулировать шашки
  private HashMap<String, Integer> alphMap;
  // стек ходов шашек, когда они становятся дамками
  private Stack<Integer> queenStepStack = new Stack<>();

  //  private ShashkiGinjector shashkiGinjector = ShashkiGinjector.INSTANCE;
  private List<Square> highlightedSquares = new ArrayList<>();
  //  private String lastEndMove;
//  private String lastStartMove;
//  private String lastCaptured;
  private HandlerRegistration playMoveOpponentHR;

  private Stack<Stroke> moveMyStack = new Stack<>();
  private Stack<Stroke> moveOpponentStack = new Stack<>();
  private int moveCounter = 0;
  private boolean complexBeat = false;

  private BoardBackgroundLayer backgroundLayer;

  private EventBus eventBus;

  public Board(EventBus playEventBus,
               BoardBackgroundLayer backgroundLayer,
               int rows,
               int cols,
               boolean white) {
    this.eventBus = playEventBus;
    this.backgroundLayer = backgroundLayer;
    this.white = white;
    turn = white;

    this.rows = rows;
    this.cols = cols;

    myDraughtList = new ArrayList<>(rows / 2 * 3);
    opponentDraughtList = new ArrayList<>(rows / 2 * 3);

    placeDraughts();

    alphMap = new HashMap<>();
    alphMap.put("a", 0);
    alphMap.put("b", 1);
    alphMap.put("c", 2);
    alphMap.put("d", 3);
    alphMap.put("e", 4);
    alphMap.put("f", 5);
    alphMap.put("g", 6);
    alphMap.put("h", 7);

    bindEvents();
  }

  private void bindEvents() {
    eventBus.addHandler(PlayMoveCancelEvent.TYPE, new PlayMoveCancelEventHandler() {
      @Override
      public void onPlayMove(PlayMoveCancelEvent event) {
        final Move move = event.getMove();
        final Stroke stroke = StrokeFactory.createStrokeFromMove(move);
        final Stroke mirror = stroke.mirror();
        eventBus.fireEvent(new NotationCancelStrokeEvent(mirror));
        moveMyCanceled(stroke);
      }
    });

    eventBus.addHandler(PlayMoveOpponentCancelEvent.TYPE, new PlayMoveOpponentCancelEventHandler() {
      @Override
      public void onPlayMoveOpponentCancel(PlayMoveOpponentCancelEvent event) {
        final Move move = event.getMove();
        final Stroke stroke = StrokeFactory.createStrokeFromMove(move);
        final Stroke mirror = stroke.mirror();
        eventBus.fireEvent(new NotationCancelStrokeEvent(mirror));
        moveOpponentCanceled(stroke);
      }
    });

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

    playMoveOpponentHR = eventBus.addHandler(PlayMoveOpponentEvent.TYPE, new PlayMoveOpponentEventHandler() {
      @Override
      public void onPlayMoveOpponent(PlayMoveOpponentEvent event) {
        final Move move = event.getMove();
        final Stroke stroke = StrokeFactory.createStrokeFromMove(move);
        SHLog.debug("PLAY MOVE STROKE " + stroke);
        final Stroke mirror = stroke.mirror();
        moveOpponent(mirror);
      }
    });

    eventBus.addHandler(RemovePlayMoveOpponentHandlerEvent.TYPE, new RemoveWebsocketHandlersEventHandler() {
      @Override
      public void onRemovePlayMoveOpponentHandler(RemovePlayMoveOpponentHandlerEvent event) {
        playMoveOpponentHR.removeHandler();
      }
    });
  }

  private void placeDraughts() {
    for (int row = 0; row < 3; row++) {
//    for(int row = 3; row < 4; row++) {
      for (int col = 0; col < 8; col++) {
//      for (int col = 2; col < 3; col++) {
        if (Square.isValid(row, col)) {
          opponentDraughtList.add(addDraught(row, col, !white));
        }
      }
    }

    // Now establish the Black side
    for (int row = 5; row < 8; row++) {
//    for(int row = 4; row < 5; row++) {
      for (int col = 0; col < 8; col++) {
//      for (int col = 5; col < 6; col++) {
        if (Square.isValid(row, col)) {
          myDraughtList.add(addDraught(row, col, white));
        }
      }
    }

/*
    if (isWhite()) {
      opponentDraughtList.add(addDraught(3, 4, !white));
    } else {
      myDraughtList.add(addDraught(4, 3, white));
    }

    if (isWhite()) {
      myDraughtList.add(addDraught(4, 3, white));
    } else {
      opponentDraughtList.add(addDraught(3, 4, !white));
    }
*/
  }

  private Draught addDraught(int row, int col, boolean white) {
    try {
      Square square = backgroundLayer.getSquare(row, col);
      if (Square.isValid(row, col)) {
        Draught draught = new Draught(
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
   * @param clickedPiece
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
   * @return A List of Squares to which this Draught can calcStroke
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

    //Begin checking which moves are possible, keeping in mind that only black shashki may calcStroke up
    //and only red shashki may calcStroke downwards

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
   * @param sideWhite - цвет текущей шашки
   */
  private void possibleMovePair(Operator opRow, Operator opCol, int row, int col, boolean white, boolean sideWhite,
                                boolean queen, List<Square> outPossibleMoves, List<Square> outJumpMoves,
                                boolean straightQueen) {
    if (inBounds(opRow.apply(row, 1), opCol.apply(col, 1)) && (white || queen)) {
      Square square = null, nextSquare = null;
      try {
        square = backgroundLayer.getSquare(opRow.apply(row, 1), opCol.apply(col, 1));
        nextSquare = backgroundLayer.getSquare(opRow.apply(row, 2), opCol.apply(col, 2));
      } catch (SquareNotFoundException ignore) {
//        SHLog.debug(e.getLocalizedMessage(), e);
      }
      // Check moves to the op1, op2 of this Draught
      if (square != null && !square.isOccupied()) {
        outPossibleMoves.add(square);
        if (queen) {
          possibleMovePair(opRow, opCol, opRow.apply(row, 1), opCol.apply(col, 1), white, sideWhite,
              true, outPossibleMoves, outJumpMoves, straightQueen);
        }
      } else {
        //if square is occupied, and the color of the Draught in square is
        //not equal to the Draught whose moves we are checking, then
        //check to see if we can make the jump by checking
        //the next square in the same direction
        if (queen) {
          if (inBounds(opRow.apply(row, 2), opCol.apply(col, 2))) {
            if (nextSquare != null && !nextSquare.isOccupied()
                && square != null && square.getOccupant().isWhite() != sideWhite) {
              int i = opRow.apply(row, 2);
              int j = opCol.apply(col, 2);
              List<Square> jumps = new ArrayList<>();
              List<Square> oneJumps = new ArrayList<>();
              Square startSquareLoop;
              try {
                startSquareLoop = backgroundLayer.getSquare(i, j);
              } catch (SquareNotFoundException e) {
                return;
              }
              while (inBounds(i, j) && !startSquareLoop.isOccupied()) {
                final Square loopSquare;
                try {
                  loopSquare = backgroundLayer.getSquare(i, j);
                } catch (SquareNotFoundException e) {
                  continue;
                }
                if (straightQueen) {
                  if (opRow.equals(PossibleOperators.SUB) && opCol.equals(PossibleOperators.SUB)
                      || opRow.equals(PossibleOperators.ADD) && opCol.equals(PossibleOperators.ADD)) { // top-left & bottom-right
                    possibleMovePair(Operator.ADDITION, Operator.SUBTRACTION, i, j, white, sideWhite, true,
                        outPossibleMoves, outJumpMoves, false);
                    possibleMovePair(Operator.SUBTRACTION, Operator.ADDITION, i, j, white, sideWhite, true,
                        outPossibleMoves, outJumpMoves, false);
                  } else if (opRow.equals(PossibleOperators.SUB) && opCol.equals(PossibleOperators.ADD)
                      || opRow.equals(PossibleOperators.ADD) && opCol.equals(PossibleOperators.SUB)) { // bottom-left & top-right
                    possibleMovePair(Operator.SUBTRACTION, Operator.SUBTRACTION, i, j, white, sideWhite, true,
                        outPossibleMoves, outJumpMoves, false);
                    possibleMovePair(Operator.ADDITION, Operator.ADDITION, i, j, white, sideWhite, true,
                        outPossibleMoves, outJumpMoves, false);
                  }
                  if (!outJumpMoves.isEmpty()) {
                    jumps.add(loopSquare);
                    outJumpMoves.clear();
                  } else if (jumps.isEmpty()) {
                    oneJumps.add(loopSquare);
                  }
                } else {
                  outJumpMoves.add(loopSquare);
                }
                i = opRow.apply(i, 1);
                j = opCol.apply(j, 1);
              }
              if (!jumps.isEmpty()) {
                outJumpMoves.addAll(jumps);
              } else if (!oneJumps.isEmpty()) {
                outJumpMoves.addAll(oneJumps);
              }
              if (!capturedSquares.contains(square)) {
                capturedSquares.add(square);
              }
            }
          }
        } else {
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
    }

    if (!queen) {
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
  }

  public void resetDesk() {
    backgroundLayer.resetDeskDrawing();
    highlightedSquares.clear();
  }

  public Square getSquare(double row, double col) throws SquareNotFoundException {
    return backgroundLayer.getSquare(row, col);
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
      if (!queen && row == 0) {
        queen = true;
      }
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
      if (jumpMoves.isEmpty()) {
        stroke.setOnStopBeat();

        if (!complexBeat) {
          stroke.setOnStartBeat();
        } else {
          complexBeat = false;
        }
      } else {
        stroke.setOnContinueBeat();
        if (!complexBeat) {
          stroke.setOnStartBeat();
        }
        complexBeat = true;
      }

      SHLog.debug(takenSquare.toString());
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
              eventBus.fireEvent(new CheckWinnerEvent());
            }
          }
        });
    capturedSquares = new ArrayList<>();
  }

  public boolean inBounds(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }

  private Square parseStep(String move) throws SquareNotFoundException {
    int sRow = 8 - Integer.parseInt(move.substring(1, 2));
    int sCol = alphMap.get(move.substring(0, 1));

    return backgroundLayer.getSquare(sRow, sCol);
  }

  private Square findCaptured(Square firstStep, Square secondStep) {
    Square captured = null;
    OUTERLOOP:
    for (int n = 0; n < rows; n++) {
      for (int m = 0; m < cols; m++) {
        Square current;
        try {
          current = backgroundLayer.getSquare(n, m);
        } catch (SquareNotFoundException e) {
          continue;
        }
        if (null != current && null != current.getOccupant() && current.isBetween(firstStep, secondStep)
            && current.isOnLine(firstStep)) {
          captured = current;
          break OUTERLOOP;
        }
      }
    }
    return captured;
  }

//  public void moveEmulatedNextWhite(String calcStroke, int stepCursor) {
//    if (calcStroke.contains(ANNOTATION_SIMPLE_MOVE)) {
//      String[] steps = calcStroke.split(ANNOTATION_SIMPLE_MOVE);
//      Square startSquare, endSquare;
//      try {
//        startSquare = parseStep(steps[0]);
//        endSquare = parseStep(steps[1]);
//      } catch (SquareNotFoundException e) {
//        SHLog.debug(e.getLocalizedMessage(), e);
//        return;
//      }
//      calcStroke(startSquare, endSquare, null, false, stepCursor);
//    } else if (calcStroke.contains(ANNOTATION_BEAT_MOVE)) {
//      String[] steps = calcStroke.split(ANNOTATION_BEAT_MOVE);
//      for (int i = 0; i < steps.length - 1; i++) {
//        Square firstStep, secondStep;
//        try {
//          firstStep = parseStep(steps[i]);
//          secondStep = parseStep(steps[i + 1]);
//        } catch (SquareNotFoundException e) {
//          SHLog.debug(e.getLocalizedMessage(), e);
//          continue;
//        }
//        Square captured = findCaptured(firstStep, secondStep);
//        if (null == captured) {
//          return;
//        }
//        capturedStack.push(captured);
//        calcStroke(firstStep, secondStep, captured, false, stepCursor);
//      }
//    }
//  }

//  public void moveEmulatedNextBlack(String calcStroke, int stepCursor) {
//    if (calcStroke.contains(ANNOTATION_SIMPLE_MOVE)) {
//      String[] steps = calcStroke.split(ANNOTATION_SIMPLE_MOVE);
//      Square startSquare, endSquare;
//      try {
//        startSquare = parseStep(steps[0]);
//        endSquare = parseStep(steps[1]);
//      } catch (SquareNotFoundException e) {
//        SHLog.debug(e.getLocalizedMessage(), e);
//        return;
//      }
//      calcStroke(startSquare, endSquare, null, false, stepCursor);
//    } else if (calcStroke.contains(ANNOTATION_BEAT_MOVE)) {
//      String[] steps = calcStroke.split(ANNOTATION_BEAT_MOVE);
//      for (int i = 0; i < steps.length - 1; i++) {
//        Square firstStep, secondStep;
//        try {
//          firstStep = parseStep(steps[i]);
//          secondStep = parseStep(steps[i + 1]);
//        } catch (SquareNotFoundException e) {
//          SHLog.debug(e.getLocalizedMessage(), e);
//          continue;
//        }
//        Square captured = findCaptured(firstStep, secondStep);
//        if (null == captured) {
//          return;
//        }
//        capturedStack.push(captured);
//        calcStroke(firstStep, secondStep, captured, false, stepCursor);
//      }
//    }
//  }

//  public void moveEmulatedPrevWhite(String calcStroke, int stepCursor) {
//    if (calcStroke.contains(ANNOTATION_SIMPLE_MOVE)) {
//      String[] steps = calcStroke.split(ANNOTATION_SIMPLE_MOVE);
//      Square startSquare = parseStep(steps[1]);
//      Square endSquare = parseStep(steps[0]);
//      calcStroke(startSquare, endSquare, null, false, stepCursor);
//    } else if (calcStroke.contains(ANNOTATION_BEAT_MOVE)) {
//      String[] steps = calcStroke.split(ANNOTATION_BEAT_MOVE);
//      for (int i = steps.length - 1; i > 0; i--) {
//        Square firstStep = parseStep(steps[i]);
//        Square secondStep = parseStep(steps[i - 1]);
////        Square captured = findCaptured(firstStep, secondStep);
//        Square captured = capturedStack.pop();
//        if (null == captured) {
//          return;
//        } else {
//          myDraughtList.add(addDraught(captured.getRow(), captured.getCol(), !white));
//        }
//        calcStroke(firstStep, secondStep, null, false, stepCursor);
//      }
//    }
//  }

//  public void moveEmulatedPrevBlack(String calcStroke, int stepCursor) {
//    if (calcStroke.contains(ANNOTATION_SIMPLE_MOVE)) {
//      String[] steps = calcStroke.split(ANNOTATION_SIMPLE_MOVE);
//      Square startSquare = parseStep(steps[1]);
//      Square endSquare = parseStep(steps[0]);
//      calcStroke(startSquare, endSquare, null, false, stepCursor);
//    } else if (calcStroke.contains(ANNOTATION_BEAT_MOVE)) {
//      String[] steps = calcStroke.split(ANNOTATION_BEAT_MOVE);
//      for (int i = steps.length - 1; i > 0; i--) {
//        Square firstStep = parseStep(steps[i]);
//        Square secondStep = parseStep(steps[i - 1]);
////        Square captured = findCaptured(firstStep, secondStep);
//        Square captured = capturedStack.pop();
//        if (null == captured) {
//          return;
//        } else {
//          myDraughtList.add(addDraught(captured.getRow(), captured.getCol(), white));
//        }
//        calcStroke(firstStep, secondStep, null, false, stepCursor);
//      }
//    }
//  }

  public void moveOpponent(Stroke stroke) {
    SHLog.debug("MOVE OPPONENT " + stroke.toString());

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
    } else {
      stroke.setStartSquare(startSquare);
      stroke.setEndSquare(endSquare);
      moveOpponentStack.push(stroke);
    }

    stroke.setTakenSquare(takenSquare);

    if (stroke.isContinueBeat()) {
//      stroke.mirror();
      SHLog.debug("MOVE CONT BEAT " + stroke.toString());
    }

    if (!stroke.isCancel()) {
      final boolean first = isFirstMoveFlag();
      stroke.setNumber(stroke.getNumber())
          .setFirst(first);
      eventBus.fireEvent(new NotationStrokeEvent(stroke, isWhite()));
    }

    doMove(stroke); // сделать ход
  }

  private void doMove(Stroke stroke) {
    doMove(stroke, -1);
  }

  /**
   * Функция, которая выполняет физическое перемещение шашек
   *
   * @param stroke     ход
   * @param stepCursor для дамок
   */
  private void doMove(Stroke stroke, final int stepCursor) {
    SHLog.debug("DO MOVE " + stroke.toString());
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
          } else if ((rows - 1) == endSquare.getRow() && occupant.isWhite() != white) {
            occupant.setPosition(endSquare.getRow(), endSquare.getCol());
            occupant.setQueen(true);
          }
        }
      }
    });

    endSquare.setOccupant(occupant);
    occupant.setPosition(endSquare.getRow(), endSquare.getCol());

    if (taken != null && !stroke.isCancel()) {
      removeDraughtFrom(taken);
    } else if (taken != null) {
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
      eventBus.fireEvent(new CheckWinnerEvent());
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
    eventBus.fireEvent(new TurnChangeEvent(turn));
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
   * @param clickX
   * @param clickY
   */
  public void moveDraught(double clickX, double clickY) {
    Draught selectedDraught = Draught.getSelectedDraught();
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
        Stroke stroke = calcStroke(startSquare, endSquare);

        boolean isSimpleMove = stroke.isSimple();
        SHLog.debug("SIMPLE MOVE " + isSimpleMove);
        if (isSimpleMove || stroke.isStopBeat()) {
          toggleTurn();
        }
        if (!selectedDraught.isQueen()) {
          if (selectedDraught.getRow() == 0) {
            selectedDraught.setQueen(true);
          }
        }

        SHLog.debug("END SQUARE " + endSquare);
        eventBus.fireEvent(new NotationStrokeEvent(stroke, isWhite()));

        final Move move = MoveFactory.createMoveFromStroke(stroke)
            .setTitle(stroke.toNotation(isWhite()))
            .setHashTags(StringUtils.getHashes(getComment()));

        eventBus.fireEvent(new PlayMoveMessageEvent(move));
        moveMyStack.push(stroke);
        SHLog.debug("MOVE DRAUGHT " + stroke.toString());

        AnimationProperties props = new AnimationProperties();
        props.push(AnimationProperty.Properties.X(endSquare.getCenterX()));
        props.push(AnimationProperty.Properties.Y(endSquare.getCenterY()));

        selectedDraught.animate(AnimationTweener.LINEAR, props, 100);

        props = new AnimationProperties();
        props.push(AnimationProperty.Properties.SCALE(1.0));

        selectedDraught.animate(AnimationTweener.LINEAR, props, 100);

        backgroundLayer.resetDeskDrawing();
      }
    }
  }

  /**
   * Вычисляем является ли ход первым в полном ходе a1 b2 - а1 первый ход
   *
   * @return
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
    stroke = stroke.mirror();

    SHLog.debug("MOVE CANCELED " + stroke.toString());
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

    SHLog.debug("MOVE CANCELED REVERSED " + stroke.toString());

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

    SHLog.debug("MOVE CANCELED 1");
    doMove(stroke);
    SHLog.debug("MOVE CANCELED 2");
  }

  private void moveOpponentCanceled(Stroke stroke) {
    SHLog.debug("OPPONENT CANCELED");
    strokeCanceled(stroke);
    Stroke canceled = moveOpponentStack.pop();
    if (canceled.isFirst()) {
      moveCounter--;
    }
  }

  private void moveMyCanceled(Stroke stroke) {
    SHLog.debug("MY CANCELED");
    strokeCanceled(stroke);
    Stroke canceled = moveMyStack.pop();
    if (canceled.isFirst()) {
      moveCounter--;
    }
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
}
