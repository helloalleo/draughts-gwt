package online.draughts.rus.client.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 01.05.16
 * Time: 9:16
 */
public class AdsUtils {

  public static void addAdvertisementFromGoogleScript() {
    /**
     * <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
     <!-- RuShashkiNet728x90 -->
     <ins class="adsbygoogle"
     style="display:inline-block;width:728px;height:90px"
     data-ad-client="ca-pub-6590061713732270"
     data-ad-slot="4461668713"></ins>
     <script>
     (adsbygoogle = window.adsbygoogle || []).push({});
     </script>
     */

    Document doc = Document.get();
    ScriptElement script = doc.createScriptElement();
    script.setSrc("//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js");
    script.setType("text/javascript");
    script.setLang("javascript");
    doc.getBody().appendChild(script);

    script = doc.createScriptElement();
    script.setText("(adsbygoogle = window.adsbygoogle || []).push({});");
    script.setType("text/javascript");
    script.setLang("javascript");
    doc.getBody().appendChild(script);
  }
}
