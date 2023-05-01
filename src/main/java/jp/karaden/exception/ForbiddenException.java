package jp.karaden.exception;

import java.util.List;
import java.util.Map;

import jp.karaden.model.ErrorInterface;

public class ForbiddenException extends KaradenException {
    public final static int STATUS_CODE = 403;

    public ForbiddenException() {
        super();
    }

    public ForbiddenException(Map<String, List<String>> headers, String body, ErrorInterface error) {
        super("", headers, body, error);
    }
}
