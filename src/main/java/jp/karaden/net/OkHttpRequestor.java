package jp.karaden.net;

import java.io.IOException;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

import jp.karaden.Config;
import jp.karaden.RequestOptions;
import jp.karaden.exception.ConnectionException;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpRequestor implements RequestorInterface {
    protected static final String DEFAULT_USER_AGENT = "Karaden/Java/";

    protected OkHttpClient httpClient;

    public OkHttpRequestor() {
    }

    public OkHttpResponse send(String method, String path, Map<String, ?> params, Map<String, ?> data, RequestOptions requestOptions) throws ConnectionException {
        requestOptions = Config.asRequestOptions().merge(requestOptions);

        okhttp3.Request.Builder requestBuilder = (new okhttp3.Request.Builder())
            .method(method, this.buildFormBody(data))
            .url(buildHttpUrl(path, params, requestOptions))
            .addHeader("User-Agent", this.buildUserAgent(requestOptions))
            .addHeader("Karaden-Client-User-Agent", this.buildClientUserAgent())
            .addHeader("Authorization", this.buildAuthorization(requestOptions))
            .addHeader("Karaden-Version", requestOptions.apiVersion);

        try {
            okhttp3.Response response = this.getHttpClient()
                .newBuilder()
                .connectTimeout(requestOptions.connectionTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(requestOptions.readTimeout, TimeUnit.MILLISECONDS)
                .proxy(requestOptions.proxy == null ? Proxy.NO_PROXY : requestOptions.proxy)
                .build()
                .newCall(requestBuilder.build())
                .execute();

            return new OkHttpResponse(response, requestOptions);
        } catch (IOException e) {
            throw new ConnectionException("Karadenへのリクエスト中にIOExceptionが発生しました。接続を確認し、もう一度やり直してください。", e);
        }
    }

    protected OkHttpClient getHttpClient() {
        if (this.httpClient == null) {
            this.httpClient = (new OkHttpClient.Builder())
                .followRedirects(true)
                .addInterceptor(this.buildHttpLoggingInterceptor())
                .build();
        }
        return this.httpClient;
    }

    protected Interceptor buildHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        return interceptor.setLevel(Config.isLogging ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    }

    protected String buildUserAgent(RequestOptions requestOptions) {
        return requestOptions.userAgent == null ? OkHttpRequestor.DEFAULT_USER_AGENT + Config.VERSION : requestOptions.userAgent;
    }

    protected String buildClientUserAgent() {
        List<String> uname = Arrays
            .asList("os.name", "os.version", "os.arch")
            .stream()
            .map((key) -> System.getProperty(key))
            .collect(Collectors.toList());

        return (new JSONObject())
            .put("binding_version", Config.VERSION)
            .put("language", "Java")
            .put("language_version", System.getProperty("java.version"))
            .put("uname", uname)
            .toString();
    }

    protected String buildAuthorization(RequestOptions requestOptions) {
        return "Bearer " + requestOptions.apiKey;
    }

    protected HttpUrl buildHttpUrl(String path, Map<String, ?> params, RequestOptions requestOptions) {

        HttpUrl.Builder builder = HttpUrl.parse(requestOptions.getBaseUri())
            .newBuilder();

        Arrays.stream(path.split("/"))
            .forEach((pathSegment) -> builder.addPathSegment(pathSegment));

        if (params != null) {
            params.forEach((key, value) -> builder.addQueryParameter(key, value.toString()));
        }

        return builder.build();
    }

    protected FormBody buildFormBody(Map<String, ?> data) {
        if (data == null) {
            return null;
        }

        FormBody.Builder builder = new FormBody.Builder();

        data.entrySet()
            .stream()
            .flatMap((entry) -> {
                String key = entry.getKey();
                Object value1 = entry.getValue();
                if (value1 instanceof List) {
                    return ((List<?>)value1).stream()
                        .map((value2) -> new String[] { key + "[]", value2.toString() });
                } else {
                    return Stream.of(new String[][] {{ key, value1.toString() }});
                }
            })
            .forEach((String[] value) -> builder.add(value[0], value[1]));

        return builder.build();
    }
}
