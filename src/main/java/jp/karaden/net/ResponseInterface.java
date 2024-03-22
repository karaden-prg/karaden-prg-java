package jp.karaden.net;

import jp.karaden.exception.KaradenException;
import jp.karaden.model.KaradenObject;
import okhttp3.Headers;


public interface ResponseInterface {
    KaradenObject getObject();
    KaradenException getError();
    int getStatusCode();
    Headers getHeaders();
    boolean isError();
}
