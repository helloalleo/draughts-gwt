package online.draughts.rus.client.application.profile.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.shared.config.ClientConfiguration;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class SettingsView extends ViewWithUiHandlers<SettingsUiHandlers> implements SettingsPresenter.MyView {
  @UiField
  SimplePanel main;
  @UiField
  TextBox playerNameTextBox;
  @UiField
  CheckBox subscribeOnNewsletter;
  @UiField
  Button submitPlayerNameButton;
  @UiField
  InputGroupAddon playerNameAddon;

  private final ClientConfiguration config;

  @Inject
  SettingsView(Binder uiBinder,
               ClientConfiguration config) {
    this.config = config;
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(SettingsPresenter.SLOT_SETTINGS, main);
  }

  @Override
  protected void onAttach() {
    if (Window.getClientWidth() < 768) {
      submitPlayerNameButton.setText("");
      submitPlayerNameButton.setIcon(IconType.SAVE);
      playerNameAddon.setText("");
    }
  }

  @Override
  public void setPlayerName(String playerName) {
    playerNameTextBox.setText(playerName);
  }

  @Override
  public void setSubscribedOnNewsletter(boolean subscribeOnNewsletter) {
    this.subscribeOnNewsletter.setValue(subscribeOnNewsletter);
  }

  @SuppressWarnings("unused")
  @UiHandler("submitPlayerNameButton")
  public void onSubmitPlayerName(ClickEvent event) {
    getUiHandlers().submitNewPlayerName(playerNameTextBox.getText());
  }

  @SuppressWarnings("unused")
  @UiHandler("playerNameTextBox")
  public void onSubmit(KeyPressEvent event) {
    if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
      getUiHandlers().submitNewPlayerName(playerNameTextBox.getText());
    }
  }

  @SuppressWarnings("unused")
  @UiHandler("subscribeOnNewsletter")
  public void onSubscribeOnNewsletterChecked(ClickEvent event) {
    getUiHandlers().subscribeOnNewsletter(subscribeOnNewsletter.getValue());
  }

  public static class ViewFactoryImpl implements SettingsPresenter.ViewFactory {

    private final Binder binder;
    private final ClientConfiguration config;

    @Inject
    public ViewFactoryImpl(Binder binder, ClientConfiguration config) {
      this.binder = binder;
      this.config = config;
    }

    @Override
    public SettingsPresenter.MyView create() {
      return new SettingsView(binder, config);
    }
  }

  interface Binder extends UiBinder<Widget, SettingsView> {
  }
}
