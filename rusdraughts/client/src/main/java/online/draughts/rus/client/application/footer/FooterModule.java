
package online.draughts.rus.client.application.footer;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class FooterModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindSingletonPresenterWidget(FooterPresenter.class, FooterPresenter.MyView.class, FooterView.class);
  }
}
