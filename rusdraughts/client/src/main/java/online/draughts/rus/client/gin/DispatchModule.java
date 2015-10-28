package online.draughts.rus.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.gwtplatform.dispatch.rest.client.RestApplicationPath;
import com.gwtplatform.dispatch.rest.client.gin.RestDispatchAsyncModule;
import com.gwtplatform.dispatch.rpc.client.gin.RpcDispatchAsyncModule;
import com.gwtplatform.dispatch.shared.SecurityCookie;
import online.draughts.rus.shared.rest.ApiPaths;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.10.15
 * Time: 21:39
 */
public class DispatchModule extends AbstractGinModule {

  public static final String JSESSIONID = "JSESSIONID";

  @Override
  protected void configure() {
    install(new RestDispatchAsyncModule.Builder()
//        .interceptorRegistry(RestInterceptorRegistry.class)
        .build());
    install(new RpcDispatchAsyncModule.Builder()
//        .interceptorRegistry(RpcInterceptorRegistry.class)
        .build());

    bindConstant().annotatedWith(SecurityCookie.class).to(JSESSIONID);
    bindConstant().annotatedWith(RestApplicationPath.class).to(ApiPaths.ROOT);
  }
}
