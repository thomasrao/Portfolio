package games.exceptions;

/**
 * @author Tom
 */
public class InvalidCardValueException extends Exception {
    public InvalidCardValueException() {
        super("Invalid card value found.");
    }
}
