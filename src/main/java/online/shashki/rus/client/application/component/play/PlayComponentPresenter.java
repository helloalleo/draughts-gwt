package online.shashki.rus.client.application.component.play;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import online.shashki.rus.client.event.RecivedPlayerListEvent;
import online.shashki.rus.client.event.RecivedPlayerListEventHandler;
import online.shashki.rus.shared.model.Shashist;

import java.util.List;


public class PlayComponentPresenter extends PresenterWidget<PlayComponentPresenter.MyView>
    implements PlayComponentUiHandlers {

  @Inject
  PlayComponentPresenter(
      EventBus eventBus,
      MyView view) {
    super(eventBus, view);

    getView().setUiHandlers(this);
    addVisibleHandler(RecivedPlayerListEvent.TYPE, new RecivedPlayerListEventHandler() {
          @Override
          public void onRecivedPlayerList(RecivedPlayerListEvent event) {
            getView().setPlayerList(event.getPlayerList());
          }
        }
    );
  }

  interface MyView extends View, HasUiHandlers<PlayComponentUiHandlers> {
    void setPlayerList(List<Shashist> shashistList);
  }
}
