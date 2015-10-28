package online.draughts.rus.draughts;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 30.08.15
 * Time: 13:21
 */
public class SquareNotFoundException extends Exception {

    private static final String MESSAGE = "Square not found";

    public SquareNotFoundException() {
        super(MESSAGE);
    }
}
