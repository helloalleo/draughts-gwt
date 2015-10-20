package online.shashki.rus.server.rest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import online.shashki.rus.server.dao.GameMessageDao;
import online.shashki.rus.server.utils.AuthUtils;
import online.shashki.rus.shared.model.GameMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Singleton
public class GameMessagesResourceImpl {
  private Logger logger;
  private Provider<GameMessageDao> gameMessageDaoProvider;
  private Provider<HttpServletRequest> requestProvider;

  public GameMessagesResourceImpl() {
  }

  @Inject
  GameMessagesResourceImpl(
      Logger logger,
      Provider<GameMessageDao> gameDaoProvider,
      Provider<HttpServletRequest> requestProvider) {
    this.logger = logger;
    this.gameMessageDaoProvider = gameDaoProvider;
    this.requestProvider = requestProvider;
  }

  public void saveOrCreate(GameMessage gameMessage) {
    if (gameMessage == null) {
      return;
    }
    if (!AuthUtils.isAuthenticated(requestProvider.get().getSession())) {
      throw new RuntimeException("Not authorized");
    }

    logger.info("New message: " + gameMessage.toString());
    if (gameMessage.getId() == null) {
      gameMessageDaoProvider.get().create(gameMessage);
    } else {
      gameMessageDaoProvider.get().edit(gameMessage);
    }
  }
 }
