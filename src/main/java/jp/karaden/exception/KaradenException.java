package jp.karaden.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.karaden.model.ErrorInterface;

public class KaradenException extends Exception {
    public Map<String, List<String>> headers;
    public String body;
    public ErrorInterface error;

    public KaradenException() {
        this("", new HashMap<>(), "", null);
    }

    public KaradenException(String message) {
        this(message, new HashMap<>(), "", null);
    }

    public KaradenException(String message, Throwable cause) {
        super(message, cause);
        this.headers = null;
        this.body = null;
        this.error = null;
    }

    public KaradenException(Map<String, List<String>> headers, String body, ErrorInterface error) {
        this("", headers, body, error);
    }

    public KaradenException(String message, Map<String, List<String>> headers, String body, ErrorInterface error) {
        super(message == null ? "" : message);
        this.headers = headers;
        this.body = body;
        this.error = error;
    }
}
