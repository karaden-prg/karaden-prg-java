package jp.karaden.exception;

import jp.karaden.model.Error;

public class InvalidRequestOptionsException extends KaradenException {
    public InvalidRequestOptionsException(Error error) {
        super("", null, null, error);
    }
}
