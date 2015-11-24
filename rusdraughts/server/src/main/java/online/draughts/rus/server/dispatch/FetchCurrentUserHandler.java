package online.draughts.rus.server.dispatch;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import online.draughts.rus.server.dao.PlayerDao;
import online.draughts.rus.server.util.AuthUtils;
import online.draughts.rus.shared.dispatch.FetchCurrentPlayerAction;
import online.draughts.rus.shared.dispatch.FetchCurrentPlayerResult;
import online.draughts.rus.shared.model.Player;

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
  private final Provider<PlayerDao> playerDaoProvider;
  private final Provider<Boolean> authProvider;

  @Inject
  public FetchCurrentUserHandler(
      @Named(AuthUtils.AUTHENTICATED) Provider<Boolean> authProvider,
      Provider<HttpServletRequest> requestProvider,
      Provider<PlayerDao> playerDaoProvider) {
    this.requestProvider = requestProvider;
    this.playerDaoProvider = playerDaoProvider;
    this.authProvider = authProvider;
  }

  @Override
  public FetchCurrentPlayerResult execute(FetchCurrentPlayerAction fetchCurrentPlayerAction, ExecutionContext executionContext) throws ActionException {
    if (!authProvider.get()) {
      return new FetchCurrentPlayerResult(null);
    }
    HttpSession session = requestProvider.get().getSession();
    final Player bySessionId = playerDaoProvider.get().findBySessionId(session.getId());
    return new FetchCurrentPlayerResult(bySessionId);
  }

  @Override
  public Class<FetchCurrentPlayerAction> getActionType() {
    return FetchCurrentPlayerAction.class;
  }

  @Override
  public void undo(FetchCurrentPlayerAction fetchCurrentPlayerAction, FetchCurrentPlayerResult fetchCurrentPlayerResult, ExecutionContext executionContext) throws ActionException {
  }
}
