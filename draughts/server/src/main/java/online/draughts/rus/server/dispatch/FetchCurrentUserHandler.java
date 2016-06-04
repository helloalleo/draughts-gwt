package online.draughts.rus.server.dispatch;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.service.PlayerService;
import online.draughts.rus.server.util.AuthUtils;
import online.draughts.rus.shared.dispatch.FetchCurrentPlayerAction;
import online.draughts.rus.shared.dispatch.FetchCurrentPlayerResult;
import online.draughts.rus.shared.dto.PlayerDto;
import org.apache.log4j.Logger;
import org.dozer.Mapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.10.15
 * Time: 15:30
 */

public class FetchCurrentUserHandler implements ActionHandler<FetchCurrentPlayerAction, FetchCurrentPlayerResult> {

  private final Logger logger = Logger.getLogger(FetchCurrentUserHandler.class);
  private final Provider<HttpServletRequest> requestProvider;
  private final Provider<Boolean> authProvider;
  private final PlayerService playerService;
  private final Mapper mapper;

  @Inject
  public FetchCurrentUserHandler(
      @Named(AuthUtils.AUTHENTICATED) Provider<Boolean> authProvider,
      Provider<HttpServletRequest> requestProvider,
      PlayerService playerService, Mapper mapper) {
    this.requestProvider = requestProvider;
    this.authProvider = authProvider;
    this.playerService = playerService;
    this.mapper = mapper;
  }

  @Override
  public FetchCurrentPlayerResult execute(FetchCurrentPlayerAction fetchCurrentPlayerAction, ExecutionContext executionContext) throws ActionException {
    try {
      if (!authProvider.get()) {
        return new FetchCurrentPlayerResult(null);
      }
      HttpSession session = requestProvider.get().getSession();
      final Player bySessionId = playerService.findBySessionId(session.getId());
      if (bySessionId != null && bySessionId.isBanned()) {
        throw new RuntimeException("You were banned");
      } else if (bySessionId == null) {
        return new FetchCurrentPlayerResult(null);
      }
      PlayerDto dto = mapper.map(bySessionId, PlayerDto.class);
      return new FetchCurrentPlayerResult(dto);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new FetchCurrentPlayerResult(null);
    }
  }

  @Override
  public Class<FetchCurrentPlayerAction> getActionType() {
    return FetchCurrentPlayerAction.class;
  }

  @Override
  public void undo(FetchCurrentPlayerAction fetchCurrentPlayerAction, FetchCurrentPlayerResult fetchCurrentPlayerResult, ExecutionContext executionContext) throws ActionException {
  }
}
