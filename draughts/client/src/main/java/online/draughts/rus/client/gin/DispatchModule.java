package online.draughts.rus.client.gin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.gwtplatform.dispatch.rest.client.RestApplicationPath;
import com.gwtplatform.dispatch.rest.client.gin.RestDispatchAsyncModule;
import com.gwtplatform.dispatch.rpc.client.gin.RpcDispatchAsyncModule;
import com.gwtplatform.dispatch.shared.SecurityCookie;
import online.draughts.rus.shared.resource.ApiPaths;

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
        .build());
    install(new RpcDispatchAsyncModule.Builder()
        .build());

    bindConstant().annotatedWith(SecurityCookie.class).to(JSESSIONID);
  }

  @Provides
  @RestApplicationPath
  String getApplicationPath() {
    String baseUrl = GWT.getHostPageBaseURL();
    if (baseUrl.endsWith("/")) {
      baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
    }

    return baseUrl + ApiPaths.RESOURCE;
  }
}
