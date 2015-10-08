package online.shashki.rus.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 08.10.15
 * Time: 15:17
 */
public class FetchCurrentPlayerAction extends UnsecuredActionImpl<FetchCurrentPlayerResult> {

  private String hi;

  public FetchCurrentPlayerAction() {
  }

  public FetchCurrentPlayerAction(String hi) {
    this.hi = hi;
  }

  public String getHi() {
    return hi;
  }
}
