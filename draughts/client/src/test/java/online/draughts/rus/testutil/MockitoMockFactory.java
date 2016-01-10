package online.draughts.rus.testutil;

import com.gwtplatform.tester.MockFactory;
import org.mockito.Mockito;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 11:04
 */
public class MockitoMockFactory implements MockFactory {

  @Override
  public <T> T mock(Class<T> classToMock) {
    return Mockito.mock(classToMock);
  }
}