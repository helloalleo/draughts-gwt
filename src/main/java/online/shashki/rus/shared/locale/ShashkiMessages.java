package online.shashki.rus.shared.locale;

import com.google.gwt.i18n.client.Messages;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 23.11.14
 * Time: 20:13
 */
public interface ShashkiMessages extends Messages {

  String home();

  String homeToken();

  String play();

  String playToken();

  String unrecognizedPlace();

  String signIn();

  String login();

  String logout();

  String jumbotronGreeting();

  String jumbotronSubGreeting();

  String playTape();

  String playTapeToken();

  String profile();

  String profileToken();

  String myPage();

  String settings();

  String settingsToken();

  String webSocketDoesNotSupport();

  String reconnect();

  String close();

  String error();

  String info();

  String captionGame();

  String next();

  String cancel();

  String chooseYourColor();

  String white();

  String black();

  String waitResponse();

  String yes();

  String no();

  String confirm();

  String inviteMessage(String inviting, String color);

  String inviteToPlay(String opponent, String playType);

  String draughts();

  String atFirstStartPlay();

  String aboutUs();

  String aboutUsToken();

  String profileUpdated();

  String errorWhileProfileUpdate();

  String tooLongPlayerName();

  String playStartDescription();

  String playRestartDescription();

  String invalidCharsInName();

  String tooShortPlayerName();

  String playerRejectedPlayRequest(String sender);

  String failToStartGame();

  String errorWhileGettingProfile();

  String errorWhileGettingGame();

  String selectPlayer();

  String selectAnotherPlayerItsYou();

  String yourTurn();

  String opponentTurn();

  String youDisconnected();

  String whites();

  String blacks();

  String youLose();

  String youWon();

  String playDidNotStart();

  String areYouSureYouWantSurrender();

  String opponentSurrendered();

  String doYouWantToProposeDraw();

  String errorWhileSavingGame();

  String playerProposesDraw(String senderName);

  String playerRejectedDraw(String senderName);

  String doYouWantToCancelMove();

  String playerProposesCancelMove(String publicName);

  String playerRejectedMoveCancel(String publicName);

  String youDontMove();

  String opponentLeftGame();

  String playAlreadyPlaying(String publicName);

  String playViewTitle(String whitePlayer, String blackPlayer, String winner);

  String BLACK_WON();

  String WHITE_WON();

  String BLACK_LEFT();

  String WHITE_LEFT();

  String SURRENDER_BLACK();

  String SURRENDER_WHITE();

  String DRAW();

  String contacts();

  String contactsToken();

  String errorToken();

  String myGames();

  String playingTitle();

  String onlineTitle();

  String offlineTitle();

  String errorInAuthentication();

  String opponentNotFound();

  String failToConnectToServer();
}
