//package online.shashki.rus.server.service;
//
//import com.google.gwt.user.server.rpc.RemoteServiceServlet;
//import com.google.inject.Inject;
//import com.google.inject.Provider;
//import com.google.inject.Singleton;
//import online.shashki.rus.server.dao.GameMessageDao;
//import online.shashki.rus.shared.model.GameMessage;
//import online.shashki.rus.shared.service.GameMessageService;
//
//import java.util.List;
//
///**
// * Created with IntelliJ IDEA.
// * User: alekspo
// * Date: 30.12.14
// * Time: 9:31
// */
//@Singleton
//public class GameMessageServiceImpl extends RemoteServiceServlet implements GameMessageService {
//
//  private final GameMessageDao gameMessageDao;
//
//  @Inject
//  public GameMessageServiceImpl(Provider<GameMessageDao> gameMessageDaoProvider) {
//    this.gameMessageDao = gameMessageDaoProvider.get();
//  }
//
//  @Override
//  public List<GameMessage> getLastPlayerMessages(int countLast, Long playerId, Long opponentId) {
//    return gameMessageDao.findLastMessages(countLast, playerId, opponentId);
//  }
//

//}