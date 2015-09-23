package online.shashki.rus.client.application.profile.settings;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;


public class SettingsView extends ViewWithUiHandlers<SettingsUiHandlers> implements SettingsPresenter.MyView {
  interface Binder extends UiBinder<Widget, SettingsView> {
  }

  @UiField
  SimplePanel main;

  @Inject
  SettingsView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(SettingsPresenter.SLOT_SETTINGS, main);
  }
}
