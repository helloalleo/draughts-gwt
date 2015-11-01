package online.draughts.rus.client.application.widget.popup;

import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
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
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.model.Game;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Span;

public class DraughtsPlayerView extends PopupViewWithUiHandlers<DraughtsPlayerUiHandlers> implements DraughtsPlayerPresenter.MyView {
  private final EventBus eventBus;
  private final AppResources resources;
  private final int rows = 8;
  private final int cols = 8;
  private final Game game;
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

  @Inject
  DraughtsPlayerView(Binder uiBinder, EventBus eventBus, AppResources resources, DraughtsMessages messages, Game game) {
    super(eventBus);
    this.eventBus = eventBus;
    this.resources = resources;
    this.messages = messages;
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

    initNotationPanel();
    createMainPanel();
  }

  private void initNotationPanel() {
    final HTML notationHtml = new HTML(game.getNotation());
    final Node gameNode = notationHtml.getElement().getFirstChild();
    for (int i = 0; i < gameNode.getChildCount(); i++) {
      final Node child = gameNode.getChild(i);
      if (child.getNodeType() == Node.ELEMENT_NODE) {
        org.gwtbootstrap3.client.ui.Anchor anchor = new org.gwtbootstrap3.client.ui.Anchor();
        anchor.setHTML(child.toString());
        anchor.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            HTML stroke = new HTML(child.toString());
            final Integer pos = Integer.valueOf(((Element) stroke.getElement().getFirstChild()).getId());
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

  private void createMainPanel() {
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
    if (Boolean.valueOf(outerNotation.getAttribute(NotationPanel.CONTINUE_BEAT))
        || Boolean.valueOf(outerNotation.getAttribute(NotationPanel.STOP_BEAT))) {
      step = getStroke(notationCursor - 1).getInnerHTML();
    }

    Stroke stroke = StrokeFactory.createStrokeFromNotationHtml(outerNotation, step, true);
    board.doEmulatedMoveBack(stroke, notationCursor);

    setBeatenMy(Board.DRAUGHTS_ON_SIDE - board.getMyDraughts().size());
    setBeatenOpponent(Board.DRAUGHTS_ON_SIDE - board.getOpponentDraughts().size());

    Element notation = getStroke(notationCursor - 1);
    if (null == notation) {
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
      atEnd = true;
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

    setBeatenMy(Board.DRAUGHTS_ON_SIDE - board.getMyDraughts().size());
    setBeatenOpponent(Board.DRAUGHTS_ON_SIDE - board.getOpponentDraughts().size());

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

  protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
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

    @Inject
    public ViewFactoryImpl(Binder binder, EventBus eventBus, DraughtsMessages messages, AppResources resources) {
      this.binder = binder;
      this.eventBus = eventBus;
      this.messages = messages;
      this.resources = resources;
    }

    @Override
    public DraughtsPlayerPresenter.MyView create(Game game) {
      return new DraughtsPlayerView(binder, eventBus, resources, messages, game);
    }
  }
}
