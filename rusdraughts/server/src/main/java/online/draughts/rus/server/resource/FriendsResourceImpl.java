package online.draughts.rus.server.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.servlet.RequestScoped;
import online.draughts.rus.server.service.FriendService;
import online.draughts.rus.server.util.AuthUtils;
import online.draughts.rus.shared.dto.FriendDto;
import online.draughts.rus.shared.resource.FriendsResource;
import online.draughts.rus.shared.util.DozerHelper;
import org.dozer.Mapper;

import javax.ws.rs.NotAuthorizedException;
import java.util.List;

@RequestScoped
public class FriendsResourceImpl implements FriendsResource {

  private final FriendService friendService;
  private final Provider<Boolean> authProvider;
  private final Mapper mapper;

  @Inject
  FriendsResourceImpl(
      @Named(AuthUtils.AUTHENTICATED) Provider<Boolean> authProvider,
      FriendService friendService,
      Mapper mapper) {
    this.friendService = friendService;
    this.mapper = mapper;
    this.authProvider = authProvider;
  }

  @Override
  public FriendDto save(FriendDto friend) {
    if (!authProvider.get()) {
      throw new NotAuthorizedException("Access denied");
    }
    return friendService.save(friend);
  }

  @Override
  public List<FriendDto> getPlayerFriendList(Long playerId) {
    return DozerHelper.map(mapper, friendService.findFriends(playerId), FriendDto.class);
  }
}
