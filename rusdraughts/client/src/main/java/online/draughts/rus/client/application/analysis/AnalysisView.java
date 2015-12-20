package online.draughts.rus.client.application.analysis;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.draughts.Board;
import online.draughts.rus.draughts.BoardBackgroundLayer;
import online.draughts.rus.draughts.PlayComponent;
import online.draughts.rus.draughts.Stroke;
import online.draughts.rus.shared.dto.MoveDto;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.RadioButton;

import javax.inject.Inject;


public class AnalysisView extends ViewWithUiHandlers<AnalysisUiHandlers>
    implements AnalysisPresenter.MyView, PlayComponent {

  @UiField
  HTMLPanel main;
  @UiField
  HTMLPanel whiteDesk;
  @UiField
  HTMLPanel blackDesk;
  @UiField
  Column blackDeskColumn;
  @UiField
  Column whiteDeskColumn;
  @UiField
  Button placeDraughts;
  @UiField
  Button clearDesk;
  @UiField
  HTML turnLabel;
  @UiField
  RadioButton whiteDraughtButton;
  @UiField
  RadioButton blackDraughtButton;
  @UiField
  CheckBox addDraughtCheckbox;
  private final DraughtsMessages messages;
  private Desk deskWhite;
  private Desk deskBlack;

  @Inject
  AnalysisView(Binder uiBinder,
               DraughtsMessages messages) {
    this.messages = messages;
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  protected void onAttach() {
    if (null == deskWhite) {
      deskWhite = initEmptyDesk(whiteDesk, whiteDeskColumn.getOffsetWidth(), true);
    }
    if (null == deskBlack) {
      deskBlack = initEmptyDesk(blackDesk, blackDeskColumn.getOffsetWidth(), false);
    }
    addDraughtChangeState();
  }

  @SuppressWarnings("unused")
  @UiHandler("placeDraughts")
  public void onPlaceDraughts(ClickEvent clickEvent) {
    deskWhite = placeDraughtsOnDesk(deskWhite, true);
    deskBlack = placeDraughtsOnDesk(deskBlack, false);
  }

  @UiHandler("addDraughtCheckbox")
  public void onAddDraughtCheckboxClicked(ClickEvent clickEvent) {
    addDraughtChangeState();
  }

  private void addDraughtChangeState() {
    whiteDraughtButton.setEnabled(addDraughtCheckbox.getValue());
    blackDraughtButton.setEnabled(addDraughtCheckbox.getValue());
    whiteDraughtButton.setValue(addDraughtCheckbox.getValue());
    blackDraughtButton.setValue(false);
  }

  private Desk placeDraughtsOnDesk(Desk desk, boolean white) {
    Board board = new Board(desk.backgroundLayer, 8, 8, white, true);
    board.setView(this);
    desk.lienzoPanel.add(board);
    desk.lienzoPanel.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    desk.board = board;
    updateTurn(getUiHandlers().isMyTurn());
    return desk;
  }

  @SuppressWarnings("unused")
  @UiHandler("clearDesk")
  public void onClearPlayComponent(ClickEvent clickEvent) {
    deskWhite.lienzoPanel.removeAll();
    deskWhite.board.clearDesk();
    whiteDesk.remove(deskWhite.lienzoPanel);
    deskWhite = initEmptyDesk(whiteDesk, whiteDesk.getOffsetWidth(), true);

    deskBlack.lienzoPanel.removeAll();
    deskBlack.board.clearDesk();
    blackDesk.remove(deskBlack.lienzoPanel);
    deskBlack = initEmptyDesk(blackDesk, blackDesk.getOffsetWidth(), false);
  }

  @Override
  public void updateTurn(boolean myTurn) {
    if (myTurn) {
      turnLabel.setHTML(messages.yourTurn());
    } else {
      turnLabel.setHTML(messages.opponentTurn());
    }
  }

  private Desk initEmptyDesk(HTMLPanel draughtsDesk, int draughtsSide, boolean white) {
    final LienzoPanel lienzoPanel = new LienzoPanel(draughtsSide, draughtsSide);
    int lienzoSide = lienzoPanel.getHeight() - 30;
    final Layer initDeskRect = new Layer();
    Rectangle contour = new Rectangle(lienzoSide, lienzoSide);
    contour.setX(1);
    contour.setY(1);
    initDeskRect.add(contour);
    lienzoPanel.setBackgroundLayer(initDeskRect);
    draughtsDesk.add(lienzoPanel);

    lienzoSide = lienzoSide - 30;
    BoardBackgroundLayer boardBackgroundLayer = new BoardBackgroundLayer(
        lienzoSide, lienzoSide - 30,
        8, 8);
    boardBackgroundLayer.drawCoordinates(white);
    lienzoPanel.setBackgroundLayer(boardBackgroundLayer);

    final Desk desk = new Desk();
    desk.lienzoPanel = lienzoPanel;
    desk.backgroundLayer = boardBackgroundLayer;

    lienzoPanel.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        GWT.log("ADD WHITE FORM " + whiteDraughtButton.getFormValue());
        GWT.log("ADD WHITE " + whiteDraughtButton.getValue());
        GWT.log("1");
        if (null == desk.board) {
          return;
        }
        GWT.log("ADD WHITE FORM " + whiteDraughtButton.getFormValue());
        GWT.log("ADD WHITE " + whiteDraughtButton.getValue());
        if (whiteDraughtButton.getValue()) {
          desk.board.addDraught(event.getX(), event.getY(), true);
        } else if (blackDraughtButton.getValue()) {
          desk.board.addDraught(event.getX(), event.getY(), false);
        }
      }
    });

    return desk;
  }

  @Override
  public void checkWinner() {

  }

  @Override
  public void addNotationStroke(Stroke strokeForNotation) {

  }

  @Override
  public void toggleTurn(boolean turn) {

  }

  @Override
  public void doPlayerMove(MoveDto move) {

  }

  @Override
  public PlayerDto getPlayer() {
    return null;
  }

  @Override
  public PlayerDto getOpponent() {
    return null;
  }

  @Override
  public String takeScreenshot() {
    return null;
  }

  private static class Desk {
    LienzoPanel lienzoPanel;
    Board board;
    BoardBackgroundLayer backgroundLayer;
  }

  interface Binder extends UiBinder<Widget, AnalysisView> {
  }
}
