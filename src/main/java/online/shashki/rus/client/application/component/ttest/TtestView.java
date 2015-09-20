package online.shashki.rus.client.application.component.ttest;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;


public class TtestView extends ViewWithUiHandlers<TtestUiHandlers> implements TtestPresenter.MyView {
    interface Binder extends UiBinder<Widget, TtestView> {
    }

    @UiField
    SimplePanel main;

    @Inject
    TtestView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));

//        bindSlot(TtestPresenter.SLOT_TTEST, main);
    }
}
