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
import jp.karaden.param.message.bulk.BulkMessageCreateParams;

public class BulkMessageCreateParamsTest {
    @Test
    void 正しいパスを生成できる() {
        String bulkFileId = "c439f89c-1ea3-7073-7021-1f127a850437";
        BulkMessageCreateParams params = new BulkMessageCreateParams(bulkFileId);

        assertEquals(BulkMessageCreateParams.CONTEXT_PATH , params.toPath());
    }

    @Test
    void bulkFileIdを入力できる() {
        String expected = "c439f89c-1ea3-7073-7021-1f127a850437";
        BulkMessageCreateParams params = BulkMessageCreateParams.newBuilder()
            .withBulkFileId(expected)
            .build();

        assertEquals(expected, params.bulkFileId);
    }

    static List<String> invalidIdProvider() {
        return Arrays.asList(null, "");
    }

    @ParameterizedTest
    @MethodSource("invalidIdProvider")
    void bulkFileIdが空文字や未指定はエラー(String bulkFileId) {
        BulkMessageCreateParams.Builder builder = BulkMessageCreateParams.newBuilder();
        if (bulkFileId != null) {
            builder.withBulkFileId(bulkFileId);
        }
        InvalidParamsException e = assertThrows(InvalidParamsException.class, () -> builder.build().validate());

        assertInstanceOf(List.class, e.error.getErrors().getProperty("bulkFileId"));
    }
}
