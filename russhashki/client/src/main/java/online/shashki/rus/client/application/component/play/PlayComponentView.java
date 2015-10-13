package online.shashki.rus.client.application.component.play;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.ait.lienzo.shared.core.types.DataURLType;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.shashki.rus.client.application.widget.NotationPanel;
import online.shashki.rus.client.application.widget.dialog.ConfirmeDialogBox;
import online.shashki.rus.client.application.widget.dialog.InfoDialogBox;
import online.shashki.rus.client.application.widget.dialog.InviteDialogBox;
import online.shashki.rus.client.resources.AppResources;
import online.shashki.rus.client.resources.Variables;
import online.shashki.rus.client.util.SHLog;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.Player;
import online.shashki.rus.shashki.Board;
import online.shashki.rus.shashki.BoardBackgroundLayer;
import online.shashki.rus.shashki.Stroke;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.ArrayList;
import java.util.List;

public class PlayComponentView extends ViewWithUiHandlers<PlayComponentUiHandlers>
    implements PlayComponentPresenter.MyView {

  private final ShashkiMessages messages;
  private final AppResources resources;
  @UiField
  HTMLPanel main;
  @UiField
  HTMLPanel shashki;
  @UiField
  HTMLPanel shashkiColumn;
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
  ScrollPanel playerPanel;
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
  private CellList<Player> playerCellList;
  private SingleSelectionModel<Player> playerSelectionModel;
  private NotationPanel notationPanel;
  private InviteDialogBox inviteDialogBox;
  private boolean opponentColor;
  private Player opponent;

  @Inject
  PlayComponentView(Binder uiBinder,
                    ShashkiMessages messages,
                    AppResources resources) {
    initWidget(uiBinder.createAndBindUi(this));

    this.messages = messages;
    this.resources = resources;

    initEmptyDeskPanel();
    initPlayersCellList();
  }

  @UiHandler("playButton")
  public void onConnectToServer(ClickEvent event) {
    switch (playButton.getIcon()) {
      case REFRESH:
        getUiHandlers().refreshConnectionToServer();
        break;
      case PLAY:
        getUiHandlers().startPlayWith(playerSelectionModel.getSelectedObject());
        break;
    }
  }

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
  public void onCancelMove(ClickEvent event) {
    if (board == null) {
      return;
    }
    final Stroke lastStroke = board.getLastMove();
    final Stroke lastOpponentMove = board.getLastOpponentMove();
    if (lastOpponentMove != null && lastOpponentMove.isContinueBeat()) {
      return;
    }
    if (board.isMyTurn() && !(lastStroke != null && lastStroke.isContinueBeat())) {
      InfoDialogBox.setMessage(messages.youDontMove()).show();
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
    int shashkiSide = Window.getClientHeight() - Integer.valueOf(highStr);
    shashkiColumn.setWidth(shashkiSide + "px");

    lienzoPanel = new LienzoPanel(shashkiSide, shashkiSide);
    int lienzoSide = lienzoPanel.getHeight() - 20;
    Layer initDeskRect = new Layer();
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
    shashki.add(lienzoPanel);
  }

  @Override
  public void initNotationPanel(EventBus eventBus) {
    notationPanel = new NotationPanel(eventBus);
    notationList.add(notationPanel);

    SHLog.debug("INIT NOTATION PANEL");

    Scheduler.get().scheduleFinally(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        alignNotationPanel();
      }
    });
  }

  private void alignNotationPanel() {
    if (Window.getClientWidth() > 0) {
      String notationHeight = lienzoPanel.getHeight() - 170 + "px";
      notationPanel.setHeight(notationHeight);
      String playerListHeight = lienzoPanel.getHeight() - 105 + "px";
      playerCellList.setHeight(playerListHeight);
    }
  }

  private void initPlayersCellList() {
    playerCellList = new CellList<>(new AbstractCell<Player>() {
      @Override
      public void render(Context context, Player value, SafeHtmlBuilder sb) {
        if (value != null) {
          if (value.isLoggedIn()) {
            org.gwtbootstrap3.client.ui.Image img;
            String playerPublicName = value.getPublicName();
            SHLog.debug("CELL LIST userPublicName " + playerPublicName);
            if (player.getId().equals(value.getId())) {
              sb.appendEscaped(playerPublicName);
            } else {
              // TODO: не показывать статус. Пусть играют с теми кого знают либо наугад
              if (value.isPlaying()) {
                img = new org.gwtbootstrap3.client.ui.Image(resources.images().playingIconImage().getSafeUri());
                img.setTitle(playerPublicName + messages.playingTitle());
              } else {
                if (value.isOnline()) {
                  img = new org.gwtbootstrap3.client.ui.Image(resources.images().onlineIconImage().getSafeUri());
                  img.setTitle(playerPublicName + messages.onlineTitle());
                } else {
                  img = new org.gwtbootstrap3.client.ui.Image(resources.images().offlineIconImage().getSafeUri());
                  img.setTitle(playerPublicName + messages.offlineTitle());
                }
              }
              sb.appendHtmlConstant(img.getElement().getString());
              sb.appendEscaped(" " + playerPublicName);
            }
          }
        }
      }
    });
    playerSelectionModel = new SingleSelectionModel<>();
    playerCellList.setSelectionModel(playerSelectionModel);
    playerPanel.add(playerCellList);
  }

  @Override
  public void setPlayerList(List<Player> playerList) {
    SHLog.debug("setPlayerList " + playerList);
    playerCellList.setRowCount(0);
    playerCellList.setRowData(playerList);
  }

  @Override
  public void setPlayer(Player player) {
    this.player = player;
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

    playerCellList.setRowData(new ArrayList<Player>());
    turnLabel.setHTML(messages.youDisconnected());

    hidePlayingButtonsAndShowPlayButton();
  }

  @Override
  public void hidePlayingButtonsAndShowPlayButton() {
    playButton.setVisible(true);
    drawButton.setVisible(false);
    surrenderButton.setVisible(false);
    setBeatenMy(0);
    setBeatenOpponent(0);
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
    shashki.remove(lienzoPanel);
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
    board = new Board(getUiHandlers().getPlayEventBus(), backgroundLayer, 8, 8, white);
    lienzoPanel.add(board);
    lienzoPanel.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    updateTurn(getUiHandlers().isMyTurn());
    hidePlayButtonAndShowPlayingButtons();
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

  interface Binder extends UiBinder<Widget, PlayComponentView> {
  }
}
