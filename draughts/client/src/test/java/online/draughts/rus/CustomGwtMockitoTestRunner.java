package online.draughts.rus;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.runners.model.InitializationError;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 06.11.15
 * Time: 19:54
 */
public class CustomGwtMockitoTestRunner extends GwtMockitoTestRunner {

  public CustomGwtMockitoTestRunner(Class<?> unitTestClass) throws InitializationError {
    super(unitTestClass);
  }

  @Override
  protected Collection<Class<?>> getClassesToStub() {
    Collection<Class<?>> classes = super.getClassesToStub();
    classes.add(LienzoPanel.class);
    classes.add(Layer.class);
    return classes;
  }
}
