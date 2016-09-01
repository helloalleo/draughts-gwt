package online.draughts.rus.client.place;

import com.google.inject.Inject;
import online.draughts.rus.client.application.security.CurrentSession;
import online.draughts.rus.shared.dto.PlayerDto;
import online.draughts.rus.shared.locale.DraughtsMessages;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.ArrayList;
import java.util.List;

public class NameTokens {
  // токены - адреса в навигации
  public static final String HOME_PAGE = "!/home";
  public static final String LEARN_PAGE = "!/learn";
  public static final String PLAY_PAGE = "!/play";
  public static final String GAME_PAGE = "!/game";
  public static final String MY_GAMES_PAGE = "!/mygames";
  public static final String ANALYSIS_PAGE = "!/analysis";
  public static final String LOGIN_PAGE = "!/login";
  public static final String PROFILE_PAGE = "!/profile";
  public static final String RULES_PAGE = "!/rules";
  public static final String LOVE_PAGE = "!/love";

  public static final String ERROR_PAGE = "!/error";
  public static final String GENERAL_SETTINGS_PAGE = "!/generalSettings";
  public static final String SETTINGS_PAGE = "!/settings";
  public static final String COACH_PAGE = "!/coach";
  public static final String COACH_SETTINGS_PAGE = "!/coachSettings";

  public static final String LOGOUT_PAGE = "/logout";
  private final String ruShashkiNetUrl = "https://rushashki-net.appspot.com";

  // ссылки - название и токен
  private final Link homeLink;
  private final Link learnLink;
  private final Link loveLink;
  private final Link rulesLink;
  private final Link loginLink;
  private final Link logoutLink;
  private final Link profileLink;
  private final Link generalSettingsLink;
  private final Link settingsLink;
  private final Link coachSettingsLink;
  private final Link playLink;
  private final Link myGamesLink;
  //  private final Link gameLink;
  private final Link analysisLink;
  private final Link ruShashkiNetLink;
  private final Link coachLink;
  private final CurrentSession currentSession;
  private final PlayerDto player;

  @Inject
  public NameTokens(DraughtsMessages messages, CurrentSession currentSession) {
    this.currentSession = currentSession;
    this.player = currentSession.getPlayer();

    homeLink = new Link(HOME_PAGE, messages.home(), null, IconType.HOME, true);
    ruShashkiNetLink = new Link(null, "RuShashkiNet", ruShashkiNetUrl, IconType.LINK, false);
    learnLink = new Link(LEARN_PAGE, messages.learn(), null, null, false);
    playLink = new Link(PLAY_PAGE, messages.play(), null, IconType.PLAY_CIRCLE_O, false);
//    gameLink = new Link(GAME_PAGE, messages.game(), null, IconType.PLAY_CIRCLE_O);
    analysisLink = new Link(ANALYSIS_PAGE, messages.analysis(), null, null, false);
    loveLink = new Link(LOVE_PAGE, "", null, IconType.HEART_O, false);
    loginLink = new Link(LOGIN_PAGE, messages.login(), null, IconType.SIGN_IN, false);
    logoutLink = new Link(null, messages.logout(), LOGOUT_PAGE, IconType.SIGN_OUT, false);
    profileLink = new Link(PROFILE_PAGE, messages.profile(), null, IconType.USER, false);
    generalSettingsLink = new Link(GENERAL_SETTINGS_PAGE, messages.generalSettings(), null, IconType.USER, false);
    settingsLink = new Link(SETTINGS_PAGE, messages.settings(), null, IconType.GEAR, false);
    coachSettingsLink = new Link(COACH_SETTINGS_PAGE, messages.forCoaches(), null, null, false);
    coachLink = new Link(COACH_PAGE, messages.coach(), null, null, false);
    rulesLink = new Link(RULES_PAGE, messages.rules(), null, null, false);
    myGamesLink = new Link(MY_GAMES_PAGE, messages.myGames(), null, null, true);
  }

  public static String getLovePage() {
    return LOVE_PAGE;
  }

  public Link[] getLeftLinks() {
    return new Link[]{homeLink, rulesLink};
  }

  public Link[] getLeftAuthLinks() {
    List<Link> menu = new ArrayList<Link>(){{ add(homeLink); add(playLink); add(myGamesLink); }};
    if (player == null || !player.isCoach()) {
      menu.add(rulesLink);
    } else {
      menu.add(coachLink);
      menu.add(analysisLink);
    }
    return menu.toArray(new Link[menu.size()]);
  }

  public Link[] getRightLinks() {
    return new Link[]{loginLink};
  }

  public Link[] getRightAuthLinks() {
    return new Link[]{profileLink, logoutLink};
  }

  public Link[] getProfileLinks() {
    return new Link[]{generalSettingsLink, settingsLink, coachSettingsLink};
  }

  public static class Link {
    public String token;
    public String name;
    public String href;
    public IconType iconType;
    public boolean waitCursor; // wait cursor

    Link(String token, String name, String href, IconType iconType, boolean waitCursor) {
      this.token = token;
      this.name = name;
      this.href = href;
      this.iconType = iconType;
      this.waitCursor = waitCursor;
    }
  }
}
