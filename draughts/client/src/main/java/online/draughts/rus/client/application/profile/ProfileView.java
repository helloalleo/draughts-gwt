package online.draughts.rus.client.application.profile;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.resources.Variables;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.NavPills;


public class ProfileView extends ViewWithUiHandlers<ProfileUiHandlers> implements ProfilePresenter.MyView {
  private final NameTokens nameTokens;

  @UiField
  SimplePanel main;
  @UiField
  SimplePanel subcontent;
  @UiField
  NavPills navProfile;

  private AnchorListItem prevAnchor;

  @Inject
  ProfileView(Binder uiBinder,
              NameTokens nameTokens) {
    initWidget(uiBinder.createAndBindUi(this));

    this.nameTokens = nameTokens;

    bindSlot(ProfilePresenter.SLOT_PROFILE, main);
    bindSlot(ProfilePresenter.SLOT_SETTINGS_CONTENT, subcontent);
    bindSlot(ProfilePresenter.SLOT_GENERAL_CONTENT, subcontent);
    bindSlot(ProfilePresenter.SLOT_COACH_SETTINGS_CONTENT, subcontent);
    setUpLinks();
  }

  @Override
  protected void onAttach() {
    main.setHeight(Window.getClientHeight() - Variables.marginTop() - Variables.footerHeight() + "px");
  }

  private void setUpLinks() {
    for (NameTokens.Link link : nameTokens.getProfileLinks()) {
      navProfile.add(createAnchor(link));
    }
  }

  private AnchorListItem createAnchor(final NameTokens.Link link) {
    final AnchorListItem anchor;
    if (Window.getClientWidth() < 768) {
      anchor = new AnchorListItem();
    } else {
      anchor = new AnchorListItem(link.name);
    }
    anchor.setIcon(link.iconType);
    switch (link.token) {
      case NameTokens.GENERAL_SETTINGS_PAGE:
        anchor.setActive(true);
        prevAnchor = anchor;
        break;
    }
    anchor.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        disableLink(prevAnchor);
        prevAnchor = anchor;
        anchor.setActive(true);
        getUiHandlers().displayPage(link.token);
      }
    });
    return anchor;
  }

  private void disableLink(AnchorListItem link) {
    if (link != null) {
      link.setActive(false);
    }
  }

  interface Binder extends UiBinder<Widget, ProfileView> {
  }
}
