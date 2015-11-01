package online.draughts.rus.client.application.widget.popup;

import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;
import com.gwtplatform.mvp.client.view.PopupPositioner;
import online.draughts.rus.client.application.widget.NotationPanel;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.resources.Variables;
import online.draughts.rus.client.util.TrUtils;
import online.draughts.rus.draughts.Board;
import online.draughts.rus.draughts.BoardBackgroundLayer;
import online.draughts.rus.draughts.Stroke;
import online.draughts.rus.draughts.StrokeFactory;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.model.Game;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Br;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.ArrayList;
import java.util.List;

import static online.draughts.rus.client.util.Utils.format;

public class DraughtsPlayerView extends PopupViewWithUiHandlers<DraughtsPlayerUiHandlers>
    implements DraughtsPlayerPresenter.MyView {
  private final EventBus eventBus;
  private final AppResources resources;
  private final int rows = 8;
  private final int cols = 8;
  private final Game game;
  private final ClientConfiguration config;
  @UiField
  PopupPanel main;
  @UiField
  HTML caption;
  @UiField
  HTMLPanel body;
  @UiField
  Button toStartButton;
  @UiField
  Button prevButton;
  @UiField
  Button playButton;
  @UiField
  Button nextButton;
  @UiField
  Button toEndButton;
  @UiField
  Button dismiss;
  @UiField
  SimplePanel deskPanel;
  @UiField
  HTMLPanel notationPanel;
  @UiField
  HTMLPanel beatenDraughts;
  private Board board;
  private DraughtsMessages messages;
  private int side = 0;
  private int deskSide = 0;
  private int notationCursor;
  private int prevPos = -1;
  private Timer playTimer;
  private boolean playing = false;
  private boolean atEnd = false;
  private VerticalPanel mainPanel;
  @UiField
  Label beatenMineDraughtsLabel;
  @UiField
  Label beatenOpponentDraughtsLabel;
  @UiField
  TextArea commentCurrentStrokeTextArea;
  @UiField
  Button commentCurrentStrokeButton;
  @UiField
  org.gwtbootstrap3.client.ui.Label leftSymbols;
  private HandlerRegistration nativePreviewHandler;
  private boolean commentHasFocus;
  private List<Stroke> notationStrokes = new ArrayList<>();

  @Inject
  DraughtsPlayerView(Binder uiBinder, EventBus eventBus, AppResources resources, DraughtsMessages messages,
                     ClientConfiguration config, Game game) {
    super(eventBus);
    this.eventBus = eventBus;
    this.resources = resources;
    this.messages = messages;
    this.config = config;
    this.game = game;

    initWidget(uiBinder.createAndBindUi(this));

    main.setHeight((Window.getClientHeight() - Variables.navbarTopHeight() * 2) + "px");
    setPopupPositioner(new PopupPositioner() {
      @Override
      protected int getLeft(int popupWidth) {
        return (Window.getClientWidth() - main.getOffsetWidth()) / 2;
      }

      @Override
      protected int getTop(int popupHeight) {
        return Variables.marginTop();
      }
    });

    HTML cap = new HTML(messages.playerCaption(game.getPlayerWhite().getPublicName(), game.getPlayerBlack().getPublicName(),
        TrUtils.translateEndGame(game.getPlayEndStatus())));
    caption.setHTML(cap.getHTML());
    dismiss.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        hide();
      }
    });
  }

  @Override
  protected void onAttach() {
    super.onAttach();

    registerNativeEvents();
    initMainPanel();
    initNotationPanel();
    Window.enableScrolling(false);
  }

  @Override
  protected void onDetach() {
    super.onDetach();

    nativePreviewHandler.removeHandler();
    Window.enableScrolling(true);
  }

  private void registerNativeEvents() {
    nativePreviewHandler = Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
      @Override
      public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
        previewNativeEvent(event);
      }
    });
  }

  private void initNotationPanel() {
    final HTML notationHtml = new HTML(game.getNotation());
    final Node gameNode = notationHtml.getElement().getFirstChild();
    if (gameNode.getChildCount() == 0) {
      enableComments(false);
      return;
    }
    int order = 0;
    for (int i = 0; i < gameNode.getChildCount(); i++) {
      final Node child = gameNode.getChild(i);
      if (child.getNodeType() == Node.ELEMENT_NODE) {
        Element wrapperNotation = ((Element) gameNode.getChild(i - 1));
        final Element step = ((Element) child);
        if (NotationPanel.NOTATION_SEP_TAG.toLowerCase().equals(step.getNodeName().toLowerCase())) {
          notationPanel.add(new Br());
          continue;
        }
        // если разделитель побитых шашек
        if (Stroke.BEAT_MOVE_SEP.equals(wrapperNotation.getNodeValue())) {
          wrapperNotation = ((Element) gameNode.getChild(i - 2));
        }
        String prevStep = "";
        if (wrapperNotation != null) {
          prevStep = wrapperNotation.getInnerHTML();
        }

        final Stroke stroke = StrokeFactory.createStrokeFromNotationHtml(step, prevStep, false);
        notationStrokes.add(stroke);

        Anchor anchor = new Anchor();
        anchor.setHTML(NotationPanel.wrapStroke(stroke, order));
        order++;
        anchor.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            final Integer pos = stroke.getOrder();
            if (pos == prevPos) {
              return;
            }
            prevPos = pos;
            toMove(pos);
          }
        });
        notationPanel.add(anchor);
      } else {
        notationPanel.add(new Span(child.getNodeValue()));
      }
    }
  }

  private void enableComments(boolean enable) {
    if (commentCurrentStrokeTextArea.isEnabled() != enable) {
      commentCurrentStrokeTextArea.setEnabled(enable);
    }
    if (commentCurrentStrokeButton.isEnabled() != enable) {
      commentCurrentStrokeButton.setEnabled(enable);
    }
  }

  private void initMainPanel() {
    side = getSide();
    deskSide = side - 80;

    BoardBackgroundLayer boardBackground = new BoardBackgroundLayer(side, deskSide, rows, cols);
    boardBackground.drawCoordinates(true);

    LienzoPanel lienzoPanel = new LienzoPanel(side, side);
    lienzoPanel.setBackgroundLayer(boardBackground);

    board = new Board(eventBus, boardBackground, rows, cols, true);
    board.setEmulate(true);
    lienzoPanel.add(board);

    deskPanel.add(lienzoPanel);
    initButtons();

    commentCurrentStrokeTextArea.addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        final int left = Integer.valueOf(config.strokeCommentLength())
            - commentCurrentStrokeTextArea.getText().length();
        if (left <= 0) {
          event.stopPropagation();
          return;
        }
        leftSymbols.setText(left
            + "");
      }
    });

    commentCurrentStrokeButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Element current = getCurrent();
        if (current == null) {
          commentCurrentStrokeTextArea.setText("");
          enableComments(false);
          return;
        }

        Element move = getStroke(Integer.valueOf(current.getAttribute(NotationPanel.DATA_ID_ATTR)));
        String oldComment = move.getAttribute(NotationPanel.DATA_COMMENT_ATTR);
        final String oComment = oldComment.length() != 0 ? (oldComment + NotationPanel.COMMENT_SEP) : "";
        move.setAttribute(NotationPanel.DATA_COMMENT_ATTR,
            oComment + commentCurrentStrokeTextArea.getText());
        commentCurrentStrokeTextArea.setText("");
      }
    });

    commentCurrentStrokeTextArea.addFocusListener(new FocusListener() {
      @Override
      public void onFocus(Widget sender) {
        commentHasFocus = true;
      }

      @Override
      public void onLostFocus(Widget sender) {
        commentHasFocus = false;
      }
    });
    enableComments(false);
  }

  private void initButtons() {
    toStartButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        toStart();
      }
    });
    prevButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        moveBack();
      }
    });
    playButton.addClickHandler(new ClickHandler() {
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
                playButton.setIcon(IconType.PLAY);
              }
            }
          };
          playTimer.scheduleRepeating(1000);
          playButton.setIcon(IconType.PAUSE);
        }
      }
    });
    nextButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        moveForward();
      }
    });
    toEndButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        toEnd();
      }
    });

    prevButton.setEnabled(false);
    toStartButton.setEnabled(false);
  }

  private void toMove(int pos) {
    int cursor = notationCursor;
    if (pos >= cursor) {
      for (int i = cursor; i <= pos; i++) {
        moveForward();
      }
    } else {
      for (int i = cursor - 1; i > pos; i--) {
        moveBack();
      }
    }
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
    if (Boolean.valueOf(outerNotation.getAttribute(NotationPanel.DATA_CONTINUE_BEAT_ATTR))
        || Boolean.valueOf(outerNotation.getAttribute(NotationPanel.DATA_STOP_BEAT_ATTR))) {
      step = getStroke(notationCursor - 1).getInnerHTML();
    }

    Stroke stroke = StrokeFactory.createStrokeFromNotationHtml(outerNotation, step, true);
    board.doEmulatedMoveBack(stroke, notationCursor);

    setBeatenMy(Board.DRAUGHTS_ON_SIDE - board.getMyDraughts().size());
    setBeatenOpponent(Board.DRAUGHTS_ON_SIDE - board.getOpponentDraughts().size());

    Element notation = getStroke(notationCursor - 1);
    if (null == notation) {
      enableComments(false);
      prevButton.setEnabled(false);
      toStartButton.setEnabled(false);
      playButton.setEnabled(true);
      nextButton.setEnabled(true);
      toEndButton.setEnabled(true);
      return;
    }

    // выделяем текущий ход
    String highlightedStep = highlightedStroke(notation);
    notation.setInnerHTML(highlightedStep);
  }

  private String highlightedStroke(Element notation) {
    return format("<%s id='current' %s='%s' class='%s'>%s</%s>",
          NotationPanel.NOTATION_TAG, NotationPanel.DATA_ID_ATTR, notation.getId(),
          resources.style().notationCurrentStyle(), notation.getInnerHTML(), NotationPanel.NOTATION_TAG);
  }

  private void moveForward() {
    if (atEnd) {
      return;
    }
    if (prevButton.isEnabled()) {
      prevButton.setEnabled(false);
    }
    if (toStartButton.isEnabled()) {
      toStartButton.setEnabled(false);
    }

    Element notation = getStroke(notationCursor);
    if (null == notation) {
      atEnd = true;
      return;
    }
    enableComments(true);

    if (!prevButton.isEnabled()) {
      prevButton.setEnabled(true);
    }
    if (!toStartButton.isEnabled()) {
      toStartButton.setEnabled(true);
    }

    // сбрасываем выделени хода
    Element wrapperNotation = getCurrent();
    String prevStep = "";
    if (wrapperNotation != null) {
      prevStep = wrapperNotation.getInnerHTML();
      Element outerNotation = getStrokeById(wrapperNotation.getAttribute("data-id"));
      outerNotation.setInnerHTML(wrapperNotation.getInnerHTML());
    }

    Stroke stroke = StrokeFactory.createStrokeFromNotationHtml(notation, prevStep, false);
    board.doMove(stroke, notationCursor);

    setBeatenMy(Board.DRAUGHTS_ON_SIDE - board.getMyDraughts().size());
    setBeatenOpponent(Board.DRAUGHTS_ON_SIDE - board.getOpponentDraughts().size());

    // выделяем ход
    notation = getStroke(notationCursor);
    String highlightedStep = highlightedStroke(notation);
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
    return notationPanel.getElementById(id);
  }

  private Element getStroke(int notationCursor) {
    return notationPanel.getElementById(String.valueOf(notationCursor));
  }

  public int getSide() {
    final int width = Window.getClientWidth();
    final int height = main.getOffsetHeight() - 40;
    return Math.min(width, height);
  }

  protected void previewNativeEvent(Event.NativePreviewEvent event) {
    if (event.getTypeInt() == Event.ONKEYDOWN) {
      switch (event.getNativeEvent().getKeyCode()) {
        case KeyCodes.KEY_ESCAPE:
          hide();
          break;
      }
      if (!commentHasFocus) {
        switch (event.getNativeEvent().getKeyCode()) {
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

  public Element getCurrent() {
    return notationPanel.getElementById("current");
  }

  public void setBeatenMy(int count) {
    beatenMineDraughtsLabel.setText(count + " - " + (board.isWhite() ? messages.whites()
        : messages.blacks()));
  }

  public void setBeatenOpponent(int count) {
    beatenOpponentDraughtsLabel.setText(count + " - " + (board.isWhite() ? messages.blacks()
        : messages.whites()));
  }

  interface Binder extends UiBinder<PopupPanel, DraughtsPlayerView> {
  }

  public static class ViewFactoryImpl implements DraughtsPlayerPresenter.ViewFactory {

    private final Binder binder;
    private final EventBus eventBus;
    private final DraughtsMessages messages;
    private final AppResources resources;
    private final ClientConfiguration config;

    @Inject
    public ViewFactoryImpl(Binder binder, EventBus eventBus, DraughtsMessages messages, AppResources resources, ClientConfiguration config) {
      this.binder = binder;
      this.eventBus = eventBus;
      this.messages = messages;
      this.resources = resources;
      this.config = config;
    }

    @Override
    public DraughtsPlayerPresenter.MyView create(Game game) {
      return new DraughtsPlayerView(binder, eventBus, resources, messages, config, game);
    }
  }
}
