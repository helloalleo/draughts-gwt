//package online.shashki.rus.clnt.client.application;
//
//import online.shashki.rus.clnt.online.shashki.rus.shared.model.Game;
//import online.shashki.rus.clnt.online.shashki.rus.shared.model.Shashist;
//
//import java.util.List;
//
///**
// * Created with IntelliJ IDEA.
// * User: alekspo
// * Date: 21.12.14
// * Time: 12:29
// */
//public class ClientFactoryImpl implements ClientFactory {
//
//    private Shashist player;
//    private Shashist opponent;
//    private List<Shashist> playerList;
//    private Game game;
//    private boolean connected;
//
//    @Override
//    public boolean isConnected() {
//        return connected;
//    }
//
//    @Override
//    public void setConnected(boolean connected) {
//        this.connected = connected;
//    }
//
//    @Override
//    public boolean isPlayerHasWhiteColor() {
//        return player != null && game != null && game.getPlayerWhite().getSystemId().equals(player.getSystemId());
//    }
//
//    @Override
//    public Shashist getPlayer() {
//        return player;
//    }
//
//    @Override
//    public void setPlayer(Shashist player) {
//        this.player = player;
//    }
//
//    @Override
//    public List<Shashist> getPlayerList() {
//        return playerList;
//    }
//
//    @Override
//    public void setPlayerList(List<Shashist> playerList) {
//        this.playerList = playerList;
//    }
//
//    @Override
//    public Shashist getOpponent() {
//        return opponent;
//    }
//
//    @Override
//    public void setOpponent(Shashist opponent) {
//        this.opponent = opponent;
//    }
//
//    @Override
//    public Game find() {
//        return game;
//    }
//
//    @Override
//    public void setGame(Game game) {
//        this.game = game;
//    }
//
//}
