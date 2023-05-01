package jp.karaden.net;

import jp.karaden.exception.KaradenException;
import jp.karaden.model.KaradenObject;


public interface ResponseInterface {
    KaradenObject getObject();
    KaradenException getError();
    boolean isError();
}
