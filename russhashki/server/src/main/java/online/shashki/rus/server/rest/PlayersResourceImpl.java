package online.shashki.rus.server.rest;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import online.shashki.rus.server.service.PlayerService;
import online.shashki.rus.shared.model.Player;
import online.shashki.rus.shared.rest.PlayersResource;

@RequestScoped
public class PlayersResourceImpl implements PlayersResource {

  private final PlayerService playerService;

  @Inject
  PlayersResourceImpl(
      PlayerService playerService) {
    this.playerService = playerService;
  }

  @Override
  public Player saveOrCreate(Player player) {
    return playerService.saveOrCreate(player);
  }
}
