package online.draughts.rus.client.application.profile.coach;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.locale.DraughtsMessages;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;

import javax.inject.Inject;


public class CoachView extends ViewWithUiHandlers<CoachUiHandlers> implements CoachPresenter.MyView {
  private final ClientConfiguration config;
  private final DraughtsMessages messages;

  @UiField
  SimplePanel main;
  @UiField
  Button applyToCoach;
  @UiField
  Label applicationResponse;

  @Inject
  CoachView(Binder uiBinder, ClientConfiguration config, DraughtsMessages messages) {
    initWidget(uiBinder.createAndBindUi(this));

    this.config = config;
    this.messages = messages;

    bindSlot(CoachPresenter.SLOT_COACH, main);
  }

  @UiHandler("applyToCoach")
  public void applyToCoachesClick(ClickEvent event) {
    getUiHandlers().applyToCoach();
  }

  @Override
  public void setApplyToCoachEnabled(boolean enabled) {
    applyToCoach.setEnabled(enabled);
    if (!enabled) {
      applicationResponse.setText(messages.youAreCoach());
    }
  }

  public static class ViewFactoryImpl implements CoachPresenter.ViewFactory {

    private final CoachView.Binder binder;
    private final ClientConfiguration config;
    private final DraughtsMessages messages;

    @com.google.inject.Inject
    public ViewFactoryImpl(CoachView.Binder binder, DraughtsMessages messages, ClientConfiguration config) {
      this.binder = binder;
      this.config = config;
      this.messages = messages;
    }

    @Override
    public CoachPresenter.MyView create() {
      return new CoachView(binder, config, messages);
    }
  }

  interface Binder extends UiBinder<Widget, CoachView> {
  }
}
