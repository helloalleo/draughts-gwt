
package online.shashki.rus.client.application.component.playshowpanel;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class PlayShowPanelView extends ViewWithUiHandlers<PlayShowPanelUiHandlers>
    implements PlayShowPanelPresenter.MyView {
  @UiField
  FlowPanel panel;
  @UiField
  HTMLPanel p;

  @Inject
  PlayShowPanelView(Binder binder) {
    initWidget(binder.createAndBindUi(this));
//    bindSlot(PlayShowPanelPresenter.SLOT_PLAY_ROW, panel);
//    bindSlot(PlayShowPanelPresenter.SLOT_PLAY_2, p);
  }

  public static class FactoryImpl implements PlayShowPanelPresenter.ViewFactory {

    private final Binder binder;

    @Inject
    public FactoryImpl(Binder binder) {
      this.binder = binder;
    }

    @Override
    public PlayShowPanelPresenter.MyView create() {
      return new PlayShowPanelView(binder);
    }
  }

  interface Binder extends UiBinder<HTMLPanel, PlayShowPanelView> {
  }
}
