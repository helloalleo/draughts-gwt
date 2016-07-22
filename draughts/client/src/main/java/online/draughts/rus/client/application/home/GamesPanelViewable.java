package online.draughts.rus.client.application.home;

import online.draughts.rus.shared.dto.GameDto;

/**
 * Created by aleksey on 22.07.16.
 */
public interface GamesPanelViewable {
    void getMoreGames(int newPageSize);

    GamesPanelPresentable getPresenter();

    void setEnableLessGameButton(boolean enable);

    void setEnableMoreGameButton(boolean enable);

    PlayShowPanel.PagingList getMoreGamesInRow(boolean forward, PlayShowPanel.PagingList gamesInRow);

    void updateGames();

    void removeGame(GameDto gameDto);
}
