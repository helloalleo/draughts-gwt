
package online.draughts.rus.client.application.widget.popup.a;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class aPresenter extends PresenterWidget<aPresenter.MyView> {
  interface MyView extends PopupView {
  }

  @Inject
  aPresenter(final EventBus eventBus, final MyView view) {
    super(eventBus, view);

  }

}
