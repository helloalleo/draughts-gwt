package online.draughts.rus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.01.15
 * Time: 20:53
 */
public interface CheckWinnerEventHandler extends EventHandler {
  void onCheckWinner(CheckWinnerEvent event);
}
