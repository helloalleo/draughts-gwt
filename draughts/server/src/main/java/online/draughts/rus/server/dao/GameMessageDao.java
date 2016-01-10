//package online.draughts.rus.server.dao;
//
//import online.draughts.rus.server.domain.GameMessage;
//import online.draughts.rus.server.domain.Move;
//
//import java.util.List;
//
///**
// * Created with IntelliJ IDEA.
// * User: alekspo
// * Date: 31.12.14
// * Time: 18:04
// */
//public interface GameMessageDao extends Dao<GameMessage> {
//
//  List<GameMessage> findLastMessages(int countLast, Long playerId, Long opponentId);
//
//  List<GameMessage> findGameMessagesByGameId(Long gameId);
//
//  List<Move> findGameMoves(Long gameId);
//
//  Move saveMove(Move move);
//}
