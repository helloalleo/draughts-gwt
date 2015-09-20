
package online.shashki.rus.client.application.component.atest;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class AtestView extends ViewWithUiHandlers<AtestUiHandlers> implements AtestPresenter.MyView {
    interface Binder extends UiBinder<HTMLPanel, AtestView> {
    }

    @UiField
    HTMLPanel panel;

    @Inject
    AtestView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }
}
