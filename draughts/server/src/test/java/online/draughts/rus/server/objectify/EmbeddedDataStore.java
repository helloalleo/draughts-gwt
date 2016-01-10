package online.draughts.rus.server.objectify;

import com.google.appengine.tools.development.testing.*;
import org.junit.rules.ExternalResource;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.12.15
 * Time: 15:36
 */
public class EmbeddedDataStore extends ExternalResource {
  private static LocalServiceTestHelper helper;

  @Override
  protected void before() throws Throwable {
    helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
        new LocalBlobstoreServiceTestConfig(), new LocalTaskQueueTestConfig(),
        new LocalMemcacheServiceTestConfig());
    helper.setUp();
  }

  @Override
  protected void after() {
    helper.tearDown();
  }
}