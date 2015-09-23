//package online.shashki.rus.server.rpc;
//
//import com.google.gwt.user.server.rpc.RemoteServiceServlet;
//import online.shashki.rus.client.rpc.GameMessageRpcService;
//import online.shashki.rus.server.service.GameMessageService;
//import online.shashki.rus.shared.model.GameMessage;
//
//import javax.inject.Inject;
//import java.util.List;
//
///**
// * Created with IntelliJ IDEA.
// * User: alekspo
// * Date: 30.12.14
// * Time: 9:31
// */
//public class GameMessageRpcServiceImpl extends RemoteServiceServlet implements GameMessageRpcService {
//  @Inject
//  private GameMessageService gameMessageService;
//
//  @Override
//  public List<GameMessage> getLastPlayerMessages(int countLast, Long playerId, Long opponentId) {
//    return gameMessageService.findLastMessages(countLast, playerId, opponentId);
//  }
//}