package online.shashki.rus.client.websocket;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 27.09.15
 * Time: 10:46
 */
public class WebsocketModule extends AbstractPresenterModule {

  @Override
  protected void configure() {
    bind(GameWebsocket.class).asEagerSingleton();
  }
}
