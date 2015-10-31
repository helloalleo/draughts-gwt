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
import online.draughts.rus.draughts.Stroke;
import online.draughts.rus.draughts.StrokeFactory;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.model.Game;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.constants.IconType;

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
  private HTMLPanel notationHTMLPanel;
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
  private boolean atStart = false;
  private Timer playTimer;
  private boolean playing = false;
  private boolean atEnd = false;
  private VerticalPanel mainPanel;
  private long gameId;

  @Inject
  public DraughtsPlayer(DraughtsMessages messages, EventBus eventBus, AppResources resources, @Assisted Game game) {
    this.messages = messages;
    this.eventBus = eventBus;
    this.game = game;
    this.gameId = game.getId();

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
        toStart();
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
          notationCursor = 0;
          playTimer = null;
          playButton.setIcon(IconType.PLAY);
        } else {
          moveForward();
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
        toEnd();
      }
    });

    prevButton.setEnabled(false);
    toStartButton.setEnabled(false);

    closeButton = new Button(messages.ok());
    closeButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        hide();
      }
    });
  }

  private void moveBack() {
    if (atEnd) {
      atEnd = false;
      playButton.setEnabled(true);
      nextButton.setEnabled(true);
      toEndButton.setEnabled(true);
    }

    // получаем текущий ход
    Element currentNotation = getCurrent();
    if (currentNotation == null) {
      return;
    }
    notationCursor--;

    Element outerNotation = getStrokeById(currentNotation.getAttribute(NotationPanel.DATA_ID_ATTR));
    outerNotation.setInnerHTML(currentNotation.getInnerHTML());

    String step = null;
    if (Boolean.valueOf(outerNotation.getAttribute(NotationPanel.CONTINUE_BEAT))
        || Boolean.valueOf(outerNotation.getAttribute(NotationPanel.STOP_BEAT))) {
      step = getStroke(notationCursor - 1).getInnerHTML();
    }

    Stroke stroke = StrokeFactory.createStrokeFromNotationHtml(outerNotation, step, true);
    board.doEmulatedMoveBack(stroke, notationCursor);

    Element notation = getStroke(notationCursor - 1);
    if (null == notation) {
      atStart = true;
      prevButton.setEnabled(false);
      toStartButton.setEnabled(false);
      playButton.setEnabled(true);
      nextButton.setEnabled(true);
      toEndButton.setEnabled(true);
      return;
    }

    String highlightedStep = "<span id='current' data-id='" + notation.getId() + "' style='background: yellow;'>" + notation.getInnerHTML() + "</span>";
    notation.setInnerHTML(highlightedStep);
  }

  private void moveForward() {
    if (!atEnd) {
      if (prevButton.isEnabled()) {
        prevButton.setEnabled(false);
      }
      if (toStartButton.isEnabled()) {
        toStartButton.setEnabled(false);
      }
    }

    Element notation = getStroke(notationCursor);
    if (null == notation) {
      return;
    }

    if (!prevButton.isEnabled()) {
      prevButton.setEnabled(true);
    }
    if (!toStartButton.isEnabled()) {
      toStartButton.setEnabled(true);
    }

    Element wrapperNotation = getCurrent();
    String prevStep = "";
    if (wrapperNotation != null) {
      prevStep = wrapperNotation.getInnerHTML();
      Element outerNotation = getStrokeById(wrapperNotation.getAttribute("data-id"));
      outerNotation.setInnerHTML(wrapperNotation.getInnerHTML());
    }

    Stroke stroke = StrokeFactory.createStrokeFromNotationHtml(notation, prevStep, false);
    board.doMove(stroke, notationCursor);

    String highlightedStep = "<span id='current' " + NotationPanel.DATA_ID_ATTR + "='" + notation.getId() + "' style='background: yellow;'>" + notation.getInnerHTML() + "</span>";
    notation.setInnerHTML(highlightedStep);

    notationCursor++;

    notation = getStroke(notationCursor);
    if (null == notation) {
      atEnd = true;
      playing = false;
      playButton.setIcon(IconType.PLAY);
      playButton.setEnabled(false);
      nextButton.setEnabled(false);
      toEndButton.setEnabled(false);
      prevButton.setEnabled(true);
      toStartButton.setEnabled(true);
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
          moveBack();
          break;
        case KeyCodes.KEY_RIGHT:
          moveForward();
          break;
        case KeyCodes.KEY_UP:
          toStart();
          break;
        case KeyCodes.KEY_DOWN:
          toEnd();
          break;
      }
    }
  }

  private void toEnd() {
    while (!atEnd) {
      moveForward();
    }
  }

  private void toStart() {
    for (int i = notationCursor + 1; i > 0; i--) {
      moveBack();
    }
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    initDialog();
    mainPanel = createMainPanel();
    add(mainPanel);
  }

  private void initDialog() {
    notationCursor = 0;
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    remove(mainPanel);
    mainPanel = null;
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

    verticalPanel.addStyleName("player-window");
    return verticalPanel;
  }

  public Element getCurrent() {
    return notationHTMLPanel.getElementById("current");
  }
}
