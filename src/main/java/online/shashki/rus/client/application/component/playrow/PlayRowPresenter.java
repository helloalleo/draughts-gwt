
package online.shashki.rus.client.application.component.playrow;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import online.shashki.rus.shared.model.Game;

import java.util.List;

public class PlayRowPresenter extends PresenterWidget<PlayRowPresenter.MyView> implements PlayRowUiHandlers, Comparable<PlayRowPresenter> {
  private int order;

  @Inject
  PlayRowPresenter(EventBus eventBus,
                   MyView view,
                   int order,
                   List<Game> gameList) {
    super(eventBus, view);

    this.order = order;
    GWT.log(order + " HAS GAMES " + gameList.size());
    getView().setUiHandlers(this);
  }

  @Override
  public int compareTo(PlayRowPresenter p) {
    if (p == null) {
      return 1;
    }
    if (order < p.getOrder()) {
      return -1;
    }
    if (order > p.getOrder()) {
      return 1;
    }
    return 0;
  }

  public int getOrder() {
    return order;
  }

  public interface ViewFactory {
    MyView create();
  }

  public interface Factory {
    PlayRowPresenter create(int order, List<Game> gameList);
  }

  public static class FactoryImpl implements Factory {
    private final EventBus eventBus;
    private final ViewFactory viewFactory;

    @Inject
    public FactoryImpl(EventBus eventBus, ViewFactory viewFactory) {
      this.eventBus = eventBus;
      this.viewFactory = viewFactory;
    }

    @Override
    public PlayRowPresenter create(int order, List<Game> gameList) {
      return new PlayRowPresenter(eventBus, viewFactory.create(), order, gameList);
    }
  }
  
  interface MyView extends View, HasUiHandlers<PlayRowUiHandlers> {
  }
}
