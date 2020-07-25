package drii.pingeso.backend.Exceptions;

public class MongoSaveException extends RuntimeException{
    public MongoSaveException(String message) {
        super(message);
    }
    public MongoSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}