
package online.shashki.rus.client.application.menu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;
import online.shashki.rus.client.place.NameTokens;
import online.shashki.rus.shared.locale.ShashkiMessages;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class MenuView extends ViewWithUiHandlers<MenuUiHandlers> implements MenuPresenter.MyView {
  private final ShashkiMessages messages;
  @UiField
  HTMLPanel panel;
  @UiField
  NavbarNav navLeft;
  @UiField
  NavbarNav navRight;

  private AnchorListItem prevAnchor;
  private NameTokens nameTokens;

  @Inject
  MenuView(Binder binder,
           ShashkiMessages messages,
           NameTokens nameTokens) {
    initWidget(binder.createAndBindUi(this));
    this.nameTokens = nameTokens;
    this.messages = messages;
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

    getUiHandlers().isAuthenticated(new AsyncCallback<Boolean>() {
      @Override
      public void onFailure(Throwable caught) {
        ErrorDialogBox.setMessage(messages.errorInAuthentication()).show();
      }

      @Override
      public void onSuccess(Boolean result) {
        if (result) {
          for (NameTokens.Link link : nameTokens.getRightAuthLinks()) {
            AnchorListItem anchor;
            if (!link.token.equals(NameTokens.logoutPage)) {
              anchor = createAnchor(link);
            } else {
              anchor = new AnchorListItem(link.name);
              anchor.setIcon(IconType.SIGN_OUT);
              anchor.setHref(link.token);
            }
            navRight.add(anchor);
          }
        } else {
          for (NameTokens.Link link : nameTokens.getRightLinks()) {
            AnchorListItem anchor = createAnchor(link);
            navRight.add(anchor);
          }
        }
      }
    });
  }

  private AnchorListItem createAnchor(final NameTokens.Link link) {
    final AnchorListItem anchor = new AnchorListItem(link.name);
    switch (link.token) {
      case NameTokens.homePage:
        anchor.setIcon(IconType.HOME);
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
    return anchor;
  }

  private void disableLink(AnchorListItem link) {
    if (link != null) {
      link.setActive(false);
    }
  }

  interface Binder extends UiBinder<HTMLPanel, MenuView> {
  }
}
