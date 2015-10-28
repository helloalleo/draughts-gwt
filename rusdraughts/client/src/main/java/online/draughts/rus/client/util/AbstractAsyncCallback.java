package online.shashki.rus.client.util;

import com.google.gwt.user.client.rpc.AsyncCallback;
import online.shashki.rus.client.application.widget.dialog.ErrorDialogBox;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.10.15
 * Time: 11:04
 */
public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {

  @Override
  public void onFailure(Throwable throwable) {
    ErrorDialogBox.setMessage(throwable).show();
  }
}
