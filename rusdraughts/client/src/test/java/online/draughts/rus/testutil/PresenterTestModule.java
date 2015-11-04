package online.draughts.rus.testutil;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.testing.CountingEventBus;
import com.gwtplatform.mvp.client.AutobindDisable;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.config.ClientConfigurationImpl;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.locale.DraughtsMessagesImpl;
import org.jukito.JukitoModule;
import org.jukito.TestSingleton;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 15:34
 */
public abstract class PresenterTestModule extends JukitoModule {

  @Override
  protected void configureTest() {
    GWTMockUtilities.disarm();
    bind(EventBus.class).to(CountingEventBus.class).in(TestSingleton.class);
    bind(ClientConfiguration.class).to(ClientConfigurationImpl.class).in(TestSingleton.class);
    bind(DraughtsMessages.class).to(DraughtsMessagesImpl.class).in(TestSingleton.class);

    configurePresenterTest();

    bind(AutobindDisable.class).toInstance(new AutobindDisable(true));
  }

  abstract protected void configurePresenterTest();

}
