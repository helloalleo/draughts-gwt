package online.shashki.rus.shared.dto;

import com.google.gwt.safehtml.shared.SafeHtml;
import online.shashki.rus.shared.model.Game;
import online.shashki.rus.shared.model.GameEnds;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 25.11.14
 * Time: 18:07
 */
public class PlayDto {
  private String playerWhite;
  private String playerBlack;
  private SafeHtml deskHtml;
  private GameEnds gameEnd;

  public PlayDto() {
  }

  public PlayDto(Game game) {
    this.playerWhite = game.getPlayerWhite().getPublicName();
    this.playerBlack = game.getPlayerBlack().getPublicName();
    this.gameEnd = game.getPlayEndStatus();
  }

  public String getPlayerWhite() {
    return playerWhite;
  }

  public void setPlayerWhite(String playerWhite) {
    this.playerWhite = playerWhite;
  }

  public String getPlayerBlack() {
    return playerBlack;
  }

  public void setPlayerBlack(String playerBlack) {
    this.playerBlack = playerBlack;
  }

  public SafeHtml getDeskHtml() {
    return deskHtml;
  }

  public void setDeskHtml(SafeHtml deskHtml) {
    this.deskHtml = deskHtml;
  }

  public String getBlackPlayer() {
    return playerBlack;
  }

  public String getWhitePlayer() {
    return playerWhite;
  }

  public GameEnds getGameEnd() {
    return gameEnd;
  }

  public void setGameEnd(GameEnds gameEnd) {
    this.gameEnd = gameEnd;
  }
}
