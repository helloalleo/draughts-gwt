
package online.shashki.rus.client.application.component.playrow;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import online.shashki.rus.client.application.component.playitem.PlayItemModule;

public class PlayRowModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bindPresenterWidget(PlayRowPresenter.class, PlayRowPresenter.MyView.class, PlayRowView.class);
  }
}
