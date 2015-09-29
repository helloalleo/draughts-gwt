
package online.shashki.rus.client.application.component.playitem;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import online.shashki.rus.shared.model.Game;

public class PlayItemPresenter extends PresenterWidget<PlayItemPresenter.MyView> implements PlayItemUiHandlers {
  private final Game game;

  @Inject
  PlayItemPresenter(EventBus eventBus,
                    MyView view
                    ) {
    super(eventBus, view);
    this.game = new Game();

    getView().setUiHandlers(this);
  }

  @Override
  protected void onBind() {
    super.onBind();

    getView().setGame(game);
  }

  interface MyView extends View, HasUiHandlers<PlayItemUiHandlers> {
    void setGame(Game game);
  }
}
