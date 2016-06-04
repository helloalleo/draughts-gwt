package online.draughts.rus.client.util;

import com.google.gwt.user.client.rpc.AsyncCallback;
import online.draughts.rus.client.application.widget.dialog.ErrorDialogBox;
import online.draughts.rus.client.gin.DialogFactory;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 09.10.15
 * Time: 11:04
 */
public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {

  private final DialogFactory dialogFactory;

  public AbstractAsyncCallback(DialogFactory dialogFactory) {
    this.dialogFactory = dialogFactory;
  }

  @Override
  public void onFailure(Throwable throwable) {
    final ErrorDialogBox errorDialogBox = dialogFactory.createErrorDialogBox();
    errorDialogBox.setMessage(throwable);
    errorDialogBox.show();
  }
}
