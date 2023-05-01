package jp.karaden.exception;

import jp.karaden.model.Error;

public class InvalidParamsException extends KaradenException {
    public InvalidParamsException(Error error) {
        super("", null, null, error);
    }
}
