package online.draughts.rus.client.application.play;

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.DataURLType;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.client.application.widget.NotationPanel;
import online.draughts.rus.client.application.widget.dialog.ConfirmeDialogBox;
import online.draughts.rus.client.application.widget.dialog.InviteDialogBox;
import online.draughts.rus.client.application.widget.growl.Growl;
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.gin.NotationPanelFactory;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.resources.Variables;
import online.draughts.rus.client.util.Logger;
import online.draughts.rus.draughts.Board;
import online.draughts.rus.draughts.BoardBackgroundLayer;
import online.draughts.rus.draughts.PlayComponent;
import online.draughts.rus.draughts.Stroke;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.model.Friend;
import online.draughts.rus.shared.model.Move;
import online.draughts.rus.shared.model.Player;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayComponentView extends ViewWithUiHandlers<PlayComponentUiHandlers>
    implements PlayComponentPresenter.MyView, PlayComponent {

  private final DraughtsMessages messages;
  private final AppResources resources;
  private final NotationPanelFactory notationPanelFactory;
  private final PlaySession playSession;
  @UiField
  HTMLPanel main;
  @UiField
  HTMLPanel draughtsDesk;
  @UiField
  HTMLPanel draughtsColumn;
  @UiField
  HTMLPanel notationColumn;
  @UiField
  HTMLPanel notationList;
  @UiField
  Button playButton;
  @UiField
  Button drawButton;
  @UiField
  Button surrenderButton;
  @UiField
  HTMLPanel playerListColumn;
  @UiField
  HTML turnLabel;
  @UiField
  Label beatenOpponentDraughtsLabel;
  @UiField
  Label beatenMineDraughtsLabel;
  @UiField
  Button cancelMove;
  private Board board;
  private LienzoPanel lienzoPanel;
  private Player player;
  @UiField
  CellTable<Friend> playerFriendCellTable;
  @UiField
  CellTable<Player> playerCellTable;
  @UiField
  HTMLPanel infoHTMLPanel;
  @UiField
  HTMLPanel notationHTMLPanel;
  SingleSelectionModel<Friend> playerFriendSelectionModel;
  SingleSelectionModel<Player> playerSelectionModel;
  private boolean prevSelected = false;
  private NotationPanel notationPanel;
  private InviteDialogBox inviteDialogBox;
  private boolean opponentColor;
  private Player opponent;
  private List<Friend> playerFriendList;
  private Map<Long, Integer> unreadMessagesMap;
  private List<Player> playerList;

  @Inject
  PlayComponentView(Binder binder,
                    DraughtsMessages messages,
                    AppResources resources,
                    PlaySession playSession,
                    NotationPanelFactory notationPanelFactory) {
    this.messages = messages;
    this.resources = resources;
    this.notationPanelFactory = notationPanelFactory;
    this.playSession = playSession;

    initWidget(binder.createAndBindUi(this));

    initPlayerFriendsCellList();
    initPlayersCellList();

    initEmptyDeskPanel();
  }

  @SuppressWarnings(value = "unused")
  @UiHandler("playButton")
  public void onConnectToServer(ClickEvent event) {
    if (IconType.REFRESH.equals(playButton.getIcon())) {
      getUiHandlers().refreshConnectionToServer();
      player = playSession.getPlayer();
    } else {
      Player selectedPlayer = null;
      if (playerSelectionModel.getSelectedObject() != null) {
        selectedPlayer = playerSelectionModel.getSelectedObject();
      } else if (playerFriendSelectionModel.getSelectedObject() != null) {
        selectedPlayer = playerFriendSelectionModel.getSelectedObject().getPk().getFriend();
      }
      getUiHandlers().startPlayWith(selectedPlayer);
    }
  }

  @SuppressWarnings(value = "unused")
  @UiHandler("drawButton")
  public void onDrawButton(ClickEvent event) {
    new ConfirmeDialogBox(messages.doYouWantToProposeDraw()) {
      @Override
      public void procConfirm() {
        if (isConfirmed()) {
          getUiHandlers().proposeDraw();
        }
      }
    };
  }

  @SuppressWarnings(value = "unused")
  @UiHandler("surrenderButton")
  public void onSurrenderButton(ClickEvent event) {
    new ConfirmeDialogBox(messages.areYouSureYouWantSurrender()) {
      @Override
      public void procConfirm() {
        if (isConfirmed()) {
          getUiHandlers().playerSurrendered();
        }
      }
    };
  }

  @UiHandler("cancelMove")
  @SuppressWarnings(value = "unused")
  public void onCancelMove(ClickEvent event) {
    if (board == null) {
      return;
    }
    final Stroke lastStroke = board.getLastMove();
    final Stroke lastOpponentMove = board.getLastOpponentMove();
    if (lastOpponentMove != null && lastOpponentMove.isContinueBeat()) {
      return;
    }
    if (board.isMyTurn() && !(lastStroke == null || lastStroke.isContinueBeat()) || lastStroke == null) {
      Growl.growlNotif(messages.youDontMove());
      return;
    }
    new ConfirmeDialogBox(messages.doYouWantToCancelMove()) {
      @Override
      public void procConfirm() {
        if (isConfirmed()) {
          getUiHandlers().proposeCancelMove(lastStroke);
        }
      }
    };
  }

  private void initEmptyDeskPanel() {
    final String mainContainerMarginTop = Variables.S_MAIN_CONTAINER_SCROLL_MARGIN_TOP;
    final String highStr = mainContainerMarginTop.substring(0, mainContainerMarginTop.length() - 2); // отсекаем строку пикселей
    int draughtsSide = Window.getClientHeight() - Integer.valueOf(highStr);
    draughtsColumn.setWidth(draughtsSide + "px");

    lienzoPanel = new LienzoPanel(draughtsSide, draughtsSide);
    int lienzoSide = lienzoPanel.getHeight() - 20;
    final Layer initDeskRect = new Layer();
    Rectangle contour = new Rectangle(lienzoSide, lienzoSide);
    contour.setX(1);
    contour.setY(1);
    initDeskRect.add(contour);
    String[] descriptions = messages.playStartDescription().split("\n");
    int y = 0;
    for (String description : descriptions) {
      Text greeting = new Text(description, "Times New Roman", 14);
      greeting.setFillColor("blue");
      greeting.setStrokeColor("blue");
      greeting.setY(lienzoSide / 2 - 20 + y);
      greeting.setX(lienzoSide / 2 - 180);
      initDeskRect.add(greeting);
      y += 20;
    }
    lienzoPanel.setBackgroundLayer(initDeskRect);
    draughtsDesk.add(lienzoPanel);
    cancelMove.setEnabled(false);
  }

  @Override
  public void initNotationPanel(Long gameId) {
    if (notationPanel != null) {
      notationList.remove(notationPanel);
    }
    notationPanel = notationPanelFactory.createNotationPanel(gameId);
    notationList.add(notationPanel);

    Scheduler.get().scheduleFinally(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        alignNotationPanel();
      }
    });
  }

  @Override
  public void setUnreadMessagesMap(Map<Long, Integer> result) {
    this.unreadMessagesMap = result;
    setPlayerList(playerList);
  }

  private void alignNotationPanel() {
    if (Window.getClientWidth() > 0) {
      String notationHeight = lienzoPanel.getHeight() - infoHTMLPanel.getOffsetHeight() - 40 + "px";
      notationPanel.setHeight(notationHeight);
    }
  }

  private void initPlayerFriendsCellList() {
    playerFriendSelectionModel = new SingleSelectionModel<>();
    playerFriendCellTable.setSelectionModel(playerFriendSelectionModel);
    final HTML connectToServer = new HTML();
    connectToServer.setHTML(messages.connectToServer());
    connectToServer.getElement().getStyle().setFontSize(12, Style.Unit.PX);
    playerFriendCellTable.setLoadingIndicator(connectToServer);
    playerFriendCellTable.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    playerFriendSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        resetPlayerSelection(playerSelectionModel);
      }
    });

    Column<Friend, SafeHtml> statusColumn = new Column<Friend, SafeHtml>(new SafeHtmlCell()) {
      @Override
      public SafeHtml getValue(Friend friend) {
        return getStatusSafeHtml(friend.getPk().getFriend());
      }
    };
    statusColumn.setCellStyleNames(resources.style().cellWithButton());
    playerFriendCellTable.addColumn(statusColumn);

    TextColumn<Friend> publicNameColumn = new TextColumn<Friend>() {
      @Override
      public String getValue(Friend friend) {
        return friend.getPk().getFriend().getPublicName();
      }
    };
    publicNameColumn.setCellStyleNames(resources.style().cellWithButton());
    playerFriendCellTable.addColumn(publicNameColumn);

    final ButtonCell favoriteButtonCell = new ButtonCell();
    final Column<Friend, String> favoriteColumn = new Column<Friend, String>(favoriteButtonCell) {
      @Override
      public String getValue(Friend object) {
        return "";
      }

      @Override
      public void render(Cell.Context context, Friend object, SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<button type=\"button\" class=\"btn btn-link btn-large pull-right\" tabindex=\"-1\">");
        if (object != null) {
          Icon icon = new Icon(object.isFavorite() ? IconType.STAR : IconType.STAR_O);
          sb.appendHtmlConstant(icon.getElement().getString());
        }
        sb.appendHtmlConstant("</button>");
      }
    };
    favoriteColumn.setFieldUpdater(new FieldUpdater<Friend, String>() {
      @Override
      public void update(int index, Friend object, String value) {
        if (object != null) {
          object.setFavorite(!object.isFavorite());
          getUiHandlers().saveFriend(object);
          playerFriendList.get(playerFriendList.indexOf(object)).setFavorite(object.isFavorite());
          playerFriendCellTable.setRowCount(0);
          playerFriendCellTable.setRowData(playerFriendList);
        }
      }
    });
    playerFriendCellTable.addColumn(favoriteColumn);
  }

  private void initPlayersCellList() {
    playerSelectionModel = new SingleSelectionModel<>();
    playerCellTable.setSelectionModel(playerSelectionModel);
    final HTML connectToServer = new HTML();
    connectToServer.setHTML(messages.connectToServer());
    connectToServer.getElement().getStyle().setFontSize(12, Style.Unit.PX);
    playerCellTable.setLoadingIndicator(connectToServer);
    playerCellTable.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    playerSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        resetPlayerSelection(playerFriendSelectionModel);
      }
    });

    Column<Player, SafeHtml> statusColumn = new Column<Player, SafeHtml>(new SafeHtmlCell()) {
      @Override
      public SafeHtml getValue(Player player) {
        return getStatusSafeHtml(player);
      }
    };
    statusColumn.setCellStyleNames(resources.style().cellWithButton());
    playerCellTable.addColumn(statusColumn);

    TextColumn<Player> publicNameColumn = new TextColumn<Player>() {
      @Override
      public String getValue(Player player) {
        return player.getPublicName();
      }
    };
    publicNameColumn.setCellStyleNames(resources.style().cellWithButton());
    playerCellTable.addColumn(publicNameColumn);

    final ButtonCell writeButtonCell = new ButtonCell(ButtonType.LINK, IconType.PENCIL);
    final Column<Player, String> writeColumn = new Column<Player, String>(writeButtonCell) {
      @Override
      public String getValue(Player object) {
        return "";
      }

      @Override
      public void render(Cell.Context context, Player object, SafeHtmlBuilder sb) {
        if (!object.getId().equals(player.getId())) {
          super.render(context, object, sb);
        }
      }
    };
    writeColumn.setFieldUpdater(new FieldUpdater<Player, String>() {
      @Override
      public void update(int index, Player object, String value) {
        unreadMessagesMap.remove(object.getId());
        getUiHandlers().writeToFriend(object);
      }
    });
    playerCellTable.addColumn(writeColumn);

    TextColumn<Player> newMessagesColumn = new TextColumn<Player>() {
      @Override
      public String getValue(Player player) {
        if (unreadMessagesMap.containsKey(player.getId())) {
          return String.valueOf(unreadMessagesMap.get(player.getId()));
        }
        return "";
      }
    };
    newMessagesColumn.setCellStyleNames(resources.style().cellWithButton());
    playerCellTable.addColumn(newMessagesColumn);
  }

  private SafeHtml getStatusSafeHtml(Player player) {
    if (player.getId().equals(this.player.getId())) {
      Icon html = getIconString(IconType.USER);
      return new SafeHtmlBuilder().appendHtmlConstant(html.getElement().getString()).toSafeHtml();
    }
    Image img;
    String playerPublicName = player.getPublicName();
    if (player.isPlaying()) {
      img = new Image(resources.images().playingIconImage().getSafeUri());
      img.setTitle(playerPublicName + " " + messages.playingTitle());
    } else {
      if (player.isOnline()) {
        img = new Image(resources.images().onlineIconImage().getSafeUri());
        img.setTitle(playerPublicName + " " + messages.onlineTitle());
      } else {
        img = new Image(resources.images().offlineIconImage().getSafeUri());
        img.setTitle(playerPublicName + " " + messages.offlineTitle());
      }
    }
    return new SafeHtmlBuilder().appendHtmlConstant(img.getElement().getString()).toSafeHtml();
  }

  private Icon getIconString(IconType iconType) {
    final Icon userIcon = new Icon();
    userIcon.getElement().addClassName("fa btn-link");
    userIcon.getElement().addClassName(iconType.getCssName());
    return userIcon;
  }

  @SuppressWarnings(value = "unchecked")
  private void resetPlayerSelection(SingleSelectionModel<?> prevSelectionModel) {
    final Object selectedObject = prevSelectionModel.getSelectedObject();
    if (selectedObject == null) {
      return;
    }
    if (prevSelected) {
      prevSelected = false;
      return;
    }
    prevSelected = true;
    ((SingleSelectionModel<Object>) prevSelectionModel).setSelected(selectedObject, false);
  }

  @Override
  public void setPlayerFriendList(List<Friend> playerList) {
    playerFriendList = playerList;
    playerFriendCellTable.setRowCount(0);
    playerFriendCellTable.setRowData(playerList);
  }

  @Override
  public void setPlayerList(List<Player> playerList) {
    this.playerList = playerList;
    playerList.remove(player);
    playerList.add(0, player);
    playerCellTable.setRowCount(0);
    playerCellTable.setRowData(playerList);
  }

  @Override
  public void toggleInPlayButton() {
    playButton.setType(ButtonType.DEFAULT);
    playButton.setIcon(IconType.PLAY);
    playButton.setText(messages.play());
  }

  @Override
  public void setUpViewOnDisconnectFromServer() {
    playButton.setActive(true);
    playButton.setBlock(true);
    playButton.addStyleName("btn-danger");
    playButton.setIcon(IconType.REFRESH);
    playButton.setText(messages.reconnect());
    cancelMove.setEnabled(false);

    playerCellTable.setRowData(new ArrayList<Player>());
    playerFriendCellTable.setRowData(new ArrayList<Friend>());
    turnLabel.setHTML(messages.youDisconnected());

    hidePlayingButtonsAndShowPlayButton();
  }

  @Override
  public void hidePlayingButtonsAndShowPlayButton() {
    playButton.setVisible(true);
    drawButton.setVisible(false);
    surrenderButton.setVisible(false);
    beatenMineDraughtsLabel.setText("");
    beatenOpponentDraughtsLabel.setText("");
  }

  @Override
  public void hidePlayButtonAndShowPlayingButtons() {
    playButton.setVisible(false);
    drawButton.setVisible(true);
    surrenderButton.setVisible(true);
    setBeatenMy(0);
    setBeatenOpponent(0);
  }

  @Override
  public void clearPlayComponent() {
    lienzoPanel.removeAll();
    board.clearDesk();
    draughtsDesk.remove(lienzoPanel);
    initEmptyDeskPanel();

    turnLabel.setHTML(messages.playDidNotStart());
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
  public void startPlay(boolean white) {
    BoardBackgroundLayer backgroundLayer = initDeskPanel(white);
    board = new Board(backgroundLayer, 8, 8, white);
    board.setView(this);
    lienzoPanel.add(board);
    lienzoPanel.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    updateTurn(getUiHandlers().isMyTurn());
    cancelMove.setEnabled(true);
    hidePlayButtonAndShowPlayingButtons();

    lienzoPanel.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Circle circle = new Circle(2)
            .setX(event.getX())
            .setY(event.getY())
            .setFillColor(ColorName.RED);
        board.add(circle);
        lienzoPanel.draw();
      }
    });
  }

  @Override
  public void updateTurn(boolean myTurn) {
    if (myTurn) {
      turnLabel.setHTML(messages.yourTurn());
    } else {
      turnLabel.setHTML(messages.opponentTurn());
    }
  }

  @Override
  public int getMyDraughtsSize() {
    return board.getMyDraughts().size();
  }

  @Override
  public int getOpponentDraughtsSize() {
    return board.getOpponentDraughts().size();
  }

  @Override
  public boolean isWhite() {
    return board.isWhite();
  }

  @Override
  public void hideInviteDialog() {
    if (inviteDialogBox != null && inviteDialogBox.isVisible()) {
      inviteDialogBox.hide();
    }
  }

  @Override
  public void showInviteDialog(ClickHandler inviteClickHandler) {
    inviteDialogBox = new InviteDialogBox(inviteClickHandler) {
      @Override
      public void submitted(boolean white) {
        PlayComponentView.this.opponentColor = !white;
      }
    };
    inviteDialogBox.show(messages.inviteToPlay(opponent.getPublicName(),
        messages.draughts()));
  }

  @Override
  public boolean opponentColor() {
    return opponentColor;
  }

  @Override
  public String takeScreenshot() {
    return takeScreenshot(DataURLType.PNG, true);
  }

  public String takeScreenshot(DataURLType dataType, boolean includeBackgroundLayer) {
    return lienzoPanel.toDataURL(dataType, includeBackgroundLayer);
  }

  private BoardBackgroundLayer initDeskPanel(boolean white) {
    int lienzoSide = lienzoPanel.getHeight() - 50;
    if (lienzoSide > 800) {
      lienzoSide = 800;
    } else if (lienzoSide < 200) {
      lienzoSide = 200;
    }
    BoardBackgroundLayer boardBackgroundLayer = new BoardBackgroundLayer(
        lienzoSide, lienzoSide - 30,
        8, 8);
    boardBackgroundLayer.drawCoordinates(white);
    lienzoPanel.setBackgroundLayer(boardBackgroundLayer);
    return boardBackgroundLayer;
  }

  @Override
  public void setOpponent(Player opponent) {
    this.opponent = opponent;
  }

  @Override
  public Board getBoard() {
    return board;
  }


  @Override
  public void checkWinner() {
    getUiHandlers().checkWinner();
  }

  @Override
  public void addNotationStroke(Stroke strokeForNotation) {
    getUiHandlers().addNotationStroke(strokeForNotation);
  }

  @Override
  public void toggleTurn(boolean turn) {
    getUiHandlers().toggleTurn(turn);
  }

  @Override
  public void doPlayerMove(Move move) {
    getUiHandlers().doPlayerMove(move);
  }

  @Override
  public Player getPlayer() {
    return playSession.getPlayer();
  }

  @Override
  public Player getOpponent() {
    return playSession.getOpponent();
  }

  interface Binder extends UiBinder<Widget, PlayComponentView> {
  }
}
