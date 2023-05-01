package jp.karaden.exception;

import java.util.List;
import java.util.Map;

import jp.karaden.model.ErrorInterface;

public class TooManyRequestsException extends KaradenException {
    public final static int STATUS_CODE = 429;

    public TooManyRequestsException() {
        super();
    }

    public TooManyRequestsException(Map<String, List<String>> headers, String body, ErrorInterface error) {
        super("", headers, body, error);
    }
}
