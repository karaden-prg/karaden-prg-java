package jp.karaden.exception;

import java.util.List;
import java.util.Map;

import jp.karaden.model.ErrorInterface;

public class BadRequestException extends KaradenException {
    public final static int STATUS_CODE = 400;

    public BadRequestException() {
        super();
    }

    public BadRequestException(Map<String, List<String>> headers, String body, ErrorInterface error) {
        super("", headers, body, error);
    }
}
