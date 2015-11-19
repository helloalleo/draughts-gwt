package online.draughts.rus.client.application.widget.popup;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import online.draughts.rus.client.event.CheckWinnerEvent;
import online.draughts.rus.client.event.NotationStrokeEvent;
import online.draughts.rus.client.event.PlayMovePlayerMessageEvent;
import online.draughts.rus.client.event.TurnChangeEvent;
import online.draughts.rus.draughts.Stroke;
import online.draughts.rus.shared.model.Game;
import online.draughts.rus.shared.model.Move;


public class DraughtsPlayerPresenter extends PresenterWidget<DraughtsPlayerPresenter.MyView> implements DraughtsPlayerUiHandlers {

  private final Game game;

  DraughtsPlayerPresenter(
      EventBus eventBus,
      MyView view,
      Game game) {
    super(eventBus, view);

    this.game = game;
    getView().setUiHandlers(this);
  }

  public interface ViewFactory {
    MyView create(Game game);
  }

  public interface Factory {
    DraughtsPlayerPresenter create(Game game);
  }

  public static class FactoryImpl implements Factory {

    private final EventBus eventBus;
    private final ViewFactory viewFactory;

    @Inject
    FactoryImpl(EventBus eventBus,
                ViewFactory viewFactory) {
      this.eventBus = eventBus;
      this.viewFactory = viewFactory;
    }

    public DraughtsPlayerPresenter create(Game game) {
      return new DraughtsPlayerPresenter(eventBus, viewFactory.create(game), game);
    }
  }

  @Override
  public void checkWinner() {
    fireEvent(new CheckWinnerEvent());
  }

  @Override
  public void addNotationStroke(Stroke strokeForNotation) {
    fireEvent(new NotationStrokeEvent(strokeForNotation));
  }

  @Override
  public void toggleTurn(boolean turn) {
    fireEvent(new TurnChangeEvent(turn));
  }

  @Override
  public void doPlayerMove(Move move) {
    fireEvent(new PlayMovePlayerMessageEvent(move));
  }

  public interface MyView extends PopupView, HasUiHandlers<DraughtsPlayerUiHandlers> {
  }
}
