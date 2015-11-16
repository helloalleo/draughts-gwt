package online.draughts.rus.shared.resource;

import online.draughts.rus.shared.model.Game;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(ApiPaths.GAMES)
@Produces(MediaType.APPLICATION_JSON)
public interface GamesResource {
  String DEFAULT_LIMIT = "50";
  String DEFAULT_OFFSET = "0";
  int LIMIT_ALL = -1;

  @GET
  List<Game> getGames(
      @DefaultValue(DEFAULT_OFFSET) @QueryParam(ApiParameters.OFFSET) int offset,
      @DefaultValue(DEFAULT_LIMIT) @QueryParam(ApiParameters.LIMIT) int limit);

  @GET
  @Path(ApiPaths.LOGGED_IN_USER)
  List<Game> getLoggedInUserGames(
      @DefaultValue(DEFAULT_OFFSET) @QueryParam(ApiParameters.OFFSET) int offset,
      @DefaultValue(DEFAULT_LIMIT) @QueryParam(ApiParameters.LIMIT) int limit);

  @GET
  @Path(ApiPaths.COUNT)
  Long getGamesCount();

  @POST
  Game saveOrCreate(Game game);

  @GET
  @Path(ApiPaths.PATH_ID)
  Game game(@PathParam(ApiParameters.ID) Long gameId);
}
