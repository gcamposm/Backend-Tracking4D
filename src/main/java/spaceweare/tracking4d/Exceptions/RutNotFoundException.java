package spaceweare.tracking4d.Exceptions;

public class RutNotFoundException extends RuntimeException {
    public RutNotFoundException(String message) {
        super(message);
    }

    public RutNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
