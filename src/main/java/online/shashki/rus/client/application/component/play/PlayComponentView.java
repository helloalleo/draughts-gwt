package online.shashki.rus.client.application.component.play;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.shashki.rus.client.application.widget.NotationPanel;
import online.shashki.rus.client.resources.ResourceLoader;
import online.shashki.rus.client.resources.constants.GSSConstants;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.Shashist;
import online.shashki.rus.shashki.Board;
import online.shashki.rus.shashki.BoardBackgroundLayer;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.ArrayList;
import java.util.List;

public class PlayComponentView extends ViewWithUiHandlers<PlayComponentUiHandlers>
    implements PlayComponentPresenter.MyView {

  private final ShashkiMessages messages;
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
  private Shashist player;
  private CellList<Shashist> playersCellList;
  private SingleSelectionModel<Shashist> playerSelectionModel;
  private NotationPanel notationPanel;

  @Inject
  PlayComponentView(Binder uiBinder,
                    ShashkiMessages messages) {
    initWidget(uiBinder.createAndBindUi(this));
    this.messages = messages;

    initEmptyDeskPanel();
    initNotationPanel();
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

  private void initEmptyDeskPanel() {
    final String mainContainerMarginTop = GSSConstants.mainContainerMarginTop();
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

  private void initNotationPanel() {
    notationPanel = new NotationPanel();
    notationList.add(notationPanel);

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
      playersCellList.setHeight(playerListHeight);
    }
  }

  private void initPlayersCellList() {
    playersCellList = new CellList<>(new AbstractCell<Shashist>() {
      @Override
      public void render(Context context, Shashist value, SafeHtmlBuilder sb) {
        if (value != null) {
          if (value.isLoggedIn()) {
            org.gwtbootstrap3.client.ui.Image img;
            String playerPublicName = value.getPublicName();
            if (player.getId().equals(value.getId())) {
              sb.appendEscaped(playerPublicName);
            } else {
              // TODO: не показывать статус. Пусть играют с теми кого знают либо наугад
              if (value.isPlaying()) {
                img = new org.gwtbootstrap3.client.ui.Image(ResourceLoader.INSTANCE.images().playingIconImage().getSafeUri());
                img.setTitle(playerPublicName + messages.playingTitle());
              } else {
                if (value.isOnline()) {
                  img = new org.gwtbootstrap3.client.ui.Image(ResourceLoader.INSTANCE.images().onlineIconImage().getSafeUri());
                  img.setTitle(playerPublicName + messages.onlineTitle());
                } else {
                  img = new org.gwtbootstrap3.client.ui.Image(ResourceLoader.INSTANCE.images().offlineIconImage().getSafeUri());
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
    playersCellList.setSelectionModel(playerSelectionModel);
    playerPanel.add(playersCellList);
  }

  @Override
  public void setPlayerList(List<Shashist> shashistList) {
    playersCellList.setRowData(shashistList);
  }

  @Override
  public void setPlayer(Shashist player) {
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

    playersCellList.setRowData(new ArrayList<Shashist>());
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
    lienzoPanel.add(board);
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

  private BoardBackgroundLayer initDeskPanel(boolean white) {
    int lienzoSide = lienzoPanel.getHeight() - 50;
    BoardBackgroundLayer boardBackgroundLayer = new BoardBackgroundLayer(
        lienzoSide, lienzoSide - 30,
        8, 8);
    boardBackgroundLayer.drawCoordinates(white);
    lienzoPanel.setBackgroundLayer(boardBackgroundLayer);
    return boardBackgroundLayer;
  }

  interface Binder extends UiBinder<Widget, PlayComponentView> {
  }
}
