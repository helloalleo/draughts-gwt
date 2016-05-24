
package online.draughts.rus.client.application.profile.general;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.shared.config.ClientConfiguration;
import org.gwtbootstrap3.client.ui.CheckBox;

public class GeneralView extends ViewWithUiHandlers<GeneralUiHandlers> implements GeneralPresenter.MyView {
  @UiField
  HTMLPanel panel;
  @UiField
  CheckBox extendAccount;

  private final ClientConfiguration config;

  @Inject
  GeneralView(Binder binder,
              ClientConfiguration config) {
    initWidget(binder.createAndBindUi(this));

    this.config = config;
  }

  @Override
  public void setSubscribed(boolean subscribed) {
    extendAccount.setValue(subscribed);
  }

  @SuppressWarnings("unused")
  @UiHandler("extendAccount")
  public void onExtendClick(ClickEvent event) {
    getUiHandlers().setSubscribed(extendAccount.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("removeAccount")
  public void onRemoveAccount(ClickEvent event) {
    Window.Location.assign(config.removeAccountUrl());
  }

  public static class ViewFactoryImpl implements GeneralPresenter.ViewFactory {

    private final GeneralView.Binder binder;
    private final ClientConfiguration config;

    @Inject
    public ViewFactoryImpl(GeneralView.Binder binder, ClientConfiguration config) {
      this.binder = binder;
      this.config = config;
    }

    @Override
    public GeneralPresenter.MyView create() {
      return new GeneralView(binder, config);
    }
  }


  interface Binder extends UiBinder<HTMLPanel, GeneralView> {
  }
}
