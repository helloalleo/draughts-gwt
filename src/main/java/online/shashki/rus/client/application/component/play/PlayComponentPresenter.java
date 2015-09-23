package online.shashki.rus.client.application.component.play;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import online.shashki.rus.client.application.widget.dialog.MyDialogBox;
import online.shashki.rus.client.application.widget.dialog.InviteDialogBox;
import online.shashki.rus.client.event.GameMessageEvent;
import online.shashki.rus.client.event.RecivedPlayerListEvent;
import online.shashki.rus.client.event.RecivedPlayerListEventHandler;
import online.shashki.rus.client.websocket.GameWebsocket;
import online.shashki.rus.shared.dto.GameMessageDto;
import online.shashki.rus.shared.locale.ShashkiMessages;
import online.shashki.rus.shared.model.GameMessage;
import online.shashki.rus.shared.model.Shashist;

import java.util.List;


public class PlayComponentPresenter extends PresenterWidget<PlayComponentPresenter.MyView>
    implements PlayComponentUiHandlers {

  private final GameWebsocket gameWebsocket;
  private final ShashkiMessages messages;
  private EventBus eventBus;

  @Inject
  PlayComponentPresenter(
      EventBus eventBus,
      MyView view,
      ShashkiMessages messages,
      GameWebsocket gameWebsocket) {
    super(eventBus, view);

    this.eventBus = eventBus;
    this.messages = messages;
    this.gameWebsocket = gameWebsocket;
    getView().setUiHandlers(this);
    getView().setPlayer(gameWebsocket.getPlayer());
    addVisibleHandler(RecivedPlayerListEvent.TYPE, new RecivedPlayerListEventHandler() {
          @Override
          public void onRecivedPlayerList(RecivedPlayerListEvent event) {
            getView().setPlayerList(event.getPlayerList());
          }
        }
    );
  }

  @Override
  public void connectToServer(final Shashist opponent) {
    if (opponent == null) {
      new MyDialogBox(messages.info(), messages.selectPlayer());
      return;
    }
    if (opponent.getSystemId().equals(gameWebsocket.getPlayer().getSystemId())) {
      new MyDialogBox(messages.info(), messages.selectAnotherPlayerItsYou());
      return;
    }
    gameWebsocket.setOpponent(opponent);

    InviteDialogBox inviteDialogBox = new InviteDialogBox() {
      @Override
      public void submitted(boolean white) {
        GameMessage gameMessage = createSendGameMessage(gameWebsocket);
        gameMessage.setMessageType(GameMessage.MessageType.PLAY_INVITE);

        gameMessage.setMessage(messages.inviteMessage(gameWebsocket.getPlayer().getPublicName(),
            String.valueOf(white ? messages.black() : messages.white())));
        gameMessage.setData(String.valueOf(!white));

        eventBus.fireEvent(new GameMessageEvent(gameMessage));
      }
    };
    inviteDialogBox.show(messages.inviteToPlay(gameWebsocket.getOpponent().getPublicName(),
        messages.draughts()));
  }

  @Override
  public void refreshConnectionToServer() {
    if (gameWebsocket.isConnected()) {
      return;
    }
    gameWebsocket.connect();
  }

  private GameMessage createSendGameMessage(GameWebsocket gameWebsocket) {
    GameMessage gameMessage = GWT.create(GameMessageDto.class);
    gameMessage.setSender(gameWebsocket.getPlayer());
    gameMessage.setReceiver(gameWebsocket.getOpponent());
    gameMessage.setGame(gameWebsocket.getGame());
    return gameMessage;
  }

  interface MyView extends View, HasUiHandlers<PlayComponentUiHandlers> {
    void setPlayerList(List<Shashist> shashistList);

    void setPlayer(Shashist player);
  }
}
