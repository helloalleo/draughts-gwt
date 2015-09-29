
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

    SHLog.log("PLAY SHOW PANEL GAME LIST - " + gameList.toString());

    getView().setUiHandlers(this);
  }

  interface MyView extends View, HasUiHandlers<PlayShowPanelUiHandlers> {
  }
}
