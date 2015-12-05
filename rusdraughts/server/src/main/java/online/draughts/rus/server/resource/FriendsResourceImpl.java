package online.draughts.rus.server.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import online.draughts.rus.server.service.FriendService;
import online.draughts.rus.server.util.AuthUtils;
import online.draughts.rus.server.domain.Friend;
import online.draughts.rus.shared.resource.FriendsResource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;

@RequestScoped
public class FriendsResourceImpl implements FriendsResource {

  private final FriendService friendService;
  private final HttpServletRequest request;
  private final Provider<Boolean> authProvider;

  @Inject
  FriendsResourceImpl(
      @Named(AuthUtils.AUTHENTICATED) Provider<Boolean> authProvider,
      FriendService friendService,
      HttpServletRequest request) {
    this.friendService = friendService;
    this.request = request;
    this.authProvider = authProvider;
  }

  @Override
  public Friend saveOrCreate(Friend friend) {
    if (!authProvider.get()) {
      throw new NotAuthorizedException("Access denied");
    }
    return friendService.saveOrCreate(friend);
  }
}
