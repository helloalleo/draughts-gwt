package online.draughts.rus.client.application.coach;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CoachRoomModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenter(CoachRoomPresenter.class, CoachRoomPresenter.MyView.class, CoachRoomView.class, CoachRoomPresenter.MyProxy.class);
  }
}
