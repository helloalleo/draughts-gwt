package online.shashki.ru.shashki;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 30.08.15
 * Time: 13:21
 */
public class SquareNotFoundException extends Exception {

    public SquareNotFoundException() {
        super("Square not found");
    }
}
