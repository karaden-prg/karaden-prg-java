package jp.karaden.net;

import java.util.Map;

import jp.karaden.RequestOptions;
import jp.karaden.exception.KaradenException;

public interface RequestorInterface {
    OkHttpResponse send(String method, String path, Map<String, ?> params, Map<String, ?> data, RequestOptions requestOptions) throws KaradenException;
}
