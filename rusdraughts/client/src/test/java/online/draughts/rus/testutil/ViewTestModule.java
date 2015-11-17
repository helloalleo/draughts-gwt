package online.draughts.rus.testutil;

import com.google.gwt.junit.GWTMockUtilities;
import com.gwtplatform.tester.MockFactory;
import online.draughts.rus.shared.locale.DraughtsMessages;
import org.jukito.JukitoModule;
import org.jukito.TestSingleton;

public abstract class ViewTestModule extends JukitoModule {

  @Override
  protected void configureTest() {
    GWTMockUtilities.disarm();

    bind(MockFactory.class).to(MockitoMockFactory.class);

    configureViewTest();
  }

  protected abstract void configureViewTest();
}