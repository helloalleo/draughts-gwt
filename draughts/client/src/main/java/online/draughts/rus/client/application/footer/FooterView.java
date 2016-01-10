
package online.draughts.rus.client.application.footer;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class FooterView extends ViewImpl implements FooterPresenter.MyView {
  @UiField
  HTMLPanel panel;

  @Inject
  FooterView(Binder binder) {
    initWidget(binder.createAndBindUi(this));
  }

  interface Binder extends UiBinder<HTMLPanel, FooterView> {
  }
}
