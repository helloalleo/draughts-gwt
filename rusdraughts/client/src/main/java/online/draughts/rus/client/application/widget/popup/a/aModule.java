package online.draughts.rus.client.application.widget.popup.a;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class aModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenterWidget(aPresenter.class, aPresenter.MyView.class, aView.class);
  }
}
