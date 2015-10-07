package online.shashki.rus.client.application.security;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class OAuthLoginModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(OAuthLoginPresenter.class, OAuthLoginPresenter.MyView.class, OAuthLoginView.class, OAuthLoginPresenter.MyProxy.class);
    }
}
