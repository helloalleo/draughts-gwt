package online.draughts.rus.client.util;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import online.draughts.rus.client.place.NameTokens;
import online.draughts.rus.shared.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 07.12.14
 * Time: 22:58
 */
public class Utils {

  public static NameTokens.Link[] concatLinks(NameTokens.Link[] a, NameTokens.Link[] b) {
    if (a == null) return b;
    if (b == null) return a;
    NameTokens.Link[] r = new NameTokens.Link[a.length + b.length];
    System.arraycopy(a, 0, r, 0, a.length);
    System.arraycopy(b, 0, r, a.length, b.length);
    return r;
  }

  public static String format(final String format, final Object... args) {
    if (StringUtils.isEmpty(format) || args == null) {
      return "";
    }

    final String pattern = "%s";

    int start = 0, last = 0, argsIndex = 0;
    final StringBuilder result = new StringBuilder();
    while ((start = format.indexOf(pattern, last)) != -1) {
      if (args.length <= argsIndex) {
        throw new IllegalArgumentException("There is more replace patterns than arguments!");
      }
      result.append(format.substring(last, start));
      result.append(args[argsIndex++]);

      last = start + pattern.length();
    }

    if (args.length > argsIndex) {
      throw new IllegalArgumentException("There is more arguments than replace patterns!");
    }

    result.append(format.substring(last));
    return result.toString();
  }

  public static native void setSelectionRange(Element elem, int pos, int length) /*-{
      try {
          console.log(pos);
          var selection = null, range2 = null;
          var iframeWindow = elem.contentWindow;
          var iframeDocument = iframeWindow.document;

          selection = iframeWindow.getSelection();
          range2 = selection.getRangeAt(0);

          //create new range
          var range = iframeDocument.createRange();
          range.setStart(selection.anchorNode, pos);
          range.setEnd(selection.anchorNode, length);

          //remove the old range and add the newly created range
          if (selection.removeRange) { // Firefox, Opera, IE after version 9
              selection.removeRange(range2);
          } else {
              if (selection.removeAllRanges) { // Safari, Google Chrome
                  selection.removeAllRanges();
              }
          }
          selection.addRange(range);
      } catch (e) {
          $wnd.alert(e);
      }
  }-*/;

  public static native void setCursor(Element elem, int pos, int length) /*-{
      var node = elem.contentWindow.document.body
      var range = elem.contentWindow.getSelection().getRangeAt(0);

      var treeWalker = $doc.createTreeWalker(node, NodeFilter.SHOW_TEXT, function (node) {
          console.log('treeWalker in node', node);
          var nodeRange = $doc.createRange();
          nodeRange.selectNodeContents(node);
          return NodeFilter.FILTER_ACCEPT;
      });

      console.log('treeWaler out current', treeWalker.currentNode);
      var charCount = 0;
      while (treeWalker.nextNode()) {
          console.log('currentItem node tw', treeWalker.currentNode);
          if (charCount + treeWalker.currentNode.length > pos) {
              console.log('cntw pos', treeWalker.currentNode.length, pos);
              break;
          }

          charCount += treeWalker.currentNode.length;
      }

      var newRange = elem.contentWindow.document.createRange();
      newRange.setStart(treeWalker.currentNode, 1);
      newRange.setEnd(treeWalker.currentNode, 1);

      var selection = elem.contentWindow.getSelection();

      if (selection.removeRange) { // Firefox, Opera, IE after version 9
          selection.removeRange(range);
      } else if (selection.removeAllRanges) { // Safari, Google Chrome
          selection.removeAllRanges();
      }

      selection.addRange(newRange);
  }-*/;

  public static native void setCursorToEnd(com.google.gwt.user.client.Element elem) /*-{
      var node = elem.contentWindow.document.body
      var range = elem.contentWindow.getSelection().getRangeAt(0);

      var selection = elem.contentWindow.getSelection();

      var newRange = elem.contentWindow.document.createRange();
      newRange.setStart(node, node.childNodes.length);
      newRange.setEnd(node, node.childNodes.length);
      console.log(newRange);

      if (selection.removeRange) { // Firefox, Opera, IE after version 9
          selection.removeRange(range);
      } else if (selection.removeAllRanges) { // Safari, Google Chrome
          selection.removeAllRanges();
      }

      selection.addRange(newRange);
  }-*/;


  public static void showWaitCursor() {
    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
  }

  public static void showDefaultCursor() {
    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
  }
}
