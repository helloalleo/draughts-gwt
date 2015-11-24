
package online.draughts.rus.client.application.play.messanger;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class MessengerPresenter extends PresenterWidget<MessengerPresenter.MyView> implements MessengerUiHandlers {
  @Inject
  MessengerPresenter(final EventBus eventBus, final MyView view) {
    super(eventBus, view);

    getView().setUiHandlers(this);
  }

  interface MyView extends PopupView, HasUiHandlers<MessengerUiHandlers> {
  }
}
