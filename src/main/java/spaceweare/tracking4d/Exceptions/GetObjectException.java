package spaceweare.tracking4d.Exceptions;

public class GetObjectException extends RuntimeException{
    public GetObjectException(String message) {
        super(message);
    }

    public GetObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
