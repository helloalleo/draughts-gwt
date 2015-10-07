package online.shashki.rus.client.exception;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 03.10.15
 * Time: 11:16
 */
public class InvalidCookieException extends RuntimeException {

  private static final String MESSAGE = "Invalid cookie";

  public InvalidCookieException() {
    super(MESSAGE);
  }
}
