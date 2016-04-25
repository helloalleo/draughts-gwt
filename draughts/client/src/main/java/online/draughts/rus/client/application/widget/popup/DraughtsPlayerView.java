package online.draughts.rus.client.application.widget.popup;

import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;
import com.gwtplatform.mvp.client.view.PopupPositioner;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.client.application.widget.NotationPanel;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.client.util.TrUtils;
import online.draughts.rus.draughts.*;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.dto.MoveDto;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.resource.GamesResource;
import online.draughts.rus.shared.util.StringUtils;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Br;
import org.gwtbootstrap3.client.ui.html.Span;

@SuppressWarnings("deprecation")
public class DraughtsPlayerView extends PopupViewWithUiHandlers<DraughtsPlayerUiHandlers>
    implements DraughtsPlayerPresenter.MyView, PlayComponent {
  private final AppResources resources;
  private static final int rows = 8;
  private static final int cols = 8;
  private GameDto game;
  private final ClientConfiguration config;
  private final CurrentSession currentSession;
  private final PlayerDto player;
  private final ResourceDelegate<GamesResource> gamesDelegate;
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
  private int notationCursor;
  private int prevPos = -1;
  private Timer playTimer;
  private boolean playing = false;
  private boolean atEnd = false;
  @UiField
  Label beatenMineDraughtsLabel;
  @UiField
  Label beatenOpponentDraughtsLabel;
  @UiField
  TextArea commentCurrentStrokeTextArea;
  @UiField
  Button commentCurrentStrokeButton;
  @UiField
  org.gwtbootstrap3.client.ui.Label leftSymbolsLabel;
  @UiField
  HTMLPanel gameComments;
  @UiField
  HTMLPanel currentStrokeComment;
  @UiField
  ScrollPanel gameCommentsScroll;
  @UiField
  ScrollPanel currentStrokeCommentScroll;
  @UiField
  Label logInToCommentLabel;
  @UiField
  Column draughtsDeskColumn;
  @UiField
  Heading allCommentsHeading;
  @UiField
  Heading commentsToStrokeHeading;
  @UiField
  Heading commentStrokeHeading;
  @UiField
  HTMLPanel notLoggedInCommentPanel;
  @UiField
  HTMLPanel loggedInCommentPanel;
  @UiField
  ScrollPanel notationScroll;
  @UiField
  HTMLPanel notationInfoPanel;
  @UiField
  HTMLPanel notationHTMLPanel;
  private HandlerRegistration nativePreviewHandler;
  private boolean commentHasFocus;

  DraughtsPlayerView(Binder uiBinder, EventBus eventBus, AppResources resources, DraughtsMessages messages,
                     ClientConfiguration config, CurrentSession currentSession,
                     ResourceDelegate<GamesResource> gamesDelegate,
                     GameDto game) {
    super(eventBus);
    this.resources = resources;
    this.messages = messages;
    this.config = config;
    this.currentSession = currentSession;
    this.player = currentSession.getPlayer();
    this.gamesDelegate = gamesDelegate;
    this.game = game;

    initWidget(uiBinder.createAndBindUi(this));

    loggedInCommentPanel.setVisible(currentSession.isLoggedIn());
    notLoggedInCommentPanel.setVisible(!currentSession.isLoggedIn());

    HTML cap = new HTML(messages.playerCaption(game.getPlayerWhite().getPublicName(), game.getPlayerBlack().getPublicName(),
        TrUtils.translateEndGame(game.getPlayEndStatus())));
    caption.setHTML(cap.getHTML());
    dismiss.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        hide();
      }
    });

    setPopupPositioner(new PopupPositioner() {
      @Override
      protected int getLeft(int popupWidth) {
        return 55;
      }

      @Override
      protected int getTop(int popupHeight) {
         return Window.getScrollTop() + 75;
      }
    });
  }

  @Override
  protected void onAttach() {
    if (currentSession.isLoggedIn()) {
      gamesDelegate.withCallback(new AbstractAsyncCallback<GameDto>() {
        @Override
        public void onSuccess(GameDto result) {
          DraughtsPlayerView.this.game = result;
          playerInit();
        }
      }).game(game.getId());
    } else {
      playerInit();
    }
  }

  private void playerInit() {
    registerNativeEvents();
    initMainPanel();
    initNotationPanel();
    initCommentPanel();
  }

  private void initCommentPanel() {
    final int offset = currentSession.isLoggedIn() ? loggedInCommentPanel.getOffsetHeight()
        : notLoggedInCommentPanel.getOffsetHeight();
    final int height = main.getOffsetHeight() / 2 - commentStrokeHeading.getOffsetHeight()
        - caption.getOffsetHeight() - offset - 20;
    gameCommentsScroll.setHeight(height + "px");
    currentStrokeCommentScroll.setHeight(height + "px");
  }

  @Override
  protected void onDetach() {
    nativePreviewHandler.removeHandler();
    deskPanel.clear();
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
    notationPanel.getElement().setAttribute("id", String.valueOf(game.getId()));
    int order = 0;
    for (int i = 0; i < gameNode.getChildCount(); i++) {
      final Node child = gameNode.getChild(i);
      // если у нас не спан
      if (child.getNodeName().toUpperCase().equals(NotationPanel.NOTATION_A_TAG.toUpperCase())) {
        // тогда у нас анчор, берем его содержимое
        final Element step = ((Element) child.getFirstChild());
        String prevStep = "";
        if (i > 0) {
          Element wrapperNotation = ((Element) gameNode.getChild(i - 1));
          // если разделитель побитых шашек
          if (Stroke.BEAT_MOVE_SEP.equals(wrapperNotation.getInnerText())) {
            wrapperNotation = ((Element) gameNode.getChild(i - 2));
            prevStep = "";
            if (wrapperNotation != null) {
              prevStep = wrapperNotation.getInnerText();
            }
          }
        }

        final Stroke stroke = StrokeFactory.createStrokeFromNotationHtml(step, prevStep, false);

        addGameComment(stroke.getOrder(), step.getInnerText(), stroke.getTitle(), stroke.getComment());

        Anchor anchor = new Anchor();
        anchor.addStyleName(resources.style().notationStrokeStyle());
        anchor.setHTML(NotationPanel.wrapStroke(stroke, order));
        order++;
        anchor.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            toMoveByClick(stroke.getOrder());
          }
        });
        notationPanel.add(anchor);
      } else if (child.getNodeName().toUpperCase().equals("BR")) {
        notationPanel.add(new Br());
      } else {
        notationPanel.add(new Span(((Element) child).getInnerText()));
      }
    }

    int side = draughtsDeskColumn.getOffsetWidth();
    notationScroll.setHeight((side - notationInfoPanel.getOffsetHeight() - 60) + "px");
  }

  private void toMoveByClick(int pos) {
    if (pos == prevPos) {
      return;
    }
    prevPos = pos;
    toMove(pos);
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
    int side = draughtsDeskColumn.getOffsetWidth();
    int deskSide = side - 80;

    BoardBackgroundLayer boardBackground = new BoardBackgroundLayer(side, deskSide, rows, cols);
    boardBackground.drawCoordinates(true);

    LienzoPanel lienzoPanel = new LienzoPanel(side, side);
    lienzoPanel.setBackgroundLayer(boardBackground);

    board = new Board(boardBackground, rows, cols, true);
    board.setEmulate(true);
    lienzoPanel.add(board);

    deskPanel.add(lienzoPanel);
    initButtons();

    if (player == null) {
      commentUnavailable();
    } else {
      initCommentAction();
    }
    enableComments(false);
  }

  private void commentUnavailable() {
    commentCurrentStrokeTextArea.setVisible(false);
    leftSymbolsLabel.setVisible(false);
    commentCurrentStrokeButton.setVisible(false);
    logInToCommentLabel.setVisible(true);
  }

  private void initCommentAction() {
    commentCurrentStrokeTextArea.addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeEvent().getCtrlKey()) {
          if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            submitComment();
            return;
          }
        }
        final int left = Integer.valueOf(config.strokeCommentLength())
            - commentCurrentStrokeTextArea.getText().length();
        if (left <= 0) {
          event.stopPropagation();
          return;
        }
        leftSymbolsLabel.setText(left
            + "");
      }
    });

    commentCurrentStrokeButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        submitComment();
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
  }

  private void submitComment() {
    if (commentCurrentStrokeTextArea.getText().isEmpty()) {
      return;
    }
    int cursor = notationCursor - 1;
    Element current = getStrokeById(cursor);
    Element prev = getStrokeById(cursor - 1);
    if (current == null && prev == null) {
      commentCurrentStrokeTextArea.setText("");
      enableComments(false);
      return;
    }
    if (current == null) {
      current = prev;
    }
    final String currentNotation = current.getInnerText();
    final int order = Integer.valueOf(current.getAttribute(NotationPanel.DATA_ORDER_ATTR));

    final String oldTitle = current.getAttribute(NotationPanel.DATA_TITLE_ATTR);
    final String oTitle = oldTitle.length() != 0 ? (oldTitle + NotationPanel.USER_COMMENT_SEP)
        : "";
    final String gameTitle = "<i>" + currentSession.getPlayer().getPublicName() + "</i>";
    final String newTitle = oTitle + gameTitle;
    if (StringUtils.isNotEmpty(newTitle)) {
      current.setAttribute(NotationPanel.DATA_TITLE_ATTR, newTitle);
    }
    final String oldComment = current.getAttribute(NotationPanel.DATA_COMMENT_ATTR);
    final String oComment = oldComment.length() != 0
        ? (oldComment + NotationPanel.USER_COMMENT_SEP)
        : "";
    String text = commentCurrentStrokeTextArea.getText().replace(config.escapeChars(), "");
    text = SimpleHtmlSanitizer.getInstance().sanitize(text).asString();
    final String gameComment = text.replace("\n", NotationPanel.COMMENT_SEP);
    final String newComment = oComment + gameComment + NotationPanel.COMMENT_SEP;

    current.setAttribute(NotationPanel.DATA_COMMENT_ATTR, newComment);

    commentCurrentStrokeTextArea.setText("");
    leftSymbolsLabel.setText(config.strokeCommentLength());
    commentCurrentStrokeTextArea.setFocus(false);

    game.setNotation(notationPanel.getElement().getString());

    gamesDelegate.withCallback(new AbstractAsyncCallback<GameDto>() {
      @Override
      public void onSuccess(GameDto result) {
        addGameComment(order, currentNotation, newTitle, newComment);
        setStrokeComment(newTitle, newComment);
      }
    }).save(game);
  }

  private void addGameComment(int order, String currentNotation, String gameTitle, String gameComment) {
    gameComments.clear();
    addComment(gameComments, gameCommentsScroll, order, currentNotation, gameTitle, gameComment);
  }


  private void setStrokeComment(String title, String comment) {
    currentStrokeComment.clear();
    addComment(currentStrokeComment, currentStrokeCommentScroll, -1, null, title, comment);
  }

  private void addComment(HTMLPanel panel, ScrollPanel scrollPanel, final int order, String currentNotation, String title, String comment) {
    if (StringUtils.isEmpty(title) || StringUtils.isEmpty(comment)) {
      return;
    }
    String[] titles = title.split(NotationPanel.USER_COMMENT_SEP);
    String[] comments = comment.split(NotationPanel.USER_COMMENT_SEP);
    if (titles.length != comments.length) {
      return;
    }
    for (int i = 0; i < titles.length; i++) {
      if (StringUtils.isEmpty(currentNotation)) {
        panel.add(new HTML(titles[i] + NotationPanel.TITLE_COMMENT_SEP + comments[i]));
      } else {
        Anchor anchor = new Anchor(currentNotation);
        anchor.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            toMoveByClick(order);
          }
        });
        panel.add(new Span(titles[i] + NotationPanel.TITLE_COMMENT_SEP));
        panel.add(anchor);
        panel.add(new Span(NotationPanel.TITLE_COMMENT_SEP + comments[i]));
      }
    }
    scrollPanel.scrollToBottom();
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
    Element currentNotation = getStrokeById(notationCursor - 1);
    if (currentNotation == null) {
      return;
    }
    notationCursor--;

    currentNotation.removeClassName(resources.style().notationCurrentStyle());

    String step = null;
    if (Boolean.valueOf(currentNotation.getAttribute(NotationPanel.DATA_CONTINUE_BEAT_ATTR))
        || Boolean.valueOf(currentNotation.getAttribute(NotationPanel.DATA_STOP_BEAT_ATTR))) {
      step = getStrokeById(notationCursor - 1).getInnerText();
    }

    Stroke stroke = StrokeFactory.createStrokeFromNotationHtml(currentNotation, step, true);
    board.doEmulatedMoveBack(stroke, notationCursor);

    setBeatenMy(Board.DRAUGHTS_ON_SIDE - board.getMyDraughts().size());
    setBeatenOpponent(Board.DRAUGHTS_ON_SIDE - board.getOpponentDraughts().size());

    Element notation = getStrokeById(notationCursor - 1);
    if (null == notation) {
      enableComments(false);
      prevButton.setEnabled(false);
      toStartButton.setEnabled(false);
      playButton.setEnabled(true);
      nextButton.setEnabled(true);
      toEndButton.setEnabled(true);
      return;
    }

    final String title = notation.getAttribute(NotationPanel.DATA_TITLE_ATTR);
    final String comment = notation.getAttribute(NotationPanel.DATA_COMMENT_ATTR);
    setStrokeComment(title, comment);

    // выделяем текущий ход
    notation.addClassName(resources.style().notationCurrentStyle());
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

    Element notation = getStrokeById(notationCursor);
    enableComments(true);

    if (!prevButton.isEnabled()) {
      prevButton.setEnabled(true);
    }
    if (!toStartButton.isEnabled()) {
      toStartButton.setEnabled(true);
    }

    // сбрасываем выделени хода
    Element currentNotation = getStrokeById(notationCursor - 1);
    String prevStep = "";
    if (currentNotation != null) {
      prevStep = currentNotation.getInnerText();
      currentNotation.removeClassName(resources.style().notationCurrentStyle());
    }

    Stroke stroke = StrokeFactory.createStrokeFromNotationHtml(notation, prevStep, false);
    board.doMove(stroke, notationCursor);
    setStrokeComment(stroke.getTitle(), stroke.getComment());

    setBeatenMy(Board.DRAUGHTS_ON_SIDE - board.getMyDraughts().size());
    setBeatenOpponent(Board.DRAUGHTS_ON_SIDE - board.getOpponentDraughts().size());

    // выделяем ход
    notation = getStrokeById(notationCursor);
    notation.addClassName(resources.style().notationCurrentStyle());

    notationCursor++;

    notation = getStrokeById(notationCursor);
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

  private Element getStrokeById(int id) {
    return notationPanel.getElementById(String.valueOf(id));
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

  public void setBeatenMy(int count) {
    beatenMineDraughtsLabel.setText(count + " - " + (board.isWhite() ? messages.whites()
        : messages.blacks()));
  }

  public void setBeatenOpponent(int count) {
    beatenOpponentDraughtsLabel.setText(count + " - " + (board.isWhite() ? messages.blacks()
        : messages.whites()));
  }

  @Override
  public String takeScreenshot() {
    throw new RuntimeException("Not implemented");
  }

  @Override
  public void checkWinner() {
    getUiHandlers().checkWinner();
  }

  @Override
  public void gameStuck(boolean isWhite) {
    getUiHandlers().gameStuck(isWhite);
  }

  @Override
  public void toggleTurn(boolean turn) {
    getUiHandlers().toggleTurn(turn);
  }

  @Override
  public void doPlayerMove(MoveDto move) {
  }

  @Override
  public void addNotationStroke(Stroke strokeForNotation) {
    throw new RuntimeException("Not implemented");
  }

  @Override
  public void doSaveMove(MoveDto move) {
    throw new RuntimeException("Not implemented");
  }

  interface Binder extends UiBinder<PopupPanel, DraughtsPlayerView> {
  }

  public static class ViewFactoryImpl implements DraughtsPlayerPresenter.ViewFactory {

    private final Binder binder;
    private final EventBus eventBus;
    private final DraughtsMessages messages;
    private final AppResources resources;
    private final ClientConfiguration config;
    private final CurrentSession currentSession;
    private final ResourceDelegate<GamesResource> gamesDelegate;

    @Inject
    public ViewFactoryImpl(Binder binder, EventBus eventBus,
                           DraughtsMessages messages, AppResources resources,
                           ClientConfiguration config, CurrentSession currentSession,
                           ResourceDelegate<GamesResource> gamesDelegate) {
      this.binder = binder;
      this.eventBus = eventBus;
      this.messages = messages;
      this.resources = resources;
      this.config = config;
      this.currentSession = currentSession;
      this.gamesDelegate = gamesDelegate;
    }

    @Override
    public DraughtsPlayerPresenter.MyView create(GameDto game) {
      return new DraughtsPlayerView(binder, eventBus, resources, messages, config, currentSession, gamesDelegate, game);
    }
  }
}
