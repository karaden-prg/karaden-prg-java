package jp.karaden.param.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class MessageCreateParamsTest {
    @Test
    void 正しいパスを生成できる() {
        MessageCreateParams params = new MessageCreateParams(0, "", "", null, null, null, null);

        assertEquals(MessageCreateParams.CONTEXT_PATH , params.toPath());
    }

    @Test
    void serviceIdを送信データにできる() {
        Integer expected = 1;
        MessageCreateParams params = new MessageCreateParams(expected, "", "", null, null, null, null);

        Map<String, ?> actual = params.toData();
        assertEquals(expected, actual.get("service_id"));
    }

    @Test
    void toを送信データにできる() {
        String expected = "to";
        MessageCreateParams params = new MessageCreateParams(0, expected, "", null, null, null, null);

        Map<String, ?> actual = params.toData();
        assertEquals(expected, actual.get("to"));
    }

    @Test
    void bodyを送信データにできる() {
        String expected = "body";
        MessageCreateParams params = new MessageCreateParams(0, "", expected, null, null, null, null);

        Map<String, ?> actual = params.toData();
        assertEquals(expected, actual.get("body"));
    }

    @Test
    void tagsを送信データにできる() {
        List<String> expected = Arrays.asList("tag");
        MessageCreateParams params = new MessageCreateParams(0, "", "", expected, null, null, null);

        Map<String, ?> actual = params.toData();
        assertArrayEquals(expected.toArray(new String[expected.size()]), ((List<?>)actual.get("tags")).toArray(new String[((List<?>)actual.get("tags")).size()]));
    }

    @Test
    void scheduledAtを送信データにできる() {
        OffsetDateTime expected = OffsetDateTime.of(2022, 12, 12, 0, 0, 0, 0, ZoneOffset.ofHours(9));
        MessageCreateParams params = new MessageCreateParams(0, "", "", null, null, expected, null);

        Map<String, ?> actual = params.toData();
        assertEquals(expected.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), actual.get("scheduled_at"));
    }

    @Test
    void scheduledAtを送信データにするとナノ秒は落ちる() {
        OffsetDateTime expected = OffsetDateTime.of(2022, 12, 12, 0, 0, 0, 30, ZoneOffset.ofHours(9));
        MessageCreateParams params = new MessageCreateParams(0, "", "", null, null, expected, null);

        Map<String, ?> actual = params.toData();
        assertEquals(expected.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), actual.get("scheduled_at"));
    }


    @Test
    void limitedAtを送信データにできる() {
        OffsetDateTime expected = OffsetDateTime.of(2022, 12, 12, 0, 0, 0, 0, ZoneOffset.ofHours(9));
        MessageCreateParams params = new MessageCreateParams(0, "", "", null, null, null, expected);

        Map<String, ?> actual = params.toData();
        assertEquals(expected.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), actual.get("limited_at"));
    }

    @Test
    void limitedAtを送信データにするとナノ秒は落ちる() {
        OffsetDateTime expected = OffsetDateTime.of(2022, 12, 12, 0, 0, 0, 30, ZoneOffset.ofHours(9));
        MessageCreateParams params = new MessageCreateParams(0, "", "", null, null, null, expected);

        Map<String, ?> actual = params.toData();
        assertEquals(expected.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), actual.get("limited_at"));
    }

    @Test
    void serviceIdを入力できる() {
        Integer expected = 1;
        MessageCreateParams params = MessageCreateParams.newBuilder()
            .withServiceId(expected)
            .build();

        assertEquals(expected, params.serviceId);
    }

    @Test
    void toを入力できる() {
        String expected = "to";
        MessageCreateParams params = MessageCreateParams.newBuilder()
            .withTo(expected)
            .build();

        assertEquals(expected, params.to);
    }

    @Test
    void bodyを入力できる() {
        String expected = "body";
        MessageCreateParams params = MessageCreateParams.newBuilder()
            .withBody(expected)
            .build();

        assertEquals(expected, params.body);
    }

    @Test
    void tagsを入力できる() {
        List<String> expected = Arrays.asList("tag");
        MessageCreateParams params = MessageCreateParams.newBuilder()
            .withTags(expected)
            .build();

        assertArrayEquals(expected.toArray(new String[expected.size()]), params.tags.toArray(new String[params.tags.size()]));
    }

    @Test
    void isShortedを入力できる() {
        boolean expected = true;
        MessageCreateParams params = MessageCreateParams.newBuilder()
            .withIsShorten(expected)
            .build();

        assertEquals(expected, params.isShorten);
    }

    @Test
    void scheduledAtを入力できる() {
        OffsetDateTime expected = OffsetDateTime.now();
        MessageCreateParams params = MessageCreateParams.newBuilder()
            .withScheduledAt(expected)
            .build();

        assertEquals(expected, params.scheduledAt);
    }

    @Test
    void limitedAtを入力できる() {
        OffsetDateTime expected = OffsetDateTime.now();
        MessageCreateParams params = MessageCreateParams.newBuilder()
            .withLimitedAt(expected)
            .build();

        assertEquals(expected, params.limitedAt);
    }
}
