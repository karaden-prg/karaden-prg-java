package jp.karaden;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jp.karaden.exception.FileUploadFailedException;
import jp.karaden.model.KaradenObject;
import jp.karaden.model.Message;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

class UtilityTest {
    protected static MockWebServer server;

    @BeforeEach
    public void setUp() throws URISyntaxException, IOException {
        UtilityTest.server = new MockWebServer();
    }

    @AfterEach
    public void tearDown() throws IOException {
        UtilityTest.server.shutdown();
    }

    static List<?> primitiveValueProvider() {
        return Arrays.asList("string", 123, 0, true, false, null);
    }

    static List<?> arrayObjectItemProvider() {
        return Arrays.asList(
            Arguments.arguments(null, null, KaradenObject.class),
            Arguments.arguments("object", null, KaradenObject.class),
            Arguments.arguments("object", "test", KaradenObject.class),
            Arguments.arguments("object", "message", Message.class)
        );
    }

    @Test
    void objectのフィールドが存在しない場合はKaradenObjectが返る() {
        JSONObject contents = new JSONObject("{\"test\": \"test\"}");
        RequestOptions requestOptions = new RequestOptions();

        KaradenObject object = Utility.convertToKaradenObject(contents, requestOptions);

        assertInstanceOf(KaradenObject.class, object);
    }

    @Test
    void objectのフィールドが存在してObjectTypesのマッピングが存在する場合はオブジェクトが返る() {
        JSONObject contents = new JSONObject("{\"object\": \"message\"}");
        RequestOptions requestOptions = new RequestOptions();

        KaradenObject object = Utility.convertToKaradenObject(contents, requestOptions);

        assertInstanceOf(Message.class, object);
    }

    @Test
    void objectのフィールドが存在してObjectTypesのマッピングに存在しない場合はKaradenObjectが返る() {
        JSONObject contents = new JSONObject("{\"object\": \"test\"}");
        RequestOptions requestOptions = new RequestOptions();

        KaradenObject object = Utility.convertToKaradenObject(contents, requestOptions);

        assertInstanceOf(KaradenObject.class, object);
    }

    @ParameterizedTest
    @MethodSource("primitiveValueProvider")
    void プリミティブな値はデシリアライズしても変わらない(Object value) {
        JSONObject contents = new JSONObject();
        contents.put("test", value);
        RequestOptions requestOptions = new RequestOptions();

        KaradenObject object = Utility.convertToKaradenObject(contents, requestOptions);

        assertInstanceOf(KaradenObject.class, object);
        assertEquals(value, object.getProperty("test"));
    }

    @ParameterizedTest
    @MethodSource("primitiveValueProvider")
    void プリミティブな値の配列の要素はデシリアライズしても変わらない(Object value) {
        JSONObject contents = new JSONObject();
        contents.put("test", new JSONArray(Arrays.asList(value)));
        RequestOptions requestOptions = new RequestOptions();

        KaradenObject object = Utility.convertToKaradenObject(contents, requestOptions);

        assertInstanceOf(KaradenObject.class, object);
        assertInstanceOf(List.class, object.getProperty("test"));
        assertEquals(value, ((List<?>)object.getProperty("test")).get(0));
    }

    @Test
    void 配列の配列もサポートする() {
        String value = "test";
        JSONObject contents = new JSONObject(String.format("{\"test\": [[\"%s\"]]}", value));
        RequestOptions requestOptions = new RequestOptions();

        KaradenObject object = Utility.convertToKaradenObject(contents, requestOptions);

        assertInstanceOf(KaradenObject.class, object);
        assertInstanceOf(List.class, object.getProperty("test"));
        assertEquals(1, ((List<?>)object.getProperty("test")).size());
        assertInstanceOf(List.class, ((List<?>)object.getProperty("test")).get(0));
        assertEquals(value, ((List<?>)(((List<?>)object.getProperty("test")).get(0))).get(0));
    }

    @Test
    void 配列のオブジェクトもサポートする() {
        String value = "test";
        JSONObject contents = new JSONObject(String.format("{\"test\": [{\"test\": \"%s\"}]}", value));
        RequestOptions requestOptions = new RequestOptions();

        KaradenObject object = Utility.convertToKaradenObject(contents, requestOptions);

        assertInstanceOf(KaradenObject.class, object);
        assertInstanceOf(List.class, object.getProperty("test"));
        assertEquals(1, ((List<?>)object.getProperty("test")).size());
        assertInstanceOf(KaradenObject.class, ((List<?>)object.getProperty("test")).get(0));
        assertEquals(value, ((KaradenObject)(((List<?>)object.getProperty("test")).get(0))).getProperty("test"));
    }

    @ParameterizedTest
    @MethodSource("arrayObjectItemProvider")
    void オブジェクトの配列の要素はデシリアライズするとKaradenObjectに変換される(String key, String value, Class<?> cls) {
        JSONObject contents = new JSONObject(String.format("{\"test\": [%s]}", key == null ? "{\"test\": \"test\"}" : String.format("{\"%s\": \"%s\", \"test\": \"test\"}", key, value)));
        RequestOptions requestOptions = new RequestOptions();

        KaradenObject object = Utility.convertToKaradenObject(contents, requestOptions);

        assertInstanceOf(KaradenObject.class, object);
        assertInstanceOf(List.class, object.getProperty("test"));
        assertEquals(1, ((List<?>)object.getProperty("test")).size());
        assertInstanceOf(KaradenObject.class, ((List<?>)object.getProperty("test")).get(0));
        assertEquals("test", ((KaradenObject)(((List<?>)object.getProperty("test")).get(0))).getProperty("test"));
    }

    @Test
    void 指定のURLにfileパスのファイルをPUTメソッドでリクエストする() throws IOException, FileUploadFailedException, InterruptedException {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setBody("");
        UtilityTest.server.enqueue(response);

        File tempFile = File.createTempFile("temp", null);
        String filename = tempFile.getAbsolutePath();
        String signedUrl = UtilityTest.server.url("/").toString();

        Utility.putSignedUrl(signedUrl, filename);

        RecordedRequest request = UtilityTest.server.takeRequest();
        assertEquals("PUT", request.getMethod());
        assertEquals(signedUrl, request.getRequestUrl().toString());
        assertEquals("application/octet-stream", request.getHeader("Content-Type"));
    }

    @Test
    void レスポンスコードが200以外だとFileUploadFailedExceptionが発生する() throws IOException, FileUploadFailedException, InterruptedException {
        MockResponse response = new MockResponse();
        response.setResponseCode(403);
        response.setBody("");
        UtilityTest.server.enqueue(response);

        File tempFile = File.createTempFile("temp", null);
        String filename = tempFile.getAbsolutePath();
        String signedUrl = UtilityTest.server.url("/").toString();

        assertThrows(FileUploadFailedException.class, () -> Utility.putSignedUrl(signedUrl, filename));
    }

    @Test
    void 例外が発生するとFileUploadFailedExceptionをリスローする() throws IOException, FileUploadFailedException, InterruptedException {
        MockResponse response = new MockResponse();
        response.setResponseCode(403);
        response.setBody("");
        response.setSocketPolicy(SocketPolicy.DISCONNECT_AT_START);
        UtilityTest.server.enqueue(response);

        File tempFile = File.createTempFile("temp", null);
        String filename = tempFile.getAbsolutePath();
        String signedUrl = UtilityTest.server.url("/").toString();

        assertThrows(FileUploadFailedException.class, () -> Utility.putSignedUrl(signedUrl, filename));
    }

    @Test
    void ContentTypeを指定できる() throws IOException, FileUploadFailedException, InterruptedException {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setBody("");
        UtilityTest.server.enqueue(response);

        File tempFile = File.createTempFile("temp", null);
        String filename = tempFile.getAbsolutePath();
        String signedUrl = UtilityTest.server.url("/").toString();
        String contentType = "text/csv";

        Utility.putSignedUrl(signedUrl, filename, contentType);

        RecordedRequest request = UtilityTest.server.takeRequest();
        assertEquals("PUT", request.getMethod());
        assertEquals(signedUrl, request.getRequestUrl().toString());
        assertEquals(contentType, request.getHeader("Content-Type"));
    }
}
