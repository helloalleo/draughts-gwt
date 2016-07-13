package online.draughts.rus.client.application.home;

import com.gwtplatform.mvp.client.UiHandlers;
import online.draughts.rus.shared.dto.PlayerDto;

interface HomeUiHandlers extends UiHandlers {

  void getMoreGames(int newPageSize);

  void updatePlayShowPanel();

  PlayerDto getPlayer();
}
