package jp.karaden.param.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import jp.karaden.exception.InvalidParamsException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class MessageCancelParamsTest {
    static List<String> invalidParamsProvider() {
        return Arrays.asList(null, "");
    }

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

    @ParameterizedTest
    @MethodSource("invalidParamsProvider")
    void idがnullや空文字はエラー(String expected) {
        MessageCancelParams params = MessageCancelParams.newBuilder()
            .withId(expected)
            .build();
        assertThrows(InvalidParamsException.class, () -> params.validate());
    }
}
