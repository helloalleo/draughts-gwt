package online.shashki.rus.server.rest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;
import online.shashki.rus.server.service.PlayerService;
import online.shashki.rus.shared.model.Player;
import online.shashki.rus.shared.rest.PlayersResource;

import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class PlayersResourceImpl implements PlayersResource {

  private final PlayerService playerService;
  private final Provider<HttpServletRequest> requestProvider;

  @Inject
  PlayersResourceImpl(
      PlayerService playerService,
      Provider<HttpServletRequest> requestProvider) {
    this.playerService = playerService;
    this.requestProvider = requestProvider;
  }

  @Override
  public Player saveOrCreate(Player player) {
    return playerService.saveOrCreate(requestProvider.get().getSession(), player, false);
  }
}
