package online.draughts.rus.server.guice;

import com.gwtplatform.dispatch.rpc.server.guice.DispatchModule;
import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import online.draughts.rus.server.dispatch.FetchCurrentUserHandler;
import online.draughts.rus.shared.dispatch.FetchCurrentPlayerAction;

/**
 * Module which binds the handlers and configurations.
 */
public class ServerHandlerModule extends HandlerModule {
  @Override
  protected void configureHandlers() {
    install(new DispatchModule());

    bindHandler(FetchCurrentPlayerAction.class, FetchCurrentUserHandler.class);
  }
}
