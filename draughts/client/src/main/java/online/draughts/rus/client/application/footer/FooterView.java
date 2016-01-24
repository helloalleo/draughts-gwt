
package online.draughts.rus.client.application.footer;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.constants.IconSize;

public class FooterView extends ViewImpl implements FooterPresenter.MyView {
  @UiField
  HTMLPanel panel;
  @UiField
  Anchor groupInVk;

  @Inject
  FooterView(Binder binder) {
    initWidget(binder.createAndBindUi(this));
  }

  @Override
  protected void onAttach() {
    if (Window.getClientWidth() < 768) {
      groupInVk.setIconSize(IconSize.TIMES3);
    }
  }

  interface Binder extends UiBinder<HTMLPanel, FooterView> {
  }
}
