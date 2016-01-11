
package online.draughts.rus.client.application.menu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.resources.AppResources;
import online.draughts.rus.client.resources.Variables;
import online.draughts.rus.client.util.Cookies;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.Date;

@SuppressWarnings("deprecation")
public class MenuView extends ViewWithUiHandlers<MenuUiHandlers> implements MenuPresenter.MyView {
  private final Image logo;
  private final Cookies cookies;
  @UiField
  HTMLPanel panel;
  @UiField
  NavbarNav navLeft;
  @UiField
  NavbarNav navRight;
  @UiField
  NavbarBrand brand;
  @UiField
  Navbar navbar;

  private AnchorListItem prevAnchor;
  private NameTokens nameTokens;
  private AppResources resources;
  private AnchorListItem scrollToTop;
  @SuppressWarnings("SuspiciousNameCombination")
  @Inject
  MenuView(Binder binder,
           AppResources resources,
           Cookies cookies,
           NameTokens nameTokens) {
    initWidget(binder.createAndBindUi(this));

    this.nameTokens = nameTokens;
    this.resources = resources;
    this.cookies = cookies;

    int currentMonth = Integer.valueOf(DateTimeFormat.getFormat("M").format(new Date()));
    if (12 == currentMonth || 1 == currentMonth) {
      logo = new Image(resources.images().logoNewYear());
    } else {
      logo = new Image(resources.images().logo());
    }
    logo.setSize(Variables.S_LOGO_TOP_HEIGHT, Variables.S_LOGO_TOP_HEIGHT);
    brand.add(logo);

    scrollToTop = new AnchorListItem();
    scrollToTop.setIcon(IconType.ARROW_UP);
    scrollToTop.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Window.scrollTo(0, 0);
      }
    });

    Window.addWindowScrollHandler(new Window.ScrollHandler() {
      @Override
      public void onWindowScroll(Window.ScrollEvent event) {
        if (event.getScrollTop() == 0) {
          navbarTopHeight();
        } else {
          navbarScrollHeight();
        }
      }
    });
  }

  private void setLocale(String locale) {
    String hash = Window.Location.getHash();
    Window.Location.assign("/?locale=" + locale + hash);
    cookies.setLocale(locale);
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
    for (int i = 0; i < element.getChildCount(); i++) {
      final Element child = (Element) element.getChild(i);
      if (child.getTagName() != null && child.getTagName().toUpperCase().equals("IMG")) {
        continue;
      }
      if (top) {
        child.addClassName(resources.style().navbarElemTop());
        child.removeClassName(resources.style().navbarElemScroll());
      } else {
        child.addClassName(resources.style().navbarElemScroll());
        child.removeClassName(resources.style().navbarElemTop());
      }
    }
  }

  @Override
  protected void onAttach() {
    setLinks();
  }

  private void setLinks() {
    AnchorListItem anchor;
    if (getUiHandlers().isLoggedIn()) {
      for (final NameTokens.Link link : nameTokens.getLeftAuthLinks()) {
        anchor = createAnchor(link);
        navLeft.add(anchor);
      }
    } else {
      for (final NameTokens.Link link : nameTokens.getLeftLinks()) {
        anchor = createAnchor(link);
        navLeft.add(anchor);
      }
    }

    if (getUiHandlers().isLoggedIn()) {
      for (NameTokens.Link link : nameTokens.getRightAuthLinks()) {
        anchor = createAnchor(link);
        navRight.add(anchor);
      }
    } else {
      for (NameTokens.Link link : nameTokens.getRightLinks()) {
        anchor = createAnchor(link);
        navRight.add(anchor);
      }
    }

    anchor = new AnchorListItem();
    final Image ruImg = new Image(resources.images().russiaFlag().getSafeUri());
    ruImg.setResponsive(true);
    anchor.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        setLocale("ru");
      }
    });
    Anchor a = (Anchor) anchor.getWidget(0);
    a.setPaddingLeft(2);
    a.setPaddingRight(2);
    a.add(ruImg);
    navRight.add(anchor);

    anchor = new AnchorListItem();
    final Image enImg = new Image(resources.images().usFlag().getSafeUri());
    enImg.setResponsive(true);
    anchor.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        setLocale("en");
      }
    });
    a = (Anchor) anchor.getWidget(0);
    a.setPaddingLeft(2);
    a.setPaddingRight(2);
    a.add(enImg);
    navRight.add(anchor);

    highlightMenu();
    navbarTopHeight();
  }

  private void navbarScrollHeight() {
    final String navbarScrollHeight = Variables.S_NAVBAR_SCROLL_HEIGHT;
    navbar.getElement().addClassName(resources.style().navbarScroll());
    navbar.getElement().removeClassName(resources.style().navbarTop());
    setLinkHeight(navbarScrollHeight);
    brand.setHeight(navbarScrollHeight);
    logo.addStyleName(resources.style().logoScroll());
    logo.removeStyleName(resources.style().logoTop());
    navRight.insert(scrollToTop, 0);
  }

  private void navbarTopHeight() {
    final String navbarTopHeight = Variables.S_NAVBAR_TOP_HEIGHT;
    navbar.getElement().addClassName(resources.style().navbarTop());
    navbar.getElement().removeClassName(resources.style().navbarScroll());
    setLinkHeight(navbarTopHeight);
    brand.setHeight(navbarTopHeight);
    logo.addStyleName(resources.style().logoTop());
    logo.removeStyleName(resources.style().logoScroll());
    navRight.remove(scrollToTop);
  }

  private AnchorListItem createAnchor(final NameTokens.Link link) {
    final AnchorListItem anchor = new AnchorListItem(link.name);
    if (link.href != null) {
      anchor.setHref(link.href);
    }
    anchor.setIcon(link.iconType);
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
    String nameToken = cookies.getLocation();
    if (nameToken == null || nameToken.isEmpty()) {
      return;
    }
    for (Widget widget : navLeft) {
      if (setActiveAnchor(nameToken, (AnchorListItem) widget)) return;
    }
    for (Widget widget : navRight) {
      if (widget instanceof AnchorListItem) {
        if (setActiveAnchor(nameToken, (AnchorListItem) widget)) return;
      }
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