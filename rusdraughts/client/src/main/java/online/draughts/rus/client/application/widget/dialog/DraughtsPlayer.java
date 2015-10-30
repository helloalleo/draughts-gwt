package online.draughts.rus.client.application.widget.dialog;

import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import online.draughts.rus.client.application.widget.NotationPanel;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.resources.Variables;
import online.draughts.rus.client.util.TrUtils;
import online.draughts.rus.draughts.Board;
import online.draughts.rus.draughts.BoardBackgroundLayer;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.model.Game;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 23.03.14
 * Time: 16:02
 */
public class DraughtsPlayer extends DialogBox {
  private final int rows = 8;
  private final int cols = 8;
  private final Game game;
  private final AppResources resource;
  private HTMLPanel notationHTMLPanel;
  private List<String> notationList;
  private Board board;
  private final EventBus eventBus;
  private Button toStartButton;
  private Button prevButton;
  private Button playButton;
  private Button nextButton;
  private Button toEndButton;
  private Button closeButton;
  private DraughtsMessages messages;
  private int side = 0;
  private int deskSide = 0;
  private int notationCursor;
  private String NOTATION_ITEM = "notation-item-";
  private boolean atStart = false;
  private boolean firstNext = false;
  private boolean firstPrev = false;
  private Timer playTimer;
  private boolean playing = false;
  private boolean atEnd = false;
  private HashMap<String, String> codeNameEndPlay;
  private VerticalPanel mainPanel;
  private long gameId;

  @Inject
  public DraughtsPlayer(DraughtsMessages messages, EventBus eventBus, AppResources resources, @Assisted Game game) {
    this.messages = messages;
    this.eventBus = eventBus;
    this.game = game;
    this.gameId = game.getId();
    this.resource = resources;

    getElement().addClassName(resources.style().dialogBox());
    setAnimationEnabled(true);

    HTML caption = new HTML(messages.playerCaption(game.getPlayerWhite().getPublicName(), game.getPlayerBlack().getPublicName(),
        TrUtils.translateEndGame(game.getPlayEndStatus())));
    caption.getElement().addClassName(resources.style().dialogCaptionPlayer());
    setHTML(new SafeHtmlBuilder().appendHtmlConstant(caption.getElement().getString()).toSafeHtml());

    side = getSide() - 20;
    setWidth(side + "px");
    deskSide = side - 80;
  }

  private void initButtons() {
    toStartButton = new Button("", IconType.FAST_BACKWARD, new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        int playLength = notationCursor * 2 + 1;
        for (int i = playLength; i > 0; i--) {
          moveBack();
        }
      }
    });
    prevButton = new Button("", IconType.BACKWARD, new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        moveBack();
      }
    });
    playButton = new Button("", IconType.PLAY, new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        if (playing) {
          playTimer.cancel();
          playing = false;
          playButton.setIcon(IconType.PLAY);
        } else {
          playTimer = new Timer() {
            @Override
            public void run() {
              moveForward();
              playing = true;
              if (atEnd) {
                cancel();
              }
            }
          };
          playTimer.scheduleRepeating(1000);
          playButton.setIcon(IconType.PAUSE);
        }
      }
    });
    nextButton = new Button("", IconType.FORWARD, new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        moveForward();
      }
    });
    toEndButton = new Button("", IconType.FAST_FORWARD, new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        int playLength = notationList.size() * 2 - notationCursor + 1;
        for (int i = notationCursor; i < playLength; i++) {
          nextButton.click();
        }
      }
    });

    prevButton.setEnabled(false);
    toStartButton.setEnabled(false);
  }

  private void moveBack() {
    if (atStart) {
      return;
    }
    if (firstNext) {
      notationCursor -= 1;
    }
    int oldNotationCursor = notationCursor;
    firstNext = false;
    firstPrev = true;
    if (0 != notationCursor) {
      notationCursor -= 1;
    } else {
      atStart = true;
      prevButton.setEnabled(false);
      toStartButton.setEnabled(false);
    }

    Element stroke = notationHTMLPanel.getElementById(game.getId() + ":" + notationCursor);

    if (null == stroke) {
      notationCursor -= 1;
      return;
    }

//        String[] subNotation = notation.getInnerText().split(" ");
//        String curStepNum = subNotation[0];
//        String curStepMiddle = subNotation[1];
//        String curStepEnd = subNotation[2];

    if (atStart) {
//          notation.setInnerHTML(curStepNum + " " + curStepMiddle + " " + curStepEnd);

//          String step = subNotation[1];
//          board.moveEmulatedPrevWhite(step, notationCursor);
      firstPrev = false;
      return;
    }

    String curStep = "<span style='background: yellow;'>" + stroke + "</span>";

//    if (notationSubCursor == 2) {
//          notation.setInnerHTML(curStepNum + " " + curStepMiddle + " " + curStep);
//    } else {
//          notation.setInnerHTML(curStepNum + " " + curStep + " " + curStepEnd);
//    }
//    if (notationSubCursor == 2) {
      Element prevNotation = notationHTMLPanel.getElementById(NOTATION_ITEM + (notationCursor + 1));
      String[] splitNotation = prevNotation.getInnerText().split(" ");
      String endStep;
      if (splitNotation.length > 2) {
        endStep = splitNotation[2];
      } else {
        endStep = "";
      }
      prevNotation.setInnerHTML(splitNotation[0] + " " + splitNotation[1] + " " + endStep);
//    }

//    notationSubCursor -= 1;
//    if (notationSubCursor == 0) {
//      notationSubCursor = 2;
//    }

    // перемещаем шашку на доске
//    Element notationPrev = notationHTMLPanel.getElementById(NOTATION_ITEM + oldNotationCursor);
//    if (null == notationPrev) {
//      notationCursor -= 1;
//      return;
//    }
//        subNotation = notationPrev.getInnerText().split(" ");
//        step = subNotation[notationSubCursor];
//    if (notationSubCursor == 1) {
//          board.moveEmulatedPrevWhite(step, notationCursor);
//    } else {
//          board.moveEmulatedPrevBlack(step, notationCursor);
//    }

//    atEnd = false;
//    playing = false;
//    if (!nextButton.isEnabled()) {
//      nextButton.setEnabled(true);
//    }
//    if (!toEndButton.isEnabled()) {
//      toEndButton.setEnabled(true);
//    }
//    if (!playButton.isEnabled()) {
//      playButton.setEnabled(true);
//    }
  }

  private void moveForward() {
    if (firstPrev) {
      notationCursor += 1;
    }
    firstPrev = false;
    atStart = false;
    if (!prevButton.isEnabled()) {
      prevButton.setEnabled(true);
    }
    if (!toStartButton.isEnabled()) {
      toStartButton.setEnabled(true);
    }

    Element notation = getStroke(notationCursor);
    if (null == notation) {
      atEnd = true;
      playButton.setIcon(IconType.PLAY);
      playButton.setEnabled(false);
      nextButton.setEnabled(false);
      toEndButton.setEnabled(false);
      return;
    }

//    String[] subNotation = notation.getInnerText().split(" ");
//    String curStepNum = subNotation[0];
//    String curStepMiddle = subNotation[1];
//    String curStepEnd;
//    if (subNotation.length > 2) {
//      curStepEnd = subNotation[2];
//    } else {
//      curStepEnd = "";
//    }
//    if (subNotation.length < notationSubCursor + 1) {
//      return;
//    }
//    String step = subNotation[notationSubCursor];
    final String step = notation.getInnerHTML();
    String highlightedStep = "<span id='current' data='" + notation.getId() + "' style='background: yellow;'>" + step + "</span>";
    notation.setInnerHTML(highlightedStep);
//    if (notationSubCursor == 2) {
//      notation.setInnerHTML(curStepNum + " " + curStepMiddle + " " + highlightedStep);
//    } else {
//      notation.setInnerHTML(curStepNum + " " + highlightedStep + " " + curStepEnd);
//    }

    // перемещаем шашку на доске
    if (0 == notationCursor % 2) {
      board.moveEmulatedNextBlack(step, notationCursor);
    } else {
      board.moveEmulatedNextWhite(step, notationCursor);
    }

    firstNext = true;
//    notationSubCursor += 1;
//    if (notationSubCursor == 3) {
//      notationSubCursor = 1;
      notationCursor += 1;
    Element wrapperNotation = getCurrent();
    if (!wrapperNotation.getAttribute("data").equals(notation.getId())) {
      Element current = getStrokeById(wrapperNotation.getAttribute("data"));
      current.setInnerHTML(wrapperNotation.getInnerHTML());
    }
  }

  private Element getStrokeById(String id) {
    return notationHTMLPanel.getElementById(id);
  }

  private Element getStroke(int notationCursor) {
    return notationHTMLPanel.getElementById(String.valueOf(notationCursor));
  }

  public int getSide() {
    if (0 == side) {
      final int width = Window.getClientWidth();
      final int height = Window.getClientHeight() - Variables.navbarTopHeight() * 3;
      side = Math.min(width, height);
    }
    return side;
  }

  @Override
  protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
    super.onPreviewNativeEvent(event);
    if (event.getTypeInt() == Event.ONKEYDOWN) {
      switch (event.getNativeEvent().getKeyCode()) {
        case KeyCodes.KEY_ESCAPE:
          hide();
          break;
        case KeyCodes.KEY_LEFT:
          prevButton.click();
          break;
        case KeyCodes.KEY_RIGHT:
          nextButton.click();
          break;
      }
    }
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    if (mainPanel != null) {
      remove(mainPanel);
    }
    mainPanel = createMainPanel();
    add(mainPanel);
  }

  @Override
  public void show() {
    Element elem = getElement();
    elem.getStyle().setPropertyPx("left", 0);
    elem.getStyle().setPropertyPx("top", 0);

    final int left = (Window.getClientWidth() - getOffsetWidth()) >> 2;
    final int top = ((Window.getClientHeight() - getOffsetHeight()) >> 2) - Variables.navbarTopHeight();
    setPopupPosition(Math.max(Window.getScrollLeft() + left, 0), Math.max(
        Window.getScrollTop() + top, 0));
    super.show();
  }

  private VerticalPanel createMainPanel() {
    closeButton = new Button(messages.ok());
    VerticalPanel verticalPanel = new VerticalPanel();

    BoardBackgroundLayer boardBackground = new BoardBackgroundLayer(side, deskSide, rows, cols);
    boardBackground.drawCoordinates(true);

    HorizontalPanel checkersPanel = new HorizontalPanel();

    SimplePanel deskPanel = new SimplePanel();
    LienzoPanel lienzoPanel = new LienzoPanel(side, side);
    lienzoPanel.setBackgroundLayer(boardBackground);

    board = new Board(eventBus, boardBackground, rows, cols, true);
    board.setEmulate(true);
    lienzoPanel.add(board);

    deskPanel.add(lienzoPanel);
    checkersPanel.add(deskPanel);

    VerticalPanel notationPanel = new VerticalPanel();

    String partyNotation = game.getNotation().trim();
    notationList = Arrays.asList(partyNotation.split(NotationPanel.NOTATION_SEP));
    notationCursor = 1;
//    notationSubCursor = 1;

    ScrollPanel notationScroll = new ScrollPanel();
    notationHTMLPanel = new HTMLPanel("");
    notationHTMLPanel.add(new HTML(game.getNotation()));
    notationHTMLPanel.setWidth("160px");
    notationHTMLPanel.setHeight(side + "px");

    notationScroll.add(notationHTMLPanel);

    notationPanel.add(new Label(messages.notationTitle()));
    notationPanel.add(notationScroll);
    notationPanel.addStyleName("player-notation");
    checkersPanel.add(notationPanel);

    initButtons();

    HorizontalPanel controlsPanel = new HorizontalPanel();
    ButtonGroup playerControls = new ButtonGroup();
    playerControls.add(toStartButton);
    playerControls.add(prevButton);
    playerControls.add(playButton);
    playerControls.add(nextButton);
    playerControls.add(toEndButton);


    controlsPanel.add(playerControls);
    controlsPanel.add(closeButton);
    controlsPanel.setCellHorizontalAlignment(closeButton, HasAlignment.ALIGN_RIGHT);
    controlsPanel.setWidth("100%");
    controlsPanel.addStyleName("player-controls");

    verticalPanel.add(checkersPanel);
    verticalPanel.add(controlsPanel);

    closeButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        hide();
      }
    });

    verticalPanel.addStyleName("player-window");
    return verticalPanel;
  }

  public Element getCurrent() {
    return notationHTMLPanel.getElementById("current");
  }
}
