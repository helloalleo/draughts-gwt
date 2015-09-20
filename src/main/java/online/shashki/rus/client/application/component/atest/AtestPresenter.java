
package online.shashki.rus.client.application.component.atest; 

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class AtestPresenter extends PresenterWidget<AtestPresenter.MyView> implements AtestUiHandlers {
    interface MyView extends View, HasUiHandlers<AtestUiHandlers> {
    }

    @Inject
    AtestPresenter(EventBus eventBus, MyView view) {
        super(eventBus, view);

        getView().setUiHandlers(this);
    }

}
