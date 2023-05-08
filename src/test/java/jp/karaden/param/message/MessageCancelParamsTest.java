package jp.karaden.param.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import jp.karaden.exception.InvalidParamsException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class MessageCancelParamsTest {
    @Test
    void 正しいパスを生成できる() {
        String id = "id";
        MessageCancelParams params = new MessageCancelParams(id);

        assertEquals(String.format("%s/%s/cancel", MessageCancelParams.CONTEXT_PATH, id) , params.toPath());
    }

    @Test
    void idを入力できる() {
        String expected = "id";
        MessageCancelParams params = MessageCancelParams.newBuilder()
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
        MessageCancelParams.Builder builder = MessageCancelParams.newBuilder();
        if (id != null) {
            builder.withId(id);
        }
        assertThrows(InvalidParamsException.class, () -> builder.build().validate());
    }
}
