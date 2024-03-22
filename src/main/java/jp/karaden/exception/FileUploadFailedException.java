package jp.karaden.exception;

public class FileUploadFailedException extends KaradenException {
    public FileUploadFailedException() {
        super();
    }

    public FileUploadFailedException(Exception e) {
        super("", e);
    }
}
