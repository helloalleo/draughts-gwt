//package online.shashki.rus.server.servlet.oauth;
//
//import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
//import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
//import com.google.api.client.http.GenericUrl;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * Created with IntelliJ IDEA.
// * User: alekspo
// * Date: 18.11.14
// * Time: 20:30
// */
//@WebServlet(name = "OAuthFacebookCallbackServlet", urlPatterns = {"/OAuthFacebookCallbackServlet"})
//public class OAuthFacebookCallbackServlet extends AbstractAuthorizationCodeCallbackServlet {
//
//  @Override
//  protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential) throws ServletException, IOException {
//    resp.sendRedirect("/");
//  }
//
//  @Override
//  protected void onError(HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse) throws ServletException, IOException {
//    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//  }
//
//  @Override
//  protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
////    return Utils.getFlow(hostName, Utils.OAuthProvider.FACEBOOK);
//    return null;
//  }
//
//  @Override
//  protected String getVkRedirectUri(HttpServletRequest httpServletRequest) throws ServletException, IOException {
//    GenericUrl url = new GenericUrl(httpServletRequest.getRequestURL().toString());
////    url.setRawPath(OAuthClient.REDIRECT_FACEBOOK_CALLBACK_URL);
//    return url.build();
//  }
//
//  @Override
//  protected String getUserId(HttpServletRequest httpServletRequest) throws ServletException, IOException {
//    return httpServletRequest.getSession(true).getId();
//  }
//}
