package online.shashki.rus.shared.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 31.12.14
 * Time: 16:13
 */
public interface Game extends PersistableObject {

  GameEnds getPlayEndStatus();

  void setPlayEndStatus(GameEnds playEndStatus);

  Date getPlayStartDate();

  void setPlayStartDate(Date playDate);

  Date getPlayEndDate();

  void setPlayEndDate(Date playEndDate);

  String getPartyNotation();

  void setPartyNotation(String partyNotation);

  Shashist getPlayerBlack();

  void setPlayerBlack(Shashist playerBlack);

  Shashist getPlayerWhite();

  void setPlayerWhite(Shashist playerWhite);
}
