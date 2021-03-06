package online.draughts.rus.shared.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.06.16
 * Time: 8:06
 */
@Path(ApiPaths.ERRORS)
@Produces(MediaType.APPLICATION_JSON)
public interface ErrorHandlerResource {

  @POST
  @Path(ApiPaths.ERROR)
  void postError(@QueryParam("error") String error, @QueryParam(ApiParameters.ID) Long senderId);

  @POST
  @Path(ApiPaths.COACH_APPLY)
  void postApply(@QueryParam("message") String message, @QueryParam(ApiParameters.ID) Long senderId);
}
