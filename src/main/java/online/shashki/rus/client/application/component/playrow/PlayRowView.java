
package online.shashki.rus.client.application.component.playrow;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class PlayRowView extends ViewWithUiHandlers<PlayRowUiHandlers> implements PlayRowPresenter.MyView {
  @UiField
  HTMLPanel panel;

  @Inject
  PlayRowView(Binder binder) {
    initWidget(binder.createAndBindUi(this));
  }

  interface Binder extends UiBinder<HTMLPanel, PlayRowView> {
  }
}
