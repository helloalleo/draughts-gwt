package online.shashki.rus.server.dispatch;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import online.shashki.rus.shared.dispatch.FetchCurrentPlayerAction;
import online.shashki.rus.shared.dispatch.FetchCurrentPlayerResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.10.15
 * Time: 15:30
 */

public class FetchCurrentUserHandler implements ActionHandler<FetchCurrentPlayerAction, FetchCurrentPlayerResult> {

  private final Provider<HttpServletRequest> requestProvider;
//  private final Provider<PlayerDao> playerDaoProvider;

  @Inject
  public FetchCurrentUserHandler(Provider<HttpServletRequest> requestProvider) {
    this.requestProvider = requestProvider;
//    this.playerDaoProvider = playerDaoProvider;
  }

  @Override
  public FetchCurrentPlayerResult execute(FetchCurrentPlayerAction fetchCurrentPlayerAction, ExecutionContext executionContext) throws ActionException {
    HttpSession session = requestProvider.get().getSession();
    if (session != null) {
//      final Player bySessionId = playerDaoProvider.get().findBySessionId(session.getId());
//      System.out.println(bySessionId);
    }
    return null;
  }

  @Override
  public Class<FetchCurrentPlayerAction> getActionType() {
    return FetchCurrentPlayerAction.class;
  }

  @Override
  public void undo(FetchCurrentPlayerAction fetchCurrentPlayerAction, FetchCurrentPlayerResult fetchCurrentPlayerResult, ExecutionContext executionContext) throws ActionException {
  }
}
