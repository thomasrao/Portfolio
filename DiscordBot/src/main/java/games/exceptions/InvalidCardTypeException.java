package games.exceptions;

/**
 * @author Tom
 */
public class InvalidCardTypeException extends Exception {
    public InvalidCardTypeException() {
        super("Invalid card type found.");
    }
}
