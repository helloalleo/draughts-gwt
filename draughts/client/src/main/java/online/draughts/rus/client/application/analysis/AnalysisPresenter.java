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
import online.draughts.rus.client.channel.PlaySession;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.client.util.Cookies;
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

    cookies.setLocation(NameTokens.ANALYSIS_PAGE);
  }

  @Override
  public boolean isMyTurn() {
    return false;
  }

  @Override
  public void checkWinner() {
    getView().setBeatenMy(DRAUGHTS_ON_DESK_INIT - getView().getMyDraughtsSize());
    getView().setBeatenOpponent(DRAUGHTS_ON_DESK_INIT - getView().getOpponentDraughtsSize());
//    PlayComponentUtil.checkWin(getEventBus(), playSession, messages, dialogFactory, getView().getMyDraughtsSize(),
//        getView().getOpponentDraughtsSize(), getView().isWhite());
  }

  @Override
  public void gameShut(boolean isWhite) {
//    PlayComponentUtil.checkShut(getEventBus(), playSession, messages, dialogFactory, isWhite);
  }

  public interface MyView extends View, HasUiHandlers<AnalysisUiHandlers> {
    void gameShut(boolean isWhite);

    int getMyDraughtsSize();

    int getOpponentDraughtsSize();

    void setBeatenMy(int num);

    void setBeatenOpponent(int num);

    boolean isWhite();
  }

  @ProxyCodeSplit
  @NameToken(NameTokens.ANALYSIS_PAGE)
  @NoGatekeeper
  interface MyProxy extends ProxyPlace<AnalysisPresenter> {
  }
}
