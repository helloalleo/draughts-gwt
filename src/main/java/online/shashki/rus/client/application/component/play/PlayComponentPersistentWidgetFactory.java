package online.shashki.rus.client.application.component.play;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 20.09.15
 * Time: 16:47
 */
public class PlayComponentPersistentWidgetFactory {

    private final AsyncProvider<PlayComponentPresenter> provider;

    @Inject
    public PlayComponentPersistentWidgetFactory( AsyncProvider<PlayComponentPresenter> provider ) {
      this.provider = provider;
    }

    public AsyncProvider<PlayComponentPresenter> getProvider() {
      return provider;
    }
}
