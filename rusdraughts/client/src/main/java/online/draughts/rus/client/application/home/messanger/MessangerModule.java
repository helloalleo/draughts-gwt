package online.draughts.rus.client.application.home.messanger;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class MessangerModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenterWidget(MessangerPresenter.class, MessangerPresenter.MyView.class, MessangerView.class);
  }
}
