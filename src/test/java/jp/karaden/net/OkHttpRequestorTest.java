package jp.karaden.net;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import jp.karaden.Config;
import jp.karaden.RequestOptions;
import jp.karaden.TestHelper;
import jp.karaden.exception.ConnectionException;
import jp.karaden.exception.KaradenException;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import okio.Buffer;


public class OkHttpRequestorTest {
    protected static MockWebServer server;
    protected OkHttpRequestor request;

    static List<String> methodProvider() {
        return Arrays.asList("post", "get", "put", "delete", "option", "head");
    }

    @BeforeAll
    public static void setUpClass() throws IOException {
        MockWebServer server = new MockWebServer();
        OkHttpRequestorTest.server = server;
    }

    @AfterAll
    public static void tearDownClass() throws IOException {
        OkHttpRequestorTest.server.shutdown();
    }

    @BeforeEach
    public void setUp() throws URISyntaxException {
        this.request = new OkHttpRequestor();
    }

    @Test
    void ベースURLとパスが結合される() throws KaradenException, InterruptedException, URISyntaxException {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody("");
        OkHttpRequestorTest.server.enqueue(response);

        String path = "test";
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(OkHttpRequestorTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();

        this.request.send("GET", path, null, null, requestOptions);

        RecordedRequest request = OkHttpRequestorTest.server.takeRequest();
        String expected = HttpUrl.parse(requestOptions.getBaseUri())
            .newBuilder()
            .addPathSegment(path)
            .build()
            .toString();
        assertEquals(expected, request.getRequestUrl().toString());
    }

    @ParameterizedTest
    @MethodSource("methodProvider")
    void メソッドがHTTPクライアントに伝わる(String method) throws KaradenException, InterruptedException, URISyntaxException {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody("");
        OkHttpRequestorTest.server.enqueue(response);

        String path = "test";
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(OkHttpRequestorTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();

        this.request.send(method, path, null, null, requestOptions);

        RecordedRequest request = OkHttpRequestorTest.server.takeRequest();
        assertEquals(method, request.getMethod());
    }

    @Test
    void URLパラメータがHTTPクライアントに伝わる() throws KaradenException, InterruptedException,  URISyntaxException {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody("");
        OkHttpRequestorTest.server.enqueue(response);

        String path = "test";
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(OkHttpRequestorTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        Map<String, Object> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");

        this.request.send("GET", path, params, null, requestOptions);

        RecordedRequest request = OkHttpRequestorTest.server.takeRequest();
        String expected = HttpUrl.parse(requestOptions.getBaseUri())
            .newBuilder()
            .addPathSegment(path)
            .addQueryParameter("key1", "value1")
            .addQueryParameter("key2", "value2")
            .build()
            .toString();
        assertEquals(expected, request.getRequestUrl().toString());
    }

    @Test
    void 本文がHTTPクライアントに伝わる() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody("");
        OkHttpRequestorTest.server.enqueue(response);

        String path = "test";
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(OkHttpRequestorTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        Map<String, Object> data = new HashMap<>();
        data.put("key1", "value1");
        data.put("key2", "value2");

        this.request.send("post", path, null, data, requestOptions);

        RecordedRequest request = OkHttpRequestorTest.server.takeRequest();
        Buffer expected = new Buffer();
        (new FormBody.Builder())
            .add("key1", "value1")
            .add("key2", "value2")
            .build()
            .writeTo(expected);
        assertEquals(expected.readUtf8(), request.getBody().readUtf8());
    }

    @Test
    void リクエスト時に指定したリクエストオプションはコンストラクタのリクエストオプションを上書きする() throws KaradenException, InterruptedException, URISyntaxException {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody("");
        OkHttpRequestorTest.server.enqueue(response);

        String path = "test";
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(OkHttpRequestorTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();

        this.request.send("GET", path, null, null, requestOptions);

        RecordedRequest request = OkHttpRequestorTest.server.takeRequest();
        String header = request.getHeader("Authorization");
        assertNotNull(header);
        assertEquals("Bearer " + requestOptions.apiKey, header);
    }

    @Test
    void APIキーに基づいてBearer認証ヘッダを出力する() throws KaradenException, InterruptedException, URISyntaxException {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody("");
        OkHttpRequestorTest.server.enqueue(response);

        String path = "test";
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(OkHttpRequestorTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();

        this.request.send("GET", path, null, null, requestOptions);

        RecordedRequest request = OkHttpRequestorTest.server.takeRequest();
        String header = request.getHeader("Authorization");
        assertNotNull(header);
        assertEquals("Bearer " + requestOptions.apiKey, header);
    }

    @Test
    void APIバージョンを設定した場合はAPIバージョンヘッダを出力する() throws KaradenException, InterruptedException, URISyntaxException {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody("");
        OkHttpRequestorTest.server.enqueue(response);

        String path = "test";
        String apiVersion = "api_version";
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(OkHttpRequestorTest.server.url("").uri().resolve(new URI("./")).toString())
            .withApiVersion(apiVersion)
            .build();

        this.request.send("GET", path, null, null, requestOptions);

        RecordedRequest request = OkHttpRequestorTest.server.takeRequest();
        assertEquals(apiVersion, request.getHeader("Karaden-Version"));
    }

    @Test
    void APIバージョンを設定しない場合はデフォルトのAPIバージョンヘッダを出力する() throws KaradenException, InterruptedException, URISyntaxException {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody("");
        OkHttpRequestorTest.server.enqueue(response);

        String path = "test";
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(OkHttpRequestorTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();

        this.request.send("GET", path, null, null, requestOptions);

        RecordedRequest request = OkHttpRequestorTest.server.takeRequest();
        assertEquals(Config.apiVersion, request.getHeader("Karaden-Version"));
    }

    @Test
    void proxyが設定されていたら通信先はproxyでHTTPのリクエスト行はapiBase() throws KaradenException, InterruptedException, URISyntaxException {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody("");
        OkHttpRequestorTest.server.enqueue(response);

        String path = "test";
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(TestHelper.API_BASE)
            .withProxy(OkHttpRequestorTest.server.toProxyAddress())
            .build();
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        this.request.send("POST", path, null, data, requestOptions);

        RecordedRequest request = OkHttpRequestorTest.server.takeRequest();
        assertEquals(OkHttpRequestorTest.server.url("/").toString(), request.getRequestUrl().toString());
        assertEquals(String.format("%s/%s", requestOptions.getBaseUri(), path), request.getRequestLine().split(" ")[1]);
    }

    @Test
    void connection_timeoutが設定されたらConnectionExceptionが発生すること() throws InterruptedException, URISyntaxException {
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            //ルーティング不可能なIPアドレスに接続を試みる
            .withApiBase("http://10.255.255.1")
            .withConnectionTimeout(10)
            .build();

        assertThrows(ConnectionException.class, () -> this.request.send("GET", "", null, null, requestOptions));
    }

    @Test
    void read_timeoutが設定されたらSocketTimeoutExceptionが発生すること() throws InterruptedException, URISyntaxException {
        MockResponse response = new MockResponse();
        response.setSocketPolicy(SocketPolicy.NO_RESPONSE);
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody("");
        OkHttpRequestorTest.server.enqueue(response);

        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(OkHttpRequestorTest.server.url("").uri().resolve(new URI("./")).toString())
            .withReadTimeout(10)
            .build();

        assertThrows(ConnectionException.class, () -> this.request.send("GET", "", null, null, requestOptions));

        OkHttpRequestorTest.server.takeRequest();
    }
}
