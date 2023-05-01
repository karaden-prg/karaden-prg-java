package jp.karaden.param.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

class MessageListParamsTest {
    @Test
    void 正しいパスを生成できる() {
        MessageListParams params = new MessageListParams(null, null, null, null, null, null, null, null, null, null);

        assertEquals(MessageListParams.CONTEXT_PATH , params.toPath());
    }

    @Test
    void serviceIdをクエリにできる() {
        Integer expected = 1;
        MessageListParams params = new MessageListParams(expected, null, null, null, null, null, null, null, null, null);

        Map<String, ?> actual = params.toParams();
        assertEquals(expected, actual.get("service_id"));
    }

    @Test
    void toをクエリにできる() {
        String expected = "to";
        MessageListParams params = new MessageListParams(null, expected, null, null, null, null, null, null, null, null);

        Map<String, ?> actual = params.toParams();
        assertEquals(expected, actual.get("to"));
    }

    @Test
    void statusをクエリにできる() {
        String expected = "status";
        MessageListParams params = new MessageListParams(null, null, expected, null, null, null, null, null, null, null);

        Map<String, ?> actual = params.toParams();
        assertEquals(expected, actual.get("status"));
    }

    @Test
    void resultをクエリにできる() {
        String expected = "result";
        MessageListParams params = new MessageListParams(null, null, null, expected, null, null, null, null, null, null);

        Map<String, ?> actual = params.toParams();
        assertEquals(expected, actual.get("result"));
    }

    @Test
    void sentResultをクエリにできる() {
        String expected = "sentResult";
        MessageListParams params = new MessageListParams(null, null, null, null, expected, null, null, null, null, null);

        Map<String, ?> actual = params.toParams();
        assertEquals(expected, actual.get("sent_result"));
    }

    @Test
    void tagをクエリにできる() {
        String expected = "tag";
        MessageListParams params = new MessageListParams(null, null, null, null, null, expected, null, null, null, null);

        Map<String, ?> actual = params.toParams();
        assertEquals(expected, actual.get("tag"));
    }

    @Test
    void startAtをクエリにできる() {
        OffsetDateTime expected = OffsetDateTime.of(2022, 12, 12, 0, 0, 0, 0, ZoneOffset.ofHours(9));
        MessageListParams params = new MessageListParams(null, null, null, null, null, null, expected, null, null, null);

        Map<String, ?> actual = params.toParams();
        assertEquals(expected.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), actual.get("start_at"));
    }

    @Test
    void startAtをクエリにするとナノ秒は落ちる() {
        OffsetDateTime expected = OffsetDateTime.of(2022, 12, 12, 0, 0, 0, 30, ZoneOffset.ofHours(9));
        MessageListParams params = new MessageListParams(null, null, null, null, null, null, expected, null, null, null);

        Map<String, ?> actual = params.toParams();
        assertEquals(expected.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), actual.get("start_at"));
    }

    @Test
    void endAtをクエリにできる() {
        OffsetDateTime expected = OffsetDateTime.of(2022, 12, 12, 0, 0, 0, 0, ZoneOffset.ofHours(9));
        MessageListParams params = new MessageListParams(null, null, null, null, null, null, null, expected, null, null);

        Map<String, ?> actual = params.toParams();
        assertEquals(expected.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), actual.get("end_at"));
    }
    
    @Test
    void endAtをクエリにするとナノ秒は落ちる() {
        OffsetDateTime expected = OffsetDateTime.of(2022, 12, 12, 0, 0, 0, 30, ZoneOffset.ofHours(9));
        MessageListParams params = new MessageListParams(null, null, null, null, null, null, null, expected, null, null);

        Map<String, ?> actual = params.toParams();
        assertEquals(expected.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), actual.get("end_at"));
    }

    @Test
    void pageをクエリにできる() {
        Integer expected = 1;
        MessageListParams params = new MessageListParams(null, null, null, null, null, null, null, null, expected, null);

        Map<String, ?> actual = params.toParams();
        assertEquals(expected, actual.get("page"));
    }

    @Test
    void perPageをクエリにできる() {
        Integer expected = 1;
        MessageListParams params = new MessageListParams(null, null, null, null, null, null, null, null, null, expected);

        Map<String, ?> actual = params.toParams();
        assertEquals(expected, actual.get("per_page"));
    }
}
