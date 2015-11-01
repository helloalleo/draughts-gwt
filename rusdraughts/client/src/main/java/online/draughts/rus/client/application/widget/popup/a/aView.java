
package online.draughts.rus.client.application.widget.popup.a;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class aView extends PopupViewImpl implements aPresenter.MyView {
  interface Binder extends UiBinder<PopupPanel, aView> {
  }

  @Inject
  aView(Binder uiBinder, EventBus eventBus) {
    super(eventBus);

    initWidget(uiBinder.createAndBindUi(this));
  }
}
