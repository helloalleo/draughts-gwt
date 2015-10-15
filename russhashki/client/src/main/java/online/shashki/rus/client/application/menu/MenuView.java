
package online.shashki.rus.client.application.menu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.client.resources.AppResources;
import online.shashki.rus.client.resources.Variables;
import online.shashki.rus.client.util.SHCookies;
import online.shashki.rus.client.util.SHLog;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class MenuView extends ViewWithUiHandlers<MenuUiHandlers> implements MenuPresenter.MyView {
  private final Image logo;
  @UiField
  HTMLPanel panel;
  @UiField
  NavbarNav navLeft;
  @UiField
  NavbarNav navRight;
  //  @UiField
//  Image logoImg;
  @UiField
  NavbarBrand brand;
  @UiField
  Navbar navbar;

  private AnchorListItem prevAnchor;
  private NameTokens nameTokens;
  private AppResources resources;

  @Inject
  MenuView(Binder binder,
           AppResources resources,
           NameTokens nameTokens) {
    initWidget(binder.createAndBindUi(this));

    this.nameTokens = nameTokens;
    this.resources = resources;

    logo = new Image(resources.images().logo());
    logo.setSize(Variables.S_LOGO_TOP_HEIGHT, Variables.S_LOGO_TOP_HEIGHT);
    brand.add(logo);

    Window.addWindowScrollHandler(new Window.ScrollHandler() {
      @Override
      public void onWindowScroll(Window.ScrollEvent event) {
        if (event.getScrollTop() == 0) {
          SHLog.debug("Scrolling");
          navbarTopHeight();
        } else {
          navbarScrollHeight();
        }
      }
    });
  }

  private void setLinkHeight(String height) {
    boolean top = Variables.S_NAVBAR_TOP_HEIGHT.equals(height);
    for (Widget widget : navLeft) {
      setLinkChildHeight(widget.getElement(), top);
    }
    for (Widget widget : navRight) {
      setLinkChildHeight(widget.getElement(), top);
    }
  }

  private void setLinkChildHeight(Element element, boolean top) {
//    if (top) {
//      element.addClassName(resources.style().navbarTop());
//      element.removeClassName(resources.style().navbarScroll());
//    } else {
//      element.addClassName(resources.style().navbarScroll());
//      element.removeClassName(resources.style().navbarTop());
//    }
    for (int i = 0; i < element.getChildCount(); i++) {
      final Element child = (Element) element.getChild(i);
      if (top) {
        child.addClassName(resources.style().navbarTop());
        child.removeClassName(resources.style().navbarScroll());
      } else {
        child.addClassName(resources.style().navbarScroll());
        child.removeClassName(resources.style().navbarTop());
      }
    }
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    setLinks();
  }

  private void setLinks() {
    for (final NameTokens.Link link : nameTokens.getLeftLinks()) {
      final AnchorListItem anchor = createAnchor(link);
      navLeft.add(anchor);
    }

    if (getUiHandlers().isLoggedIn()) {
      for (NameTokens.Link link : nameTokens.getRightAuthLinks()) {
        AnchorListItem anchor;
        if (link.token.equals(NameTokens.logoutPage)) {
          anchor = new AnchorListItem(link.name);
          anchor.setIcon(IconType.SIGN_OUT);
          anchor.setHref(link.token);
        } else {
          anchor = createAnchor(link);
        }
        navRight.add(anchor);
      }
    } else {
      for (NameTokens.Link link : nameTokens.getRightLinks()) {
        AnchorListItem anchor = createAnchor(link);
        navRight.add(anchor);
      }
    }

    highlightMenu();
    navbarTopHeight();
  }

  private void navbarScrollHeight() {
    final String navbarScrollHeight = Variables.S_NAVBAR_SCROLL_HEIGHT;
    navbar.getElement().addClassName(resources.style().navbarScroll());
    navbar.getElement().removeClassName(resources.style().navbarTop());
    setLinkHeight(navbarScrollHeight);
    logo.addStyleName(resources.style().logoScroll());
    logo.removeStyleName(resources.style().logoTop());
  }

  private void navbarTopHeight() {
    final String navbarTopHeight = Variables.S_NAVBAR_TOP_HEIGHT;
    navbar.getElement().addClassName(resources.style().navbarTop());
    navbar.getElement().removeClassName(resources.style().navbarScroll());
    setLinkHeight(navbarTopHeight);
    logo.addStyleName(resources.style().logoTop());
    logo.removeStyleName(resources.style().logoScroll());
  }

  private AnchorListItem createAnchor(final NameTokens.Link link) {
    final AnchorListItem anchor = new AnchorListItem(link.name);
    switch (link.token) {
      case NameTokens.homePage:
        break;
      case NameTokens.profilePage:
        anchor.setIcon(IconType.USER);
        break;
      case NameTokens.loginPage:
        anchor.setIcon(IconType.SIGN_IN);
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
    anchor.setId(link.token);
    return anchor;
  }

  private void disableLink(AnchorListItem link) {
    if (link != null) {
      link.setActive(false);
    }
  }

  public void highlightMenu() {
    String nameToken = SHCookies.getLocation();
    if (nameToken == null || nameToken.isEmpty()) {
      return;
    }
    SHLog.debug("highlight token " + nameToken);
    for (Widget widget : navLeft) {
      if (setActiveAnchor(nameToken, (AnchorListItem) widget)) return;
    }
    for (Widget widget : navRight) {
      if (setActiveAnchor(nameToken, (AnchorListItem) widget)) return;
    }
  }

  private boolean setActiveAnchor(String nameToken, AnchorListItem anchor) {
    if (nameToken.equals(anchor.getId())) {
      anchor.setActive(true);
      prevAnchor = anchor;
      return true;
    }
    return false;
  }

  interface Binder extends UiBinder<HTMLPanel, MenuView> {
  }
}
