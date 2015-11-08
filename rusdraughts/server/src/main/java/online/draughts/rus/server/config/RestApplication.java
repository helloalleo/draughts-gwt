package online.draughts.rus.server.config;

import online.draughts.rus.shared.resource.ApiPaths;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.10.15
 * Time: 7:36
 */
@ApplicationPath(ApiPaths.RESOURCE)
public class RestApplication extends Application {
}
