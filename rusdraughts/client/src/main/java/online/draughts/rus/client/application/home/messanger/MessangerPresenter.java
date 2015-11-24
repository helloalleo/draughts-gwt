
package online.draughts.rus.client.application.home.messanger;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class MessangerPresenter extends PresenterWidget<MessangerPresenter.MyView> implements MessangerUiHandlers {
  @Inject
  MessangerPresenter(final EventBus eventBus, final MyView view) {
    super(eventBus, view);

    getView().setUiHandlers(this);
  }

  interface MyView extends PopupView, HasUiHandlers<MessangerUiHandlers> {
  }
}
