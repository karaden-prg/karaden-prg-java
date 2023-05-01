package jp.karaden.exception;

import java.util.List;
import java.util.Map;

import jp.karaden.model.ErrorInterface;

public class NotFoundException extends KaradenException {
    public final static int STATUS_CODE = 404;

    public NotFoundException() {
        super();
    }

    public NotFoundException(Map<String, List<String>> headers, String body, ErrorInterface error) {
        super("", headers, body, error);
    }
}
