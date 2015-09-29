
package online.shashki.rus.client.application.component.playrow;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class PlayRowPresenter extends PresenterWidget<PlayRowPresenter.MyView> implements PlayRowUiHandlers {
  @Inject
  PlayRowPresenter(EventBus eventBus,
                   MyView view
//                   AssistedInjectionFactory injectionFactory,
                   ) {
    super(eventBus, view);

    getView().setUiHandlers(this);
  }

  interface MyView extends View, HasUiHandlers<PlayRowUiHandlers> {
  }

}
