package online.draughts.rus.testutil;

import com.ait.lienzo.client.core.shape.Rectangle;
import com.google.gwt.junit.GWTMockUtilities;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.Element;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.testing.CountingEventBus;
import com.gwtplatform.mvp.client.AutobindDisable;
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
    bind(DraughtsMessages.class).to(DraughtsMessagesImpl.class).in(TestSingleton.class);

    forceMock(UIObject.class);
    forceMock(Element.class);
    forceMock(Rectangle.class);
    configurePresenterTest();

    bind(AutobindDisable.class).toInstance(new AutobindDisable(true));
  }

  abstract protected void configurePresenterTest();

}
