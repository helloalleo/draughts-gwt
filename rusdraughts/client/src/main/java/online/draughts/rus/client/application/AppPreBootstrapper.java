package online.draughts.rus.client.application;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtplatform.mvp.client.PreBootstrapper;
import online.draughts.rus.client.util.DTCookiesImpl;
import online.draughts.rus.client.util.DebugUtils;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.10.15
 * Time: 7:58
 */
public class AppPreBootstrapper implements PreBootstrapper {
  @Override
  public void onPreBootstrap() {
    String locale = Cookies.getCookie(DTCookiesImpl.LOCALE);
    final HTML html;
    if ("en".equals(locale)) {
      html = new HTML("Loading...");
    } else if ("ru".equals(locale)) {
      html = new HTML("Загрузка...");
    } else {
      html = new HTML("Loading...");
    }
    html.getElement().setId("loading");
    final Style style = html.getElement().getStyle();
    style.setFontSize(20, Style.Unit.PX);
    style.setFontStyle(Style.FontStyle.ITALIC);
    style.setPosition(Style.Position.ABSOLUTE);
    style.setTop(50, Style.Unit.PCT);
    style.setLeft(50, Style.Unit.PCT);
    style.setMarginLeft(-50, Style.Unit.PX);
    style.setMarginTop(-50, Style.Unit.PX);
    RootPanel.get().add(html);
    DebugUtils.initDebugAndErrorHandling();
  }
}
