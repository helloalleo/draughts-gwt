package online.shashki.rus.client.application.component.play;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;
import online.shashki.rus.client.application.home.HomePresenter;
import online.shashki.rus.client.event.RecivedPlayerListEvent;
import online.shashki.rus.client.event.RecivedPlayerListEventHandler;
import online.shashki.rus.shared.model.Shashist;

import java.util.List;


public class PlayComponentPresenter extends Presenter<PlayComponentPresenter.MyView, PlayComponentPresenter.MyProxy> implements PlayComponentUiHandlers {
  public static final NestedSlot SLOT_PLAYCOMPONENT = new NestedSlot();

  @Inject
  PlayComponentPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy) {
    super(eventBus, view, proxy, HomePresenter.SLOT_HOME);

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

  @ProxyStandard
  interface MyProxy extends Proxy<PlayComponentPresenter> {
  }
}
