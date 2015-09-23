package online.shashki.rus.client.application.profile;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.shashki.rus.client.place.NameTokens;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.constants.IconType;


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
    bindSlot(ProfilePresenter.SLOT_PROFILE_CONTENT, subcontent);
    setUpLinks();
  }

  private void setUpLinks() {
    for (NameTokens.Link link : nameTokens.getProfileLinks()) {
      navProfile.add(createAnchor(link));
    }
  }

  private AnchorListItem createAnchor(final NameTokens.Link link) {
    final AnchorListItem anchor = new AnchorListItem(link.name);
    switch (link.token) {
      case NameTokens.myGamesPage:
        anchor.setIcon(IconType.PLAY_CIRCLE);
        // делаем ссылку мои игры активной, так как она первая в списке
        anchor.setActive(true);
        break;
      case NameTokens.settingsPage:
        anchor.setIcon(IconType.COG);
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
