package jp.karaden.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jp.karaden.RequestOptions;
import jp.karaden.TestHelper;
import jp.karaden.exception.BulkMessageCreateFailedException;
import jp.karaden.exception.BulkMessageListMessageRetryLimitExceedException;
import jp.karaden.exception.BulkMessageShowRetryLimitExceedException;
import jp.karaden.exception.FileNotFoundException;
import jp.karaden.exception.KaradenException;
import jp.karaden.model.BulkMessage;
import jp.karaden.param.message.bulk.BulkMessageDownloadParams;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class BulkMessageServiceTest {
    protected static MockWebServer server;

    @BeforeAll
    public static void setUpClass() throws URISyntaxException, IOException {
        MockWebServer server = new MockWebServer();
        BulkMessageServiceTest.server = server;
    }

    @AfterAll
    public static void tearDownClass() throws IOException {
        BulkMessageServiceTest.server.shutdown();
    }

    @Test
    void bulkMessageオブジェクトが返る() throws URISyntaxException, IOException, KaradenException, InterruptedException{
        JSONObject json = new JSONObject();
        json.put("id", "741121d7-3f7e-ed85-9fac-28d87835528e");
        json.put("object", "bulk_file");
        json.put("url", BulkMessageServiceTest.server.url("/").toString());
        json.put("created_at", "2023-12-01T15:00:00.0Z");
        json.put("expired_at", "2023-12-01T15:00:00.0Z");
        MockResponse bulkFileResponse = new MockResponse();
        bulkFileResponse.setResponseCode(200);
        bulkFileResponse.setHeader("Content-Type", "application/json");
        bulkFileResponse.setBody(json.toString());
        BulkMessageServiceTest.server.enqueue(bulkFileResponse);

        MockResponse cloudFrontResponse = new MockResponse();
        cloudFrontResponse.setResponseCode(200);
        cloudFrontResponse.setBody("");
        BulkMessageServiceTest.server.enqueue(cloudFrontResponse);

        json.clear();
        json.put("id", "ef931182-80ff-611c-c878-871a08bb5a6a");
        json.put("object", "bulk_message");
        json.put("status", "processing");
        json.put("created_at", "2023-12-01T15:00:00.0Z");
        json.put("updated_at", "2023-12-01T15:00:00.0Z");
        MockResponse bulkMessageResponse = new MockResponse();
        bulkMessageResponse.setResponseCode(200);
        bulkMessageResponse.setHeader("Content-Type", "application/json");
        bulkMessageResponse.setBody(json.toString());
        BulkMessageServiceTest.server.enqueue(bulkMessageResponse);

        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(BulkMessageServiceTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        File tempFile = File.createTempFile("temp", null);
        String filename = tempFile.getAbsolutePath();

        BulkMessage bulkMessage = BulkMessageService.create(filename, requestOptions);
        assertEquals("bulk_message", bulkMessage.getObject());
    }


    @Test
    void ファイルが存在しない場合はエラー() throws URISyntaxException, IOException, KaradenException, InterruptedException{
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(BulkMessageServiceTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();
        String filename = "test.csv";

        assertThrows(FileNotFoundException.class, () -> BulkMessageService.create(filename, requestOptions));
    }

    @Test
    void ファイルがダウンロードできる() throws URISyntaxException, IOException, KaradenException, InterruptedException{
        String filename = "file.csv";

        JSONObject json = new JSONObject();
        json.put("id", "ef931182-80ff-611c-c878-871a08bb5a6a");
        json.put("object", "bulk_message");
        json.put("status", "done");
        json.put("created_at", "2023-12-01T15:00:00.0Z");
        json.put("updated_at", "2023-12-01T15:00:00.0Z");
        MockResponse showResponse = new MockResponse();
        showResponse.setResponseCode(200);
        showResponse.setHeader("Content-Type", "application/json");
        showResponse.setBody(json.toString());
        server.enqueue(showResponse);

        MockResponse bulkMessageResponse = new MockResponse();
        bulkMessageResponse.setResponseCode(302);
        bulkMessageResponse.setHeader("Location", BulkMessageServiceTest.server.url("").uri().resolve(new URI("./")).toString() + UUID.randomUUID().toString());
        server.enqueue(bulkMessageResponse);

        String fileContent = "file content";
        MockResponse downloadResponse = new MockResponse();
        downloadResponse.setResponseCode(200);
        downloadResponse.setHeader("content-disposition", "attachment;filename=\"" + filename + "\";filename*=UTF-8''" + filename);
        downloadResponse.setBody(fileContent);
        server.enqueue(downloadResponse);

        Path tmpDir = Files.createTempDirectory("tempDir_");
        BulkMessageDownloadParams params = BulkMessageDownloadParams.newBuilder()
            .withId("c439f89c-1ea3-7073-7021-1f127a850437")
            .withDirectoryPath(tmpDir.toString())
            .withMaxRetries(1)
            .withRetryInterval(10)
            .build();

        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(BulkMessageServiceTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();

        BulkMessageService.download(params, requestOptions);

        assertTrue(Files.exists(Paths.get(tmpDir.toString() + "/" + filename)));
        String result = new String(Files.readAllBytes(FileSystems.getDefault().getPath(tmpDir.toString(), filename)));
        assertEquals(result, fileContent);
    }

    @Test
    void bulkMessageのstatusがdone以外でリトライ回数を超過した場合はエラー() throws URISyntaxException, IOException, KaradenException, InterruptedException{
        JSONObject json = new JSONObject();
        json.put("id", "ef931182-80ff-611c-c878-871a08bb5a6a");
        json.put("object", "bulk_message");
        json.put("status", "processing");
        json.put("created_at", "2023-12-01T15:00:00.0Z");
        json.put("updated_at", "2023-12-01T15:00:00.0Z");
        MockResponse showResponse = new MockResponse();
        showResponse.setResponseCode(200);
        showResponse.setHeader("Content-Type", "application/json");
        showResponse.setBody(json.toString());
        server.enqueue(showResponse);
        server.enqueue(showResponse);

        Path tmpDir = Files.createTempDirectory("tempDir_");
        BulkMessageDownloadParams params = BulkMessageDownloadParams.newBuilder()
            .withId("c439f89c-1ea3-7073-7021-1f127a850437")
            .withDirectoryPath(tmpDir.toString())
            .withMaxRetries(1)
            .withRetryInterval(10)
            .build();

        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(BulkMessageServiceTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();

        assertThrows(BulkMessageShowRetryLimitExceedException.class, () -> BulkMessageService.download(params, requestOptions));
    }

    @Test
    void 結果取得APIが202を返しリトライ回数を超過した場合はエラー() throws URISyntaxException, IOException, KaradenException, InterruptedException{
        JSONObject json = new JSONObject();
        json.put("id", "ef931182-80ff-611c-c878-871a08bb5a6a");
        json.put("object", "bulk_message");
        json.put("status", "done");
        json.put("created_at", "2023-12-01T15:00:00.0Z");
        json.put("updated_at", "2023-12-01T15:00:00.0Z");
        MockResponse showResponse = new MockResponse();
        showResponse.setResponseCode(200);
        showResponse.setHeader("Content-Type", "application/json");
        showResponse.setBody(json.toString());
        server.enqueue(showResponse);

        MockResponse bulkMessageResponse = new MockResponse();
        bulkMessageResponse.setResponseCode(202);
        server.enqueue(bulkMessageResponse);
        server.enqueue(bulkMessageResponse);

        Path tmpDir = Files.createTempDirectory("tempDir_");
        BulkMessageDownloadParams params = BulkMessageDownloadParams.newBuilder()
            .withId("c439f89c-1ea3-7073-7021-1f127a850437")
            .withDirectoryPath(tmpDir.toString())
            .withMaxRetries(1)
            .withRetryInterval(10)
            .build();

        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(BulkMessageServiceTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();

        assertThrows(BulkMessageListMessageRetryLimitExceedException.class, () -> BulkMessageService.download(params, requestOptions));
    }

    @Test
    void bulkMessageのstatusがerrorの場合はエラー() throws URISyntaxException, IOException, KaradenException, InterruptedException{
        JSONObject json = new JSONObject();
        json.put("id", "ef931182-80ff-611c-c878-871a08bb5a6a");
        json.put("object", "bulk_message");
        json.put("status", "error");
        json.put("created_at", "2023-12-01T15:00:00.0Z");
        json.put("updated_at", "2023-12-01T15:00:00.0Z");
        MockResponse showResponse = new MockResponse();
        showResponse.setResponseCode(200);
        showResponse.setHeader("Content-Type", "application/json");
        showResponse.setBody(json.toString());
        server.enqueue(showResponse);

        Path tmpDir = Files.createTempDirectory("tempDir_");
        BulkMessageDownloadParams params = BulkMessageDownloadParams.newBuilder()
            .withId("c439f89c-1ea3-7073-7021-1f127a850437")
            .withDirectoryPath(tmpDir.toString())
            .withMaxRetries(1)
            .withRetryInterval(10)
            .build();

        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .withApiBase(BulkMessageServiceTest.server.url("").uri().resolve(new URI("./")).toString())
            .build();

        assertThrows(BulkMessageCreateFailedException.class, () -> BulkMessageService.download(params, requestOptions));
    }
}
