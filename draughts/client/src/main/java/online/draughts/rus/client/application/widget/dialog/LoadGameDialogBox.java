package online.draughts.rus.client.application.widget.dialog;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by aleksey on 24.07.16.
 */
public class LoadGameDialogBox extends AbstractDialogBox {

  protected LoadGameDialogBox() {
    // TODO сделать диалог который отображает игры для выбора на редактирование
    HTML caption = new HTML(messages.inviteCaption());
    caption.getElement().addClassName(resources.style().dialogCaptionInfo());
    setHTML(new SafeHtmlBuilder().appendHtmlConstant(caption.getElement().getString()).toSafeHtml());

    VerticalPanel panel = new VerticalPanel();
    panel.setPixelSize(WIDTH, HEIGHT);

//    panel.add(inviteLabel);

  }
}
