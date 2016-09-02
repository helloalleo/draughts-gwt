package online.draughts.rus.client.application.common;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.event.GameOverEvent;
import online.draughts.rus.client.gin.DialogFactory;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.client.util.Logger;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import online.draughts.rus.shared.resource.GamesResource;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 24.04.16
 * Time: 18:54
 */
public class PlayComponentUtil {
  static void checkWin(EventBus eventBus, final PlaySession playSession, DraughtsMessages messages,
                       final DialogFactory dialogFactory, int myDraughtsSize, int opponentDraughtsSize,
                       boolean white, final ResourceDelegate<GamesResource> gamesDelegate) {
    GameDto.GameEnd gameEnd = null;
    final GameDto.GameType gameType = playSession.getGameType();
    if (0 == myDraughtsSize) {
      dialogFactory.createGameResultDialogBox(endGameMessage(messages, gameType, false)).show();
      if (white) {
        gameEnd = blackWin(gameType);
      } else {
        gameEnd = whiteWin(gameType);
      }
    }
    if (0 == opponentDraughtsSize) {
      dialogFactory.createGameResultDialogBox(endGameMessage(messages, gameType, true)).show();
      if (white) {
        gameEnd = whiteWin(gameType);
      } else {
        gameEnd = blackWin(gameType);
      }
    }

    final GameDto endGame = playSession.getGame();
    if (gameEnd == null) {
      return;
    }
    Logger.debug("1 " + gameEnd);
    endGame.setPlayEndStatus(gameEnd);
    eventBus.fireEvent(new GameOverEvent(endGame, new AbstractAsyncCallback<GameDto>(dialogFactory) {
      @Override
      public void onSuccess(GameDto result) {
      }
    }));
  }

  private static String endGameMessage(DraughtsMessages messages, GameDto.GameType gameType, boolean win) {
    switch (gameType) {
      case DRAUGHTS:
        return win ? messages.youWon() : messages.youLose();
      case GIVEAWAY:
        return !win ? messages.youWon() : messages.youLose();
      default:
        return "";
    }
  }

  private static GameDto.GameEnd whiteWin(GameDto.GameType gameType) {
    switch (gameType) {
      case DRAUGHTS:
        return GameDto.GameEnd.WHITE_WIN;
      case GIVEAWAY:
        return GameDto.GameEnd.BLACK_WIN;
      default:
        return GameDto.GameEnd.WHITE_WIN;
    }
  }

  private static GameDto.GameEnd blackWin(GameDto.GameType gameType) {
    switch (gameType) {
      case DRAUGHTS:
        return GameDto.GameEnd.BLACK_WIN;
      case GIVEAWAY:
        return GameDto.GameEnd.WHITE_WIN;
      default:
        return GameDto.GameEnd.BLACK_WIN;
    }
  }

  static void checkShut(EventBus eventBus, final PlaySession playSession,
                        DraughtsMessages messages, final DialogFactory dialogFactory, boolean isWhite,
                        final ResourceDelegate<GamesResource> gamesDelegate) {
    final GameDto.GameEnd gameEnd;
    final GameDto.GameType gameType = playSession.getGameType();
    if (isWhite == playSession.isPlayerHasWhiteColor()) {
      dialogFactory.createGameResultDialogBox(endGameMessage(messages, gameType, false)).show();
    } else {
      dialogFactory.createGameResultDialogBox(endGameMessage(messages, gameType, true)).show();
    }
    if (isWhite) {
      gameEnd = blackWin(playSession.getGameType());
    } else {
      gameEnd = whiteWin(playSession.getGameType());
    }
    final GameDto endedGame = playSession.getGame();
    endedGame.setPlayEndStatus(gameEnd);
    Logger.debug("1s " + gameEnd);
    eventBus.fireEvent(new GameOverEvent(endedGame, new AbstractAsyncCallback<GameDto>(dialogFactory) {
      @Override
      public void onSuccess(GameDto result) {
      }
    }));
  }

  public static void updatePlayerStat(ResourceDelegate<GamesResource> gamesDelegate, final DialogFactory dialogFactory,
                                      final PlaySession playerSession, GameDto game) {
    gamesDelegate.withCallback(new AbstractAsyncCallback<PlayerDto>(dialogFactory) {
      @Override
      public void onSuccess(PlayerDto playerDto) {
        playerSession.getPlayer().updateSerializable(playerDto);
      }
    }).updatePlayerStat(game.getId(), playerSession.getPlayer().getId());
  }
}
