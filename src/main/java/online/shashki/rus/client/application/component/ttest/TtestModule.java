package online.shashki.rus.client.application.component.ttest;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class TtestModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(TtestPresenter.class, TtestPresenter.MyView.class, TtestView.class, TtestPresenter.MyProxy.class);
    }
}
