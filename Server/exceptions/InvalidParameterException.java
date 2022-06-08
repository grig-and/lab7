package exceptions;

/**
 * Exception for invalid parameter
 */
public class InvalidParameterException extends Throwable {
    public InvalidParameterException(String s) {
        super(s);
    }
}
