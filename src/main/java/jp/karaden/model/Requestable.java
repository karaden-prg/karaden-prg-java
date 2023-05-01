package jp.karaden.model;


import java.util.Map;

import jp.karaden.RequestOptions;
import jp.karaden.exception.KaradenException;
import jp.karaden.net.OkHttpRequestor;
import jp.karaden.net.OkHttpResponse;


abstract class Requestable extends KaradenObject {
    protected static OkHttpRequestor request;

    static {
        Message.request = new OkHttpRequestor();
    }

    public Requestable() {
        super();
    }

    public Requestable(Object id, RequestOptions requestOptions) {
        super(id, requestOptions);
    }

    protected static KaradenObject request(String method, String path, Map<String, ?> params, Map<String, ?> data, RequestOptions requestOptions) throws KaradenException {
        OkHttpResponse response = Requestable.request.send(method, path, params, data, requestOptions);
        if (response.isError()) {
            throw response.getError();
        }
        return response.getObject();
    }
}
