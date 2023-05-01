package jp.karaden.exception;

import java.util.List;
import java.util.Map;


public class UnknownErrorException extends KaradenException {
    public int statusCode;

    public UnknownErrorException(int statusCode, Map<String, List<String>> headers, String body) {
        super(headers, body, null);

        this.statusCode = statusCode;
    }
}
