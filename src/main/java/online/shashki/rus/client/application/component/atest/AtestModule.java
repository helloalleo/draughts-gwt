
package online.shashki.rus.client.application.component.atest;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AtestModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindSingletonPresenterWidget(AtestPresenter.class, AtestPresenter.MyView.class, AtestView.class);
    }
}
