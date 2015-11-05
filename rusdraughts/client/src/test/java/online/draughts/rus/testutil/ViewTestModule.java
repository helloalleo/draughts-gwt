package online.draughts.rus.testutil;

import com.google.gwt.junit.GWTMockUtilities;
import com.gwtplatform.tester.MockFactory;
import online.draughts.rus.shared.config.ClientConfiguration;
import online.draughts.rus.shared.config.ClientConfigurationImpl;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.locale.DraughtsMessagesImpl;
import org.jukito.JukitoModule;
import org.jukito.TestSingleton;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

public abstract class ViewTestModule extends JukitoModule {

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Override
  protected void configureTest() {
    GWTMockUtilities.disarm();

    bind(MockFactory.class).to(MockitoMockFactory.class);
    bind(ClientConfiguration.class).to(ClientConfigurationImpl.class).in(TestSingleton.class);
    bind(DraughtsMessages.class).to(DraughtsMessagesImpl.class).in(TestSingleton.class);

    configureViewTest();
  }

  protected abstract void configureViewTest();

}