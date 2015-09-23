
package online.shashki.rus.client.application.menu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.shashki.rus.client.place.NameTokens;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class MenuView extends ViewWithUiHandlers<MenuUiHandlers> implements MenuPresenter.MyView {
  @UiField
  HTMLPanel panel;
  @UiField
  NavbarNav navLeft;
  @UiField
  NavbarNav navRight;

  private AnchorListItem prevAnchor;
  private NameTokens nameTokens;

  @Inject
  MenuView(Binder binder, NameTokens nameTokens) {
    initWidget(binder.createAndBindUi(this));
    this.nameTokens = nameTokens;
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    setLinks();
  }

  private void setLinks() {
    for (final NameTokens.Link link : nameTokens.getLeftLinks()) {
      final AnchorListItem anchor = new AnchorListItem(link.name);
      switch (link.token) {
        case NameTokens.homePage:
          anchor.setIcon(IconType.HOME);
          break;
      }
      createAnchor(link, anchor);
      navLeft.add(anchor);
    }

    getUiHandlers().isAuthenticated(new AsyncCallback<Boolean>() {
      @Override
      public void onFailure(Throwable caught) {

      }

      @Override
      public void onSuccess(Boolean result) {
        if (result) {
          for (NameTokens.Link link : nameTokens.getRightAuthLinks()) {
            AnchorListItem anchor = new AnchorListItem(link.name);
            switch (link.token) {
              case NameTokens.logoutPage:
                anchor.setIcon(IconType.SIGN_OUT);
                anchor.setHref(NameTokens.logoutPage);
                break;
              case NameTokens.profilePage:
                anchor.setIcon(IconType.USER);
                break;
            }
            if (!link.token.equals(NameTokens.logoutPage)) {
              createAnchor(link, anchor);
            }
            navRight.add(anchor);
          }
        } else {
          for (NameTokens.Link link : nameTokens.getRightLinks()) {
            AnchorListItem anchor = new AnchorListItem(link.name);
            switch (link.token) {
              case NameTokens.loginPage:
                anchor.setIcon(IconType.SIGN_IN);
                break;
            }
            createAnchor(link, anchor);
            navRight.add(anchor);
          }
        }
      }
    });
  }

  private void createAnchor(final NameTokens.Link link, final AnchorListItem anchor) {
    anchor.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        disableLink(prevAnchor);
        prevAnchor = anchor;
        anchor.setActive(true);
        getUiHandlers().displayPage(link.token);
      }
    });
  }

  private void disableLink(AnchorListItem link) {
    if (link != null) {
      link.setActive(false);
    }
  }

  interface Binder extends UiBinder<HTMLPanel, MenuView> {
  }
}
