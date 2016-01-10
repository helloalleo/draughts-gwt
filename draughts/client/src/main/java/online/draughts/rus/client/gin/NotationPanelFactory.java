package online.draughts.rus.client.gin;

import online.draughts.rus.client.application.widget.NotationPanel;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 16.11.15
 * Time: 10:50
 */
public interface NotationPanelFactory {

  NotationPanel createNotationPanel(Long gameId);
}
