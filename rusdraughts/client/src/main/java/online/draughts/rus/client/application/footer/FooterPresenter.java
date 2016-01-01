
package online.draughts.rus.client.application.footer;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class FooterPresenter extends PresenterWidget<FooterPresenter.MyView> {

  @Inject
  FooterPresenter(EventBus eventBus, MyView view) {
    super(eventBus, view);
  }

  interface MyView extends View {
  }
}
