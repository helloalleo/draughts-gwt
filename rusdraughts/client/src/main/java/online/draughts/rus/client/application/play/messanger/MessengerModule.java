package online.draughts.rus.client.application.play.messanger;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class MessengerModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenterWidget(MessengerPresenter.class, MessengerPresenter.MyView.class, MessengerView.class);
  }
}
