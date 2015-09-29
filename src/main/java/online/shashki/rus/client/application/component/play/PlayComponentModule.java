package online.shashki.rus.client.application.component.play;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.shashki.rus.client.application.component.playrow.PlayRowModule;

public class PlayComponentModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    install(new PlayRowModule());

    bindSingletonPresenterWidget(PlayComponentPresenter.class, PlayComponentPresenter.MyView.class,
        PlayComponentView.class);
  }
}
