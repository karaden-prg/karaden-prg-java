package jp.karaden.exception;

import java.util.List;
import java.util.Map;

import jp.karaden.model.ErrorInterface;

public class UnprocessableEntityException extends KaradenException {
    public final static int STATUS_CODE = 422;

    public UnprocessableEntityException() {
        super();
    }

    public UnprocessableEntityException(Map<String, List<String>> headers, String body, ErrorInterface error) {
        super("", headers, body, error);
    }
}
