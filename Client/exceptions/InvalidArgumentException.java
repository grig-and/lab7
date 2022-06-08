package exceptions;

/**
 * Exception for invalid argument (in command)
 */
public class InvalidArgumentException extends Exception {
    public InvalidArgumentException(String msg) {
        super(msg);
    }
}
