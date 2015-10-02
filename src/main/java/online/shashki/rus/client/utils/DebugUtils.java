package online.shashki.rus.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.UmbrellaException;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 02.09.15
 * Time: 13:29
 */
public class DebugUtils {

  private DebugUtils() {
    // hide constructor because we are a utility class with static methods only.
  }

  public static void initDebugAndErrorHandling() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      public void onUncaughtException(final Throwable tracepoint) {
        performDefaultErrorHandling(tracepoint);
      }
    });
  }

  public static void performDefaultErrorHandling(Throwable caught) {
    if (caught instanceof UmbrellaException) {
      UmbrellaException ue = (UmbrellaException) caught;
      caught = unwrap(ue.getCauses().iterator().next());
    }
    if (caught != null) {
      final String stacktrace = DebugUtils.getStacktraceAsString(caught);
      SHLog.error(stacktrace, caught);
    } else {
      final String message = "Error occurred, but we have no further information about the cause";
      SHLog.debug(message);
    }
  }

  private static Throwable unwrap(Throwable e) {
    if (e instanceof UmbrellaException) {
      UmbrellaException ue = (UmbrellaException) e;
      if (ue.getCauses().size() == 1) {
        return unwrap(ue.getCauses().iterator().next());
      }
    }
    return e;
  }

  public static String getStacktraceAsString(final Throwable tracepoint) {
    final StackTraceElement[] trace = tracepoint.getStackTrace();
    final StringBuffer sbuf = new StringBuffer(2048);
    sbuf.append(tracepoint.toString());
    sbuf.append(": at\n");
    // I cut the stacktrace at depth 7
    final int length = Math.min(7, trace.length);
    for (int i = 0; i <= length; i++) {
      sbuf.append(String.valueOf(trace[i]));
      sbuf.append("\n");
    }
    if (trace.length > 7) {
      sbuf.append("...");
    }
    final String stacktrace = sbuf.toString();
    return stacktrace;
  }
}
