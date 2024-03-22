package jp.karaden;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.karaden.exception.KaradenException;
import jp.karaden.model.BulkFile;
import jp.karaden.model.BulkMessage;
import jp.karaden.model.Collection;
import jp.karaden.model.Error;
import jp.karaden.model.Message;
import jp.karaden.param.message.MessageCancelParams;
import jp.karaden.param.message.MessageCreateParams;
import jp.karaden.param.message.MessageDetailParams;
import jp.karaden.param.message.MessageListParams;
import jp.karaden.param.message.bulk.BulkMessageCreateParams;
import jp.karaden.param.message.bulk.BulkMessageListMessageParams;
import jp.karaden.param.message.bulk.BulkMessageShowParams;

public class MockTest {
    @Test
    void 一覧() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        OffsetDateTime datetime = OffsetDateTime.parse("2020-01-31T23:59:59+09:00:00");
        MessageListParams params = MessageListParams.newBuilder()
            .withServiceId(1)
            .withStatus("done")
            .withStartAt(datetime)
            .withEndAt(datetime)
            .withPage(0)
            .withPerPage(100)
            .withTag("string")
            .withResult("done")
            .withSentResult("none")
            .withTo("09012345678")
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .build();
        Collection messages = Message.list(params, requestOptions);

        assertEquals("list", messages.getObject());
        assertEquals(true, messages.hasMore());
        List<Message> data = messages.getData();
        assertEquals(1, data.size());
        Message message = data.get(0);
        assertEquals("82bdf9de-a532-4bf5-86bc-c9a1366e5f4a", message.getId());
        assertEquals("message", message.getObject());
        assertEquals(1, message.getServiceId());
        assertEquals(1, message.getBillingAddressId());
        assertEquals("09012345678", message.getTo());
        assertEquals("本文", message.getBody());
        List<String> tags = message.getTags();
        assertEquals(1, tags.size());
        assertEquals("string", tags.get(0));
        assertEquals(true, message.isShorten());
        assertEquals(true, message.isShortenClicked());
        assertEquals("done", message.getResult());
        assertEquals("done", message.getStatus());
        assertEquals("none", message.getSentResult());
        assertEquals("docomo", message.getCarrier());
        assertEquals(datetime, message.getScheduledAt());
        assertEquals(datetime, message.getLimitedAt());
        assertEquals(datetime, message.getSentAt());
        assertEquals(datetime, message.getReceivedAt());
        assertEquals(datetime, message.getChargedAt());
        assertEquals(datetime, message.getCreatedAt());
        assertEquals(datetime, message.getUpdatedAt());
    }

    @Test
    void 作成() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        OffsetDateTime datetime = OffsetDateTime.parse("2020-01-31T23:59:59+09:00:00");
        MessageCreateParams params = MessageCreateParams.newBuilder()
            .withServiceId(1)
            .withTo("09012345678")
            .withBody("本文")
            .withIsShorten(true)
            .withLimitedAt(datetime)
            .withScheduledAt(datetime)
            .withTags(Arrays.asList("タグ１", "タグ２", "タグ３"))
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .build();
        Message message = Message.create(params, requestOptions);

        assertEquals("82bdf9de-a532-4bf5-86bc-c9a1366e5f4a", message.getId());
        assertEquals("message", message.getObject());
        assertEquals(1, message.getServiceId());
        assertEquals(1, message.getBillingAddressId());
        assertEquals("09012345678", message.getTo());
        assertEquals("本文", message.getBody());
        List<String> tags = message.getTags();
        assertEquals(1, tags.size());
        assertEquals("string", tags.get(0));
        assertEquals(true, message.isShorten());
        assertEquals(true, message.isShortenClicked());
        assertEquals("done", message.getResult());
        assertEquals("done", message.getStatus());
        assertEquals("none", message.getSentResult());
        assertEquals("docomo", message.getCarrier());
        assertEquals(datetime, message.getScheduledAt());
        assertEquals(datetime, message.getLimitedAt());
        assertEquals(datetime, message.getSentAt());
        assertEquals(datetime, message.getReceivedAt());
        assertEquals(datetime, message.getChargedAt());
        assertEquals(datetime, message.getCreatedAt());
        assertEquals(datetime, message.getUpdatedAt());
    }

    @Test
    void 詳細() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        OffsetDateTime datetime = OffsetDateTime.parse("2020-01-31T23:59:59+09:00:00");
        MessageDetailParams params = MessageDetailParams.newBuilder()
            .withId("82bdf9de-a532-4bf5-86bc-c9a1366e5f4a")
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .build();
        Message message = Message.detail(params, requestOptions);

        assertEquals("82bdf9de-a532-4bf5-86bc-c9a1366e5f4a", message.getId());
        assertEquals("message", message.getObject());
        assertEquals(1, message.getServiceId());
        assertEquals(1, message.getBillingAddressId());
        assertEquals("09012345678", message.getTo());
        assertEquals("本文", message.getBody());
        List<String> tags = message.getTags();
        assertEquals(1, tags.size());
        assertEquals("string", tags.get(0));
        assertEquals(true, message.isShorten());
        assertEquals(true, message.isShortenClicked());
        assertEquals("done", message.getResult());
        assertEquals("done", message.getStatus());
        assertEquals("none", message.getSentResult());
        assertEquals("docomo", message.getCarrier());
        assertEquals(datetime, message.getScheduledAt());
        assertEquals(datetime, message.getLimitedAt());
        assertEquals(datetime, message.getSentAt());
        assertEquals(datetime, message.getReceivedAt());
        assertEquals(datetime, message.getChargedAt());
        assertEquals(datetime, message.getCreatedAt());
        assertEquals(datetime, message.getUpdatedAt());
    }

    @Test
    void キャンセル() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        OffsetDateTime datetime = OffsetDateTime.parse("2020-01-31T23:59:59+09:00:00");
        MessageCancelParams params = MessageCancelParams.newBuilder()
            .withId("82bdf9de-a532-4bf5-86bc-c9a1366e5f4a")
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .build();
        Message message = Message.cancel(params, requestOptions);

        assertEquals("82bdf9de-a532-4bf5-86bc-c9a1366e5f4a", message.getId());
        assertEquals("message", message.getObject());
        assertEquals(1, message.getServiceId());
        assertEquals(1, message.getBillingAddressId());
        assertEquals("09012345678", message.getTo());
        assertEquals("本文", message.getBody());
        List<String> tags = message.getTags();
        assertEquals(1, tags.size());
        assertEquals("string", tags.get(0));
        assertEquals(true, message.isShorten());
        assertEquals(true, message.isShortenClicked());
        assertEquals("done", message.getResult());
        assertEquals("done", message.getStatus());
        assertEquals("none", message.getSentResult());
        assertEquals("docomo", message.getCarrier());
        assertEquals(datetime, message.getScheduledAt());
        assertEquals(datetime, message.getLimitedAt());
        assertEquals(datetime, message.getSentAt());
        assertEquals(datetime, message.getReceivedAt());
        assertEquals(datetime, message.getChargedAt());
        assertEquals(datetime, message.getCreatedAt());
        assertEquals(datetime, message.getUpdatedAt());
    }

    @Test
    void 一括送信用のアップロード先URL取得() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        OffsetDateTime datetime = OffsetDateTime.parse("2020-01-31T23:59:59+09:00:00");
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .build();
        BulkFile bulkFile = BulkFile.create(requestOptions);

        assertEquals("82bdf9de-a532-4bf5-86bc-c9a1366e5f4a", bulkFile.getId());
        assertEquals("bulk_file", bulkFile.getObject());
        assertEquals("https://example.com", bulkFile.getUrl());
        assertEquals(datetime, bulkFile.getCreatedAt());
        assertEquals(datetime, bulkFile.getExpiresAt());
    }

    @Test
    void 一括送信() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        OffsetDateTime datetime = OffsetDateTime.parse("2020-01-31T23:59:59+09:00:00");
        BulkMessageCreateParams params = BulkMessageCreateParams.newBuilder()
            .withBulkFileId("c439f89c-1ea3-7073-7021-1f127a850437")
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .build();
        BulkMessage bulkMessage = BulkMessage.create(params, requestOptions);

        assertEquals("82bdf9de-a532-4bf5-86bc-c9a1366e5f4a", bulkMessage.getId());
        assertEquals("bulk_message", bulkMessage.getObject());
        assertEquals("done", bulkMessage.getStatus());
        assertInstanceOf(Error.class, bulkMessage.getError());
        assertEquals(datetime, bulkMessage.getCreatedAt());
        assertEquals(datetime, bulkMessage.getUpdatedAt());
    }

    @Test
    void 一括送信状態取得() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        OffsetDateTime datetime = OffsetDateTime.parse("2020-01-31T23:59:59+09:00:00");
        BulkMessageShowParams params = BulkMessageShowParams.newBuilder()
            .withId("82bdf9de-a532-4bf5-86bc-c9a1366e5f4a")
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .build();
        BulkMessage bulkMessage = BulkMessage.show(params, requestOptions);

        assertEquals("82bdf9de-a532-4bf5-86bc-c9a1366e5f4a", bulkMessage.getId());
        assertEquals("bulk_message", bulkMessage.getObject());
        assertEquals("done", bulkMessage.getStatus());
        assertInstanceOf(Error.class, bulkMessage.getError());
        assertEquals(datetime, bulkMessage.getCreatedAt());
        assertEquals(datetime, bulkMessage.getUpdatedAt());
    }

    @Test
    void 一括送信結果取得() throws KaradenException, IOException, InterruptedException, URISyntaxException {
        BulkMessageListMessageParams params = BulkMessageListMessageParams.newBuilder()
            .withId("82bdf9de-a532-4bf5-86bc-c9a1366e5f4a")
            .build();
        RequestOptions requestOptions = TestHelper.getDefaultRequestOptionsBuilder()
            .build();
        String output = BulkMessage.listMessage(params, requestOptions);

        assertEquals(output, null);
    }
}
