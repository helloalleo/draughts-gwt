package online.draughts.rus.client.application.mygames;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import online.draughts.rus.client.application.home.GamesPanelPresentable;
import online.draughts.rus.client.application.home.GamesPanelViewable;
import online.draughts.rus.client.application.home.PlayShowPanel;
import online.draughts.rus.client.application.home.ShowPanelEnum;
import online.draughts.rus.client.gin.PlayShowPanelFactory;
import online.draughts.rus.client.util.AdsUtils;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.dto.GameDto;
import org.gwtbootstrap3.client.ui.Button;

import javax.inject.Inject;
import java.util.List;


public class MyGamesView extends ViewWithUiHandlers<MyGamesUiHandlers>
    implements MyGamesPresenter.MyView, GamesPanelViewable {
  private final PlayShowPanel playShowPanel;
  private final Cookies cookies;

  @UiField
  HTMLPanel main;
  @UiField
  SimplePanel playShowSimplePanel;
  @UiField
  Button moreGamesInRow;
  @UiField
  Button lessGamesInRow;

  @Inject
  MyGamesView(Binder uiBinder,
              Cookies cookies,
              PlayShowPanelFactory playShowPanelFactory) {
    initWidget(uiBinder.createAndBindUi(this));

    playShowPanel = playShowPanelFactory.createShowPanel(ShowPanelEnum.MY_GAMES_PANE, this);
    playShowSimplePanel.add(playShowPanel);
    this.cookies = cookies;

    bindSlot(MyGamesPresenter.SLOT_MYGAME, main);
  }

  @Override
  public int setGames(List<GameDto> gameList) {
    return playShowPanel.setGames(gameList);
  }

  @Override
  public int addGames(List<GameDto> games) {
    return playShowPanel.addGames(games);
  }

  public void setEnableMoreGameButton(boolean enable) {
    moreGamesInRow.setEnabled(enable);
  }

  @Override
  public PlayShowPanel.PagingList getMoreGamesInRow(boolean forward, PlayShowPanel.PagingList gamesInRow) {
    PlayShowPanel.PagingList tmpList = forward ? gamesInRow.getNext() : gamesInRow.getPrev();
    if (null != tmpList) {
      gamesInRow = tmpList;
    }
    cookies.setMyGamesInRowNumber(gamesInRow.getNumInRow());
    return gamesInRow;
  }

  @Override
  public void getMoreGames(int newPageSize) {
    getUiHandlers().getMoreGames(newPageSize);
  }

  @Override
  public GamesPanelPresentable getPresenter() {
    return (GamesPanelPresentable) getUiHandlers();
  }

  public void setEnableLessGameButton(boolean enableMoreGameButton) {
    lessGamesInRow.setEnabled(enableMoreGameButton);
  }

  @Override
  protected void onAttach() {
    playShowPanel.init();
    AdsUtils.addAdvertisementFromGoogleScript();
  }

  @SuppressWarnings("unused")
  @UiHandler("moreGamesInRow")
  public void onMoreGameOnPage(ClickEvent event) {
    playShowPanel.moreGameOnPanel();
  }

  @SuppressWarnings("unused")
  @UiHandler("lessGamesInRow")
  public void onLessGameOnPage(ClickEvent event) {
    playShowPanel.lessGameOnPanel();
  }

  interface Binder extends UiBinder<Widget, MyGamesView> {
  }
}
