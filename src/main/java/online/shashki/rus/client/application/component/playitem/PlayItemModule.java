
package online.shashki.rus.client.application.component.playitem;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PlayItemModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenterWidget(PlayItemPresenter.class, PlayItemPresenter.MyView.class, PlayItemView.class);
    }
}
