package exceptions;

public class DBError extends Exception {
    public DBError(String msg) {
        super(msg);
    }
}
