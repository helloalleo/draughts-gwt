package online.draughts.rus.client.application.home;

/**
 * Created by aleksey on 22.07.16.
 */
public interface GamesPanelViewable {
    void getMoreGames(int newPageSize);

    GamesPanelPresentable getPresenter();

    void setEnableLessGameButton(boolean enable);

    void setEnableMoreGameButton(boolean enable);

    int getMoreGamesInRow(boolean forward, PlayShowPanel.PagingList gamesInRow);
}
