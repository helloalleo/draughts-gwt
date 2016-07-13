package online.draughts.rus.client.application.game;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.presenter.slots.Slot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import online.draughts.rus.client.application.ApplicationPresenter;
import online.draughts.rus.client.application.widget.popup.DraughtsPlayerPresenter;
import online.draughts.rus.client.gin.DialogFactory;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.resource.GamesResource;


public class GamePresenter extends Presenter<GamePresenter.MyView, GamePresenter.MyProxy> implements GameUiHandlers {
  static final NestedSlot SLOT_GAME = new NestedSlot();
  static final Slot<DraughtsPlayerPresenter> SLOT_PLAYER = new Slot<>();

  private final DraughtsPlayerPresenter.Factory draughtsPlayerFactory;
  private final ResourceDelegate<GamesResource> gamesDelegate;
  private final DraughtsMessages messages;
  private final DialogFactory dialogFactory;

  @Inject
  GamePresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      DraughtsPlayerPresenter.Factory draughtsPlayerFactory,
      ResourceDelegate<GamesResource> gamesDelegate,
      DraughtsMessages messages,
      DialogFactory dialogFactory) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);
    this.draughtsPlayerFactory = draughtsPlayerFactory;
    this.gamesDelegate = gamesDelegate;
    this.messages = messages;
    this.dialogFactory = dialogFactory;

    getView().setUiHandlers(this);
  }

  @Override
  public void prepareFromRequest(PlaceRequest request) {
    // Access the "id" parameter in the current place request.
    // "0" is returned if "id" is missing.
    String id = request.getParameter("id", null);
    if (null == id) {
      dialogFactory.createInfoDialogBox(messages.gameNotFound()).show();
      return;
    }
    gamesDelegate.withCallback(
        new AbstractAsyncCallback<GameDto>(dialogFactory) {
          @Override
          public void onSuccess(GameDto result) {
            if (null == result) {
              dialogFactory.createInfoDialogBox(messages.gameNotFound()).show();
              return;
            }
            DraughtsPlayerPresenter draughtsPlayer = draughtsPlayerFactory.create(result, true);
            addToSlot(SLOT_PLAYER, draughtsPlayer);
          }
        }
    ).game(Long.parseLong(id));
  }

  interface MyView extends View, HasUiHandlers<GameUiHandlers> {
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.GAME_PAGE)
  @NoGatekeeper
  interface MyProxy extends ProxyPlace<GamePresenter> {
  }

}
