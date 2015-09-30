
package online.shashki.rus.client.application.component.playshowpanel;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class PlayShowPanelPresenter extends PresenterWidget<PlayShowPanelPresenter.MyView> implements PlayShowPanelUiHandlers {
  @Inject
  PlayShowPanelPresenter(EventBus eventBus,
                         MyView view
                         ) {
    super(eventBus, view);

//    SHLog.log("PLAY SHOW PANEL GAME LIST - " + gameList);

    getView().setUiHandlers(this);
  }

  interface MyView extends View, HasUiHandlers<PlayShowPanelUiHandlers> {
  }
}
