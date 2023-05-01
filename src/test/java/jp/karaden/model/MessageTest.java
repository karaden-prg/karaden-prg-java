package jp.karaden.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import jp.karaden.RequestOptions;
import jp.karaden.TestHelper;
import jp.karaden.exception.KaradenException;
import jp.karaden.param.message.MessageCancelParams;
import jp.karaden.param.message.MessageCreateParams;
import jp.karaden.param.message.MessageDetailParams;
import jp.karaden.param.message.MessageListParams;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class MessageTest {
    protected static MockWebServer server;

    @BeforeAll
    public static void setUpClass() throws URISyntaxException {
        MockWebServer server = new MockWebServer();
        MessageTest.server = server;

    }

    @AfterAll
    public static void tearDownClass() throws IOException {
        MessageTest.server.shutdown();
    }

    @Test
    void メッセージを作成できる() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        JSONObject json = new JSONObject();
        json.put("object", "message");
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody(json.toString());
        MessageTest.server.enqueue(response);

        MessageCreateParams in = MessageCreateParams.newBuilder()
            .withServiceId(1)
            .withTo("to")
            .withBody("body")
            .withTags(Arrays.asList("a", "b"))
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(MessageTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        Message out = Message.create(in, requestOptions);

        RecordedRequest request = MessageTest.server.takeRequest();
        URI uri = URI.create(String.format("%s/messages", requestOptions.getBaseUri()));
        assertEquals("POST", request.getMethod());
        assertEquals(uri.getPath(), request.getPath());
        assertEquals("service_id=1&to=to&body=body&tags%5B%5D=a&tags%5B%5D=b", request.getBody().readUtf8());
        assertEquals("application/x-www-form-urlencoded", request.getHeader("Content-Type"));
        assertEquals(json.getString("object"), out.getObject());
    }

    @Test
    void メッセージの詳細を取得できる() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        JSONObject json = new JSONObject();
        json.put("object", "message");
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody(json.toString());
        MessageTest.server.enqueue(response);

        MessageDetailParams in = MessageDetailParams.newBuilder()
            .withId("id")
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(MessageTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        Message out = Message.detail(in, requestOptions);

        RecordedRequest request = MessageTest.server.takeRequest();
        URI uri = URI.create(String.format("%s/messages/%s", requestOptions.getBaseUri(), in.id));
        assertEquals("GET", request.getMethod());
        assertEquals(uri.getPath(), request.getPath());
        assertEquals(json.getString("object"), out.getObject());
    }

    @Test
    void メッセージの一覧を取得できる() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        JSONObject json = new JSONObject();
        json.put("object", "list");
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody(json.toString());
        MessageTest.server.enqueue(response);

        MessageListParams in = MessageListParams.newBuilder()
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(MessageTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        Collection out = Message.list(in, requestOptions);

        RecordedRequest request = MessageTest.server.takeRequest();
        URI uri = URI.create(String.format("%s/messages", requestOptions.getBaseUri()));
        assertEquals("GET", request.getMethod());
        assertEquals(uri.getPath(), request.getPath());
        assertEquals(json.getString("object"), out.getObject());
    }

    @Test
    void メッセージの送信をキャンセルできる() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        JSONObject json = new JSONObject();
        json.put("object", "message");
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody(json.toString());
        MessageTest.server.enqueue(response);

        MessageCancelParams in = MessageCancelParams.newBuilder()
            .withId("id")
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(MessageTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        Message out = Message.cancel(in, requestOptions);

        RecordedRequest request = MessageTest.server.takeRequest();
        URI uri = URI.create(String.format("%s/messages/%s/cancel", requestOptions.getBaseUri(), in.id));
        assertEquals("POST", request.getMethod());
        assertEquals(uri.getPath(), request.getPath());
        assertEquals(json.getString("object"), out.getObject());
    }

    @Test
    void serviceIdを出力できる() {
        Integer expected = 1;
        Message message = new Message();
        message.setProperty("service_id", expected);
        assertEquals(expected, message.getServiceId());
    }

    @Test
    void billingAddressIdを出力できる() {
        Integer expected = 1;
        Message message = new Message();
        message.setProperty("billing_address_id", expected);
        assertEquals(expected, message.getBillingAddressId());
    }

    @Test
    void toを出力できる() {
        String expected = "1234567890";
        Message message = new Message();
        message.setProperty("to", expected);
        assertEquals(expected, message.getTo());
    }

    @Test
    void bodyを出力できる() {
        String expected = "body";
        Message message = new Message();
        message.setProperty("body", expected);
        assertEquals(expected, message.getBody());
    }

    @Test
    void tagsを出力できる() {
        List<String> expected = Arrays.asList("tag");
        Message message = new Message();
        message.setProperty("tags", expected);
        assertArrayEquals(expected.toArray(), message.getTags().toArray());
    }

    @Test
    void statusを出力できる() {
        String expected = "done";
        Message message = new Message();
        message.setProperty("status", expected);
        assertEquals(expected, message.getStatus());
    }

    @Test
    void resultを出力できる() {
        String expected = "none";
        Message message = new Message();
        message.setProperty("result", expected);
        assertEquals(expected, message.getResult());
    }

    @Test
    void sentResultを出力できる() {
        String expected = "done";
        Message message = new Message();
        message.setProperty("sent_result", expected);
        assertEquals(expected, message.getSentResult());
    }

    @Test
    void carrierを出力できる() {
        String expected = "docomo";
        Message message = new Message();
        message.setProperty("carrier", expected);
        assertEquals(expected, message.getCarrier());
    }

    @Test
    void scheduledAtを出力できる() {
        String expected = "2022-12-09T00:00:00+09:00";
        Message message = new Message();
        message.setProperty("scheduled_at", expected);
        assertEquals(OffsetDateTime.parse(expected), message.getScheduledAt());
    }

    @Test
    void limitedAtを出力できる() {
        String expected = "2022-12-09T00:00:00+09:00";
        Message message = new Message();
        message.setProperty("limited_at", expected);
        assertEquals(OffsetDateTime.parse(expected), message.getLimitedAt());
    }

    @Test
    void sentAtを出力できる() {
        String expected = "2022-12-09T00:00:00+09:00";
        Message message = new Message();
        message.setProperty("sent_at", expected);
        assertEquals(OffsetDateTime.parse(expected), message.getSentAt());
    }

    @Test
    void receivedAtを出力できる() {
        String expected = "2022-12-09T00:00:00+09:00";
        Message message = new Message();
        message.setProperty("received_at", expected);
        assertEquals(OffsetDateTime.parse(expected), message.getReceivedAt());
    }

    @Test
    void chargedAtを出力できる() {
        String expected = "2022-12-09T00:00:00+09:00";
        Message message = new Message();
        message.setProperty("charged_at", expected);
        assertEquals(OffsetDateTime.parse(expected), message.getChargedAt());
    }

    @Test
    void createdAtを出力できる() {
        String expected = "2022-12-09T00:00:00+09:00";
        Message message = new Message();
        message.setProperty("created_at", expected);
        assertEquals(OffsetDateTime.parse(expected), message.getCreatedAt());
    }

    @Test
    void updatedAtを出力できる() {
        String expected = "2022-12-09T00:00:00+09:00";
        Message message = new Message();
        message.setProperty("updated_at", expected);
        assertEquals(OffsetDateTime.parse(expected), message.getUpdatedAt());
    }
}
