package online.draughts.rus.client.gin;

import online.draughts.rus.client.application.widget.dialog.DraughtsPlayer;
import online.draughts.rus.shared.model.Game;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 29.10.15
 * Time: 5:05
 */
public interface DraughtsPlayerFactory {

  DraughtsPlayer create(Game game);
}
