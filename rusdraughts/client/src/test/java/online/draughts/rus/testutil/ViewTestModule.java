package online.draughts.rus.testutil;

import com.google.gwt.junit.GWTMockUtilities;
import com.gwtplatform.tester.MockFactory;
import org.jukito.JukitoModule;
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

    configureViewTest();
  }

  protected abstract void configureViewTest();

}