package online.shashki.rus.client.application.profile;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;


public class ProfileView extends ViewWithUiHandlers<ProfileUiHandlers> implements ProfilePresenter.MyView {
  @UiField
  SimplePanel main;

  @Inject
  ProfileView(Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(ProfilePresenter.SLOT_PROFILE, main);
  }

  interface Binder extends UiBinder<Widget, ProfileView> {
  }
}
