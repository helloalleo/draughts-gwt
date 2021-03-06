package online.draughts.rus.client.application;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.tester.MockFactory;
import com.gwtplatform.tester.MockingBinder;
import online.draughts.rus.testutil.SimpleTestCase;
import online.draughts.rus.testutil.ViewTestModule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 15:23
 */
public class ApplicationViewTest extends SimpleTestCase {

  public static class Module extends ViewTestModule {

    static class MyTestBinder extends MockingBinder<Widget, ApplicationView> implements ApplicationView.Binder {
      @Inject
      public MyTestBinder(final MockFactory mockitoMockFactory) {
        super(Widget.class, mockitoMockFactory);
      }
    }

    @Override
    protected void configureViewTest() {
      bind(ApplicationView.Binder.class).to(MyTestBinder.class);
    }
  }

  @Inject
  ApplicationView applicationView;

  @Test
  public void testView() {
    assertNotNull(applicationView);
  }

}