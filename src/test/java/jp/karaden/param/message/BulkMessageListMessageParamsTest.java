package jp.karaden.param.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import jp.karaden.exception.InvalidParamsException;
import jp.karaden.param.message.bulk.BulkMessageListMessageParams;

public class BulkMessageListMessageParamsTest {
    @Test
    void 正しいパスを生成できる() {
        String id = "82bdf9de-a532-4bf5-86bc-c9a1366e5f4a";
        BulkMessageListMessageParams params = new BulkMessageListMessageParams(id);

        assertEquals(String.format("%s/%s/messages", BulkMessageListMessageParams.CONTEXT_PATH, id) , params.toPath());
    }

    @Test
    void idを入力できる() {
        String expected = "82bdf9de-a532-4bf5-86bc-c9a1366e5f4a";
        BulkMessageListMessageParams params = BulkMessageListMessageParams.newBuilder()
            .withId(expected)
            .build();

        assertEquals(expected, params.id);
    }

    static List<String> invalidIdProvider() {
        return Arrays.asList(null, "");
    }

    @ParameterizedTest
    @MethodSource("invalidIdProvider")
    void idが空文字や未指定はエラー(String id) {
        BulkMessageListMessageParams.Builder builder = BulkMessageListMessageParams.newBuilder();
        if (id != null) {
            builder.withId(id);
        }
        InvalidParamsException e = assertThrows(InvalidParamsException.class, () -> builder.build().validate());

        assertInstanceOf(List.class, e.error.getErrors().getProperty("id"));
    }
}
