
package online.draughts.rus.client.application.home.messanger;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;

public class MessangerView extends PopupViewWithUiHandlers<MessangerUiHandlers> implements MessangerPresenter.MyView {
  @Inject
  MessangerView(Binder uiBinder, EventBus eventBus) {
    super(eventBus);

    initWidget(uiBinder.createAndBindUi(this));
  }

  interface Binder extends UiBinder<PopupPanel, MessangerView> {
  }
}
