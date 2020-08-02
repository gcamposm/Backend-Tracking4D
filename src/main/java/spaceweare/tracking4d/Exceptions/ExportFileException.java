package spaceweare.tracking4d.Exceptions;

public class ExportFileException extends RuntimeException{
    public ExportFileException(String message) {
        super(message);
    }

    public ExportFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
