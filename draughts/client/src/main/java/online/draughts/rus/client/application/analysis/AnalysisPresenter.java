package online.draughts.rus.client.application.analysis;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import online.draughts.rus.client.application.ApplicationPresenter;
import online.draughts.rus.client.application.common.PlayComponentPresenter;
import online.draughts.rus.client.application.widget.dialog.GameResultDialogBox;
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.event.GameOverEvent;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.AbstractAsyncCallback;
import online.draughts.rus.client.util.Cookies;
import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.locale.DraughtsMessages;


public class AnalysisPresenter extends Presenter<AnalysisPresenter.MyView, AnalysisPresenter.MyProxy>
    implements AnalysisUiHandlers {
  public static final PermanentSlot<PlayComponentPresenter> SLOT_ANALYSIS = new PermanentSlot<>();
  private static final int DRAUGHTS_ON_DESK_INIT = 12;
  private final PlaySession playSession;
  private final DraughtsMessages messages;

  @Inject
  AnalysisPresenter(
      EventBus eventBus,
      MyView view,
      MyProxy proxy,
      PlaySession playSession,
      DraughtsMessages messages,
      Cookies cookies) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

    getView().setUiHandlers(this);
    this.playSession = playSession;
    this.messages = messages;

    cookies.setLocation(NameTokens.analysisPage);
  }

  @Override
  public boolean isMyTurn() {
    return false;
  }

  @Override
  public void checkWinner() {
    getView().setBeatenMy(DRAUGHTS_ON_DESK_INIT - getView().getMyDraughtsSize());
    getView().setBeatenOpponent(DRAUGHTS_ON_DESK_INIT - getView().getOpponentDraughtsSize());
    final GameDto endGame = playSession.getGame();
    GameDto.GameEnds gameEnd = null;
    if (0 == getView().getMyDraughtsSize()) {
      GameResultDialogBox.setMessage(messages.youLose(), getEventBus()).show();
      if ((getView().isWhite())) {
        gameEnd = GameDto.GameEnds.BLACK_WIN;
      } else {
        gameEnd = GameDto.GameEnds.WHITE_WIN;
      }
    }
    if (0 == getView().getOpponentDraughtsSize()) {
      GameResultDialogBox.setMessage(messages.youWon(), getEventBus()).show();
      if (getView().isWhite()) {
        gameEnd = GameDto.GameEnds.WHITE_WIN;
      } else {
        gameEnd = GameDto.GameEnds.BLACK_WIN;
      }
    }
    if (gameEnd == null) {
      return;
    }
    fireEvent(new GameOverEvent(endGame, gameEnd, new AbstractAsyncCallback<GameDto>() {
      @Override
      public void onSuccess(GameDto result) {
      }
    }));
  }

  interface MyView extends View, HasUiHandlers<AnalysisUiHandlers> {
    int getMyDraughtsSize();

    int getOpponentDraughtsSize();

    void setBeatenMy(int num);

    void setBeatenOpponent(int num);

    boolean isWhite();
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.analysisPage)
  @NoGatekeeper
  interface MyProxy extends ProxyPlace<AnalysisPresenter> {
  }
}
