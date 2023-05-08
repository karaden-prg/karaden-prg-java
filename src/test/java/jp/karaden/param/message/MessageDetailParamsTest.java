package jp.karaden.param.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import jp.karaden.exception.InvalidParamsException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class MessageDetailParamsTest {
    static List<String> invalidParamsProvider() {
        return Arrays.asList(null, "");
    }

    @Test
    void 正しいパスを生成できる() {
        String id = "id";
        MessageDetailParams params = new MessageDetailParams(id);

        assertEquals(String.format("%s/%s", MessageDetailParams.CONTEXT_PATH, id) , params.toPath());
    }

    @Test
    void idを入力できる() {
        String expected = "id";
        MessageDetailParams params = MessageDetailParams.newBuilder()
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
        MessageDetailParams.Builder builder = MessageDetailParams.newBuilder();
        if (id != null) {
            builder.withId(id);
        }
        assertThrows(InvalidParamsException.class, () -> builder.build().validate());
    }
}
