
package online.shashki.rus.client.application.component.playshowpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class PlayRow extends Composite {

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  HTMLPanel panel;

  PlayRow() {
    initWidget(binder.createAndBindUi(this));
  }
  
  interface Binder extends UiBinder<HTMLPanel, PlayRow> {
  }
}
