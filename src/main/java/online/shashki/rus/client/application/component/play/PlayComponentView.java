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
import online.shashki.rus.client.application.widget.dialog.InviteDialogBox;
import online.shashki.rus.client.resources.ResourceLoader;
import online.shashki.rus.client.resources.constants.GSSConstants;
import online.shashki.rus.client.rpc.GameRpcServiceAsync;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.Shashist;
import online.shashki.rus.shashki.Board;
import org.gwtbootstrap3.client.ui.Button;

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
  Button connectToServerButton;
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
  private InviteDialogBox inviteDialogBox;
  private int CHECKERS_ON_DESK_INIT = 12;
  private GameRpcServiceAsync gameService;

  @Inject
  PlayComponentView(Binder uiBinder,
                    ShashkiMessages messages) {
    initWidget(uiBinder.createAndBindUi(this));
    this.messages = messages;

    initEmptyDeskPanel();
    initNotationPanel();
    initPlayersCellList();
  }

  @UiHandler("connectToServerButton")
  public void onConnectToServer(ClickEvent event) {
    switch (connectToServerButton.getIcon()) {
      case REFRESH:
        getUiHandlers().refreshConnectionToServer();
        break;
      case PLAY:
        getUiHandlers().connectToServer(playerSelectionModel.getSelectedObject());
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
            if (player.getSystemId().equals(value.getSystemId())) {
              sb.appendEscaped(playerPublicName);
            } else {
              if (value.isPlaying()) {
                img = new org.gwtbootstrap3.client.ui.Image(ResourceLoader.INSTANCE.images().playingIconImage().getSafeUri());
                img.setTitle(playerPublicName + " играет");
              } else {
                if (value.isOnline()) {
                  img = new org.gwtbootstrap3.client.ui.Image(ResourceLoader.INSTANCE.images().onlineIconImage().getSafeUri());
                  img.setTitle(playerPublicName + " в сети");
                } else {
                  img = new org.gwtbootstrap3.client.ui.Image(ResourceLoader.INSTANCE.images().offlineIconImage().getSafeUri());
                  img.setTitle(playerPublicName + " не в сети");
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

  interface Binder extends UiBinder<Widget, PlayComponentView> {
  }
}
