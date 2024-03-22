package jp.karaden.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import jp.karaden.RequestOptions;
import jp.karaden.TestHelper;
import jp.karaden.exception.KaradenException;
import jp.karaden.param.message.bulk.BulkMessageCreateParams;
import jp.karaden.param.message.bulk.BulkMessageListMessageParams;
import jp.karaden.param.message.bulk.BulkMessageShowParams;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class BulkMessageTest {
    protected static MockWebServer server;

    @BeforeAll
    public static void setUpClass() throws URISyntaxException {
        MockWebServer server = new MockWebServer();
        BulkMessageTest.server = server;
    }

    @AfterAll
    public static void tearDownClass() throws IOException {
        BulkMessageTest.server.shutdown();
    }

    @Test
    void 一括送信用CSVのアップロード先URLを発行できる() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        JSONObject json = new JSONObject();
        json.put("object", "bulk_message");
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody(json.toString());
        BulkMessageTest.server.enqueue(response);

        BulkMessageCreateParams in = BulkMessageCreateParams.newBuilder()
            .withBulkFileId("c439f89c-1ea3-7073-7021-1f127a850437")
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(BulkMessageTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        BulkMessage out = BulkMessage.create(in, requestOptions);

        RecordedRequest request = BulkMessageTest.server.takeRequest();
        URI uri = URI.create(String.format("%s/messages/bulks", requestOptions.getBaseUri()));
        assertEquals("POST", request.getMethod());
        assertEquals(uri.getPath(), request.getPath());
        assertEquals("bulk_file_id=c439f89c-1ea3-7073-7021-1f127a850437", request.getBody().readUtf8());
        assertEquals("application/x-www-form-urlencoded", request.getHeader("Content-Type"));
        assertEquals(json.getString("object"), out.getObject());
    }

    @Test
    void 一括送信メッセージの詳細を取得できる() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        JSONObject json = new JSONObject();
        json.put("object", "bulk_message");
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody(json.toString());
        BulkMessageTest.server.enqueue(response);

        BulkMessageShowParams in = BulkMessageShowParams.newBuilder()
            .withId("c439f89c-1ea3-7073-7021-1f127a850437")
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(BulkMessageTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        BulkMessage out = BulkMessage.show(in, requestOptions);

        RecordedRequest request = BulkMessageTest.server.takeRequest();
        URI uri = URI.create(String.format("%s/messages/bulks/%s", requestOptions.getBaseUri(), in.id));
        assertEquals("GET", request.getMethod());
        assertEquals(uri.getPath(), request.getPath());
        assertEquals(json.getString("object"), out.getObject());
    }

    @Test
    void 一括送信メッセージの結果を取得できる() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        JSONObject json = new JSONObject();
        json.put("object", "bulk_message");
        String expectUrl = "http://example.com";
        MockResponse response = new MockResponse();
        response.setResponseCode(302);
        response.setHeader("Location", expectUrl);
        response.setBody(json.toString());
        BulkMessageTest.server.enqueue(response);

        BulkMessageListMessageParams in = BulkMessageListMessageParams.newBuilder()
            .withId("c439f89c-1ea3-7073-7021-1f127a850437")
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(BulkMessageTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        String out = BulkMessage.listMessage(in, requestOptions);

        RecordedRequest request = BulkMessageTest.server.takeRequest();
        URI uri = URI.create(String.format("%s/messages/bulks/%s/messages", requestOptions.getBaseUri(), in.id));
        assertEquals("GET", request.getMethod());
        assertEquals(uri.getPath(), request.getPath());
        assertEquals(expectUrl, out);
    }

    static List<String> locationProvider() {
        return Arrays.asList("location", "LOCATION");
    }

    @ParameterizedTest
    @MethodSource("locationProvider")
    void Locationが大文字小文字関係なく一括送信メッセージの結果を取得できる(String location) throws KaradenException, IOException, InterruptedException, URISyntaxException {
        JSONObject json = new JSONObject();
        json.put("object", "bulk_message");
        String expectUrl = "http://example.com";
        MockResponse response = new MockResponse();
        response.setResponseCode(302);
        response.setHeader(location, expectUrl);
        response.setBody(json.toString());
        BulkMessageTest.server.enqueue(response);

        BulkMessageListMessageParams in = BulkMessageListMessageParams.newBuilder()
            .withId("c439f89c-1ea3-7073-7021-1f127a850437")
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(BulkMessageTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        String out = BulkMessage.listMessage(in, requestOptions);

        RecordedRequest request = BulkMessageTest.server.takeRequest();
        URI uri = URI.create(String.format("%s/messages/bulks/%s/messages", requestOptions.getBaseUri(), in.id));
        assertEquals("GET", request.getMethod());
        assertEquals(uri.getPath(), request.getPath());
        assertEquals(expectUrl, out);
    }

    @Test
    void statusを出力できる() {
        String expected = "processing";
        BulkMessage bulkMessage = new BulkMessage();
        bulkMessage.setProperty("status", expected);
        assertEquals(expected, bulkMessage.getStatus());
    }

    @Test
    void 受付エラーがない場合はerrorは出力されない() {
        String expected = null;
        BulkMessage bulkMessage = new BulkMessage();
        bulkMessage.setProperty("error", expected);
        assertEquals(expected, bulkMessage.getError());
    }

    @Test
    void 受付エラーがあった場合はerrorが出力される() {
        Error expected = new Error();
        BulkMessage bulkMessage = new BulkMessage();
        bulkMessage.setProperty("error", expected);
        assertInstanceOf(Error.class, bulkMessage.getError());
    }

    @Test
    void createdAtを出力できる() {
        String expected = "2022-12-09T00:00:00+09:00";
        BulkMessage bulkMessage = new BulkMessage();
        bulkMessage.setProperty("created_at", expected);
        assertEquals(OffsetDateTime.parse(expected), bulkMessage.getCreatedAt());
    }

    @Test
    void updatedAtを出力できる() {
        String expected = "2022-12-09T00:00:00+09:00";
        BulkMessage bulkMessage = new BulkMessage();
        bulkMessage.setProperty("updated_at", expected);
        assertEquals(OffsetDateTime.parse(expected), bulkMessage.getUpdatedAt());
    }
}
