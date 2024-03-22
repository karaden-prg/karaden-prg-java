package jp.karaden.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jp.karaden.RequestOptions;
import jp.karaden.TestHelper;
import jp.karaden.exception.KaradenException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class BulkFileTest {
    protected static MockWebServer server;

    @BeforeAll
    public static void setUpClass() throws URISyntaxException {
        MockWebServer server = new MockWebServer();
        BulkFileTest.server = server;
    }

    @AfterAll
    public static void tearDownClass() throws IOException {
        BulkFileTest.server.shutdown();
    }

    @Test
    void 一括送信用CSVのアップロード先URLを発行できる() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        JSONObject json = new JSONObject();
        json.put("object", "bulk_file");
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("Content-Type", "application/json");
        response.setBody(json.toString());
        BulkFileTest.server.enqueue(response);

        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(BulkFileTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        BulkFile out = BulkFile.create(requestOptions);

        RecordedRequest request = BulkFileTest.server.takeRequest();
        URI uri = URI.create(String.format("%s/messages/bulks/files", requestOptions.getBaseUri()));
        assertEquals("POST", request.getMethod());
        assertEquals(uri.getPath(), request.getPath());
        assertEquals(json.getString("object"), out.getObject());
    }

    @Test
    void urlを出力できる() {
        String expected = "https://example.com/";
        BulkFile bulkFile = new BulkFile();
        bulkFile.setProperty("url", expected);
        assertEquals(expected, bulkFile.getUrl());
    }

    @Test
    void createdAtを出力できる() {
        String expected = "2022-12-09T00:00:00+09:00";
        BulkFile bulkFile = new BulkFile();
        bulkFile.setProperty("created_at", expected);
        assertEquals(OffsetDateTime.parse(expected), bulkFile.getCreatedAt());
    }

    @Test
    void expiresAtを出力できる() {
        String expected = "2022-12-09T00:00:00+09:00";
        BulkFile bulkFile = new BulkFile();
        bulkFile.setProperty("expires_at", expected);
        assertEquals(OffsetDateTime.parse(expected), bulkFile.getExpiresAt());
    }
}
