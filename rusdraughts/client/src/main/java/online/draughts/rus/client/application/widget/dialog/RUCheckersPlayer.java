package online.draughts.rus.client.application.widget.dialog;//package net.rudraughts.social.draughts64.client.component.widget.dialog;
//
//import com.emitrom.lienzo.client.widget.LienzoPanel;
//import com.github.gwtbootstrap.client.ui.ButtonGroup;
//import com.github.gwtbootstrap.client.ui.messages.IconType;
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.event.dom.client.KeyCodes;
//import com.google.gwt.user.client.Element;
//import com.google.gwt.user.client.Event;
//import com.google.gwt.user.client.Timer;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.ui.*;
//import ru.checkers.RUCheckers.client.ClientFactory;
//import ru.checkers.RUCheckers.client.checkers.Board;
//import ru.checkers.RUCheckers.client.checkers.BoardBackgroundLayer;
//import ru.checkers.RUCheckers.client.ui.RUBase;
//import ru.checkers.RUCheckers.client.util.GameUtil;
//import ru.checkers.RUCheckers.online.draughts.rus.shared.locale.RUCheckersConstants;
//import ru.checkers.RUCheckers.online.draughts.rus.shared.model.Game;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created with IntelliJ IDEA.
// * User: alekspo
// * Date: 23.03.14
// * Time: 16:02
// */
//public class RUCheckersPlayer extends DialogBox {
//  private final int rows = 8;
//  private final int cols = 8;
//  private final HTMLPanel notationHTMLPanel;
//  private final List<String> notationList;
//  private final Board board;
//  private Button toStartButton = new Button("", IconType.FAST_BACKWARD);
//  private Button prevButton = new Button("", IconType.BACKWARD);
//  private Button playButton = new Button("", IconType.PLAY);
//  private Button nextButton = new Button("", IconType.FORWARD);
//  private Button toEndButton = new Button("", IconType.FAST_FORWARD);
//  private Button closeButton;
//  private RUCheckersConstants messages = GWT.create(RUCheckersConstants.class);
//  private int side = 0;
//  private int deskSide = 0;
//  private int notationCursor;
//  private int notationSubCursor;
//  private String NOTATION_ITEM = "notation-item-";
//  private boolean atStart = false;
//  private boolean firstNext = false;
//  private boolean firstPrev = false;
//  private Timer playTimer;
//  private boolean playing = false;
//  private boolean atEnd = false;
//  private HashMap<String, String> codeNameEndPlay;
//
//  public RUCheckersPlayer(ClientFactory clientFactory, Game game) {
//    GameUtil gameUtil = new GameUtil();
//    codeNameEndPlay = gameUtil.getEndPlayConstants();
//
//    setText("Партия: белыми - " + game.getUserWhiteName() + ", черными - " + game.getUserBlackName()
//        + ". Результат: " + codeNameEndPlay.getProvider(game.getPlayEndStatus()));
//    side = getSide() - 20;
//    deskSide = side - 20;
//    closeButton = new Button(messages.close());
//    VerticalPanel verticalPanel = new VerticalPanel();
//
//    BoardBackgroundLayer BOARD_BACKGROUND = new BoardBackgroundLayer(side, deskSide, rows, cols);
//    BOARD_BACKGROUND.drawCoordinates(true);
//
//    HorizontalPanel checkersPanel = new HorizontalPanel();
//
//    SimplePanel deskPanel = new SimplePanel();
//    LienzoPanel lienzoPanel = new LienzoPanel(side, side);
//    lienzoPanel.setBackgroundLayer(BOARD_BACKGROUND);
//
//    boolean myColor = true;
//    board = new Board(clientFactory, BOARD_BACKGROUND, rows, cols, myColor);
//    board.setEmulate(true);
//    lienzoPanel.add(board);
//
//    deskPanel.add(lienzoPanel);
//    checkersPanel.add(deskPanel);
//
//    VerticalPanel notationPanel = new VerticalPanel();
//
//    String partyNotation = game.getPartyNotation().trim();
//    notationList = Arrays.asList(partyNotation.split("\n"));
//    notationCursor = 0;
//    notationSubCursor = 1;
//
//    ScrollPanel notationScroll = new ScrollPanel();
//    notationHTMLPanel = new HTMLPanel("");
//    for (String n : notationList) {
//      HTML nHTML = new HTML(n + "<br />");
//      nHTML.getElement().setId(NOTATION_ITEM + notationCursor);
//      notationHTMLPanel.add(nHTML);
//      notationCursor += 1;
//    }
//    notationCursor = 0;
//    notationHTMLPanel.setWidth("160px");
//    notationHTMLPanel.setHeight(deskSide + "px");
//
//    notationScroll.add(notationHTMLPanel);
//
//    notationPanel.add(new Label(messages.notationTitle()));
//    notationPanel.add(notationScroll);
//    notationPanel.addStyleName("game-notation");
//    checkersPanel.add(notationPanel);
//
//    HorizontalPanel controlsPanel = new HorizontalPanel();
//    ButtonGroup playerControls = new ButtonGroup();
//    playerControls.add(toStartButton);
//    playerControls.add(prevButton);
//    playerControls.add(playButton);
//    playerControls.add(nextButton);
//    playerControls.add(toEndButton);
//
//    initControlButtons();
//
//    controlsPanel.add(playerControls);
//    controlsPanel.add(closeButton);
//    controlsPanel.setCellHorizontalAlignment(closeButton, HasAlignment.ALIGN_RIGHT);
//    controlsPanel.setWidth("100%");
//    controlsPanel.addStyleName("game-controls");
//
//    verticalPanel.add(checkersPanel);
//    verticalPanel.add(controlsPanel);
//
//    closeButton.addClickHandler(new ClickHandler() {
//      @Override
//      public void onClick(ClickEvent event) {
//        hide();
//      }
//    });
//
//    verticalPanel.addStyleName("game-window");
//    add(verticalPanel);
//    center();
//  }
//
//  public int getSide() {
//    if (0 == side) {
//      final int width = Window.getClientWidth();
//      final int height = Window.getClientHeight() - RUBase.getNavbar().getOffsetHeight()
//          - RUBase.getFooter().getOffsetHeight();
//      side = Math.min(width, height);
//    }
//    return side;
//  }
//
//  private void initControlButtons() {
//    prevButton.addClickHandler(new ClickHandler() {
//      @Override
//      public void onClick(ClickEvent event) {
//        if (atStart) {
//          return;
//        }
//        if (firstNext && 1 == notationSubCursor) {
//          notationCursor -= 1;
//        }
//        int oldNotationCursor = notationCursor;
//        firstNext = false;
//        firstPrev = true;
//        if (0 != notationCursor && 1 != notationSubCursor) {
//          notationCursor -= 1;
//        } else if (2 == notationSubCursor) {
//          atStart = true;
//          prevButton.setEnabled(false);
//          toStartButton.setEnabled(false);
//        }
//
//        Element notation = notationHTMLPanel.getElementById(NOTATION_ITEM + notationCursor);
//
//        if (null == notation) {
//          notationCursor -= 1;
//          return;
//        }
//
//        String[] subNotation = notation.getInnerText().split(" ");
//        String curStepNum = subNotation[0];
//        String curStepMiddle = subNotation[1];
//        String curStepEnd = subNotation[2];
//
//        if (atStart) {
//          notation.setInnerHTML(curStepNum + " " + curStepMiddle + " " + curStepEnd);
//
//          String step = subNotation[1];
//          board.doEmulatedMoveBack(step, notationCursor);
//          notationSubCursor = 1;
//          firstPrev = false;
//          return;
//        }
//
//        String step = subNotation[notationSubCursor];
//        String curStep = "<span style='background: yellow;'>" + step + "</span>";
//
//        if (notationSubCursor == 2) {
//          notation.setInnerHTML(curStepNum + " " + curStepMiddle + " " + curStep);
//        } else {
//          notation.setInnerHTML(curStepNum + " " + curStep + " " + curStepEnd);
//        }
//        if (notationSubCursor == 2) {
//          Element prevNotation = notationHTMLPanel.getElementById(NOTATION_ITEM + (notationCursor + 1));
//          String[] splitNotation = prevNotation.getInnerText().split(" ");
//          String endStep;
//          if (splitNotation.length > 2) {
//            endStep = splitNotation[2];
//          } else {
//            endStep = "";
//          }
//          prevNotation.setInnerHTML(splitNotation[0] + " " + splitNotation[1] + " " + endStep);
//        }
//
//        notationSubCursor -= 1;
//        if (notationSubCursor == 0) {
//          notationSubCursor = 2;
//        }
//
//        // перемещаем шашку на доске
//        Element notationPrev = notationHTMLPanel.getElementById(NOTATION_ITEM + oldNotationCursor);
//        if (null == notationPrev) {
//          notationCursor -= 1;
//          return;
//        }
//        subNotation = notationPrev.getInnerText().split(" ");
//        step = subNotation[notationSubCursor];
//        if (notationSubCursor == 1) {
//          board.doEmulatedMoveBack(step, notationCursor);
//        } else {
//          board.moveEmulatedPrevBlack(step, notationCursor);
//        }
//
//        atEnd = false;
//        playing = false;
//        if (!nextButton.isEnabled()) {
//          nextButton.setEnabled(true);
//        }
//        if (!toEndButton.isEnabled()) {
//          toEndButton.setEnabled(true);
//        }
//        if (!playButton.isEnabled()) {
//          playButton.setEnabled(true);
//        }
//      }
//    });
//    nextButton.addClickHandler(new ClickHandler() {
//      @Override
//      public void onClick(ClickEvent event) {
//        if (firstPrev && 1 == notationSubCursor) {
//          notationCursor += 1;
//        }
//        firstPrev = false;
//        atStart = false;
//        if (!prevButton.isEnabled()) {
//          prevButton.setEnabled(true);
//        }
//        if (!toStartButton.isEnabled()) {
//          toStartButton.setEnabled(true);
//        }
//
//        Element notation = notationHTMLPanel.getElementById(NOTATION_ITEM + notationCursor);
//        if (null == notation) {
//          atEnd = true;
//          playButton.setIcon(IconType.PLAY);
//          playButton.setEnabled(false);
//          nextButton.setEnabled(false);
//          toEndButton.setEnabled(false);
//          return;
//        }
//        String[] subNotation = notation.getInnerText().split(" ");
//        String curStepNum = subNotation[0];
//        String curStepMiddle = subNotation[1];
//        String curStepEnd;
//        if (subNotation.length > 2) {
//          curStepEnd = subNotation[2];
//        } else {
//          curStepEnd = "";
//        }
//        if (subNotation.length < notationSubCursor + 1) {
//          return;
//        }
//        String step = subNotation[notationSubCursor];
//        String curStep = "<span style='background: yellow;'>" + step + "</span>";
//        if (notationSubCursor == 2) {
//          notation.setInnerHTML(curStepNum + " " + curStepMiddle + " " + curStep);
//        } else {
//          notation.setInnerHTML(curStepNum + " " + curStep + " " + curStepEnd);
//        }
//
//        // перемещаем шашку на доске
//        if (notationSubCursor == 1) {
//          board.moveEmulatedNextWhite(step, notationCursor);
//        } else {
//          board.moveEmulatedNextBlack(step, notationCursor);
//        }
//
//        firstNext = true;
//        notationSubCursor += 1;
//        if (notationSubCursor == 3) {
//          notationSubCursor = 1;
//          notationCursor += 1;
//        } else if (notationSubCursor == 2 && notationCursor != 0) {
//          Element prevNotation = notationHTMLPanel.getElementById(NOTATION_ITEM + (notationCursor - 1));
//          String[] splitNotation = prevNotation.getInnerText().split(" ");
//          String endStep = splitNotation[2];
//          prevNotation.setInnerHTML(splitNotation[0] + " " + splitNotation[1] + " " + endStep);
//        }
//      }
//    });
//    toStartButton.addClickHandler(new ClickHandler() {
//      @Override
//      public void onClick(ClickEvent event) {
//        int playLength = notationCursor * 2 + 1;
//        for (int i = playLength; i > 0; i--) {
//          prevButton.click();
//        }
//      }
//    });
//    playButton.addClickHandler(new ClickHandler() {
//      @Override
//      public void onClick(ClickEvent event) {
//        if (playing) {
//          playTimer.cancel();
//          playing = false;
//          playButton.setIcon(IconType.PLAY);
//        } else {
//          playTimer = new Timer() {
//            @Override
//            public void run() {
//              nextButton.click();
//              playing = true;
//              if (atEnd) {
//                cancel();
//              }
//            }
//          };
//          playTimer.scheduleRepeating(1000);
//          playButton.setIcon(IconType.PAUSE);
//        }
//      }
//    });
//    toEndButton.addClickHandler(new ClickHandler() {
//      @Override
//      public void onClick(ClickEvent event) {
//        int playLength = notationList.size() * 2 - notationCursor + 1;
//        for (int i = notationCursor; i < playLength; i++) {
//          nextButton.click();
//        }
//      }
//    });
//
//    prevButton.setEnabled(false);
//    toStartButton.setEnabled(false);
//  }
//
//  @Override
//  protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
//    super.onPreviewNativeEvent(event);
//    if (event.getTypeInt() == Event.ONKEYDOWN) {
//      switch (event.getNativeEvent().getKeyCode()) {
//        case KeyCodes.KEY_ESCAPE:
//          hide();
//          break;
//        case KeyCodes.KEY_LEFT:
//          prevButton.click();
//          break;
//        case KeyCodes.KEY_RIGHT:
//          nextButton.click();
//          break;
//      }
//    }
//  }
//}
