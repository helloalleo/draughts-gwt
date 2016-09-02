package online.draughts.rus.shared.resource;

import online.draughts.rus.shared.dto.GameDto;
import online.draughts.rus.shared.dto.PlayerDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(ApiPaths.GAMES)
@Produces(MediaType.APPLICATION_JSON)
public interface GamesResource {
  String DEFAULT_LIMIT = "50";
  String DEFAULT_OFFSET = "0";

  @GET
  List<GameDto> getGames(
      @DefaultValue(DEFAULT_OFFSET) @QueryParam(ApiParameters.OFFSET) int offset,
      @DefaultValue(DEFAULT_LIMIT) @QueryParam(ApiParameters.LIMIT) int limit);

  @GET
  @Path(ApiPaths.LOGGED_IN_USER)
  List<GameDto> getLoggedInUserGames(
      @DefaultValue(DEFAULT_OFFSET) @QueryParam(ApiParameters.OFFSET) int offset,
      @DefaultValue(DEFAULT_LIMIT) @QueryParam(ApiParameters.LIMIT) int limit);

  @GET
  @Path(ApiPaths.COUNT)
  Integer getGamesCount();

  @POST
  GameDto save(GameDto game);

  @GET
  @Path(ApiPaths.PLAYER_STAT)
  PlayerDto updatePlayerStat(@QueryParam(ApiParameters.GAME_ID) long gameId, @QueryParam(ApiParameters.PLAYER_ID) long playerId);

  @GET
  @Path(ApiPaths.PATH_ID)
  GameDto game(@PathParam(ApiParameters.ID) long gameId);
}
