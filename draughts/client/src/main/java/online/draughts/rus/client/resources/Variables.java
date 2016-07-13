package online.draughts.rus.client.resources;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 20.09.15
 * Time: 17:31
 */
public class Variables {

  /* -- Colors -- */
  public static final String C_PANEL_DARK = "#333";
  public static final String C_PANEL_LIGHT = "#eaeaea";
  public static final String C_PANEL_MEDIUM = "#ccc";
  public static final String C_MESSAGEBOX = "#ffba00";
  public static final String C_TITLE = "#921b12";
  public static final String C_NAVBAR = "#418584";
  public static final String C_NAVBAR_BORDER = "#306160";
  public static final String C_NAVBAR_ACTIVE_ITEM = "#419392";
  public static final String C_NAVBAR_COLOR = "#fff";
  /* -- Sizes -- */
  public static final String S_TITLE = "22px";
  public static final String S_MAIN_CONTAINER_SCROLL_MARGIN_TOP = "80px";
  public static final String S_MAIN_CONTAINER_MARGIN_TOP = "100px";
  public static final String S_NAVBAR_TOP_HEIGHT = "70px";
  public static final String S_NAVBAR_SCROLL_HEIGHT = "55px";
  public static final String S_NAVBAR_TOP_LINE_HEIGHT = "40px";
  public static final String S_NAVBAR_SCROLL_LINE_HEIGHT = "20px";
  public static final String S_LOGO_TOP_HEIGHT = "40px";
  public static final String S_LOGO_SCROLL_HEIGHT = "20px";
  public static final String S_FOOTER_HEIGHT = "124px";
  public static final String S_FONT_SIZE = "14px";
  public static final String S_LINE_HEIGHT = "1.42857143";

  /*Bootstrap*/
  public static final int COLUMNS_IN_LAYOUT = 12;

  public static int marginTop() {
    return getWithoutPixels(S_MAIN_CONTAINER_MARGIN_TOP);
  }

  public static int footerHeight() {
    return getWithoutPixels(S_FOOTER_HEIGHT);
  }

  public static Integer getWithoutPixels(String valuePx) {
    return Integer.valueOf(valuePx.substring(0, valuePx.length() - 2));
  }
}
