package jp.karaden.exception;

import java.util.List;
import java.util.Map;

import jp.karaden.model.ErrorInterface;

public class UnauthorizedException extends KaradenException {
    public final static int STATUS_CODE = 401;

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(Map<String, List<String>> headers, String body, ErrorInterface error) {
        super("", headers, body, error);
    }
}
