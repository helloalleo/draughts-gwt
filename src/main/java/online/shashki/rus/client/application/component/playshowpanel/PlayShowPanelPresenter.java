
package online.shashki.rus.client.application.component.playshowpanel;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import online.shashki.rus.client.utils.SHLog;
import online.shashki.rus.shared.model.Game;

import java.util.List;

public class PlayShowPanelPresenter extends PresenterWidget<PlayShowPanelPresenter.MyView> implements PlayShowPanelUiHandlers {
  @Inject
  PlayShowPanelPresenter(EventBus eventBus,
                         MyView view,
                         @Assisted List<Game> gameList) {
    super(eventBus, view);

    SHLog.log("PLAY SHOW PANEL GAME LIST - " + gameList.size());
  }

  public interface ViewFactory {
    MyView create();
  }

  public interface MyView extends View, HasUiHandlers<PlayShowPanelUiHandlers> {
  }


  public interface Factory {
    PlayShowPanelPresenter create(List<Game> gameList);
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
    public PlayShowPanelPresenter create(List<Game> gameList) {
      return new PlayShowPanelPresenter(eventBus, viewFactory.create(), gameList);
    }
  }
}
