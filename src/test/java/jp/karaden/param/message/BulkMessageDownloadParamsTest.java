package jp.karaden.param.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import jp.karaden.exception.InvalidParamsException;
import jp.karaden.param.message.bulk.BulkMessageDownloadParams;

public class BulkMessageDownloadParamsTest {
    @Test
    void idを入力できる() {
        String expected = "82bdf9de-a532-4bf5-86bc-c9a1366e5f4a";
        BulkMessageDownloadParams params = BulkMessageDownloadParams.newBuilder()
            .withId(expected)
            .build();

        assertEquals(expected, params.id);
    }

    @Test
    void directoryPathを入力できる() {
        String expected = "path";
        BulkMessageDownloadParams params = BulkMessageDownloadParams.newBuilder()
            .withDirectoryPath(expected)
            .build();

        assertEquals(expected, params.directoryPath);
    }

    @Test
    void maxRetriesを入力できる() {
        Integer expected = 1;
        BulkMessageDownloadParams params = BulkMessageDownloadParams.newBuilder()
            .withMaxRetries(expected)
            .build();

        assertEquals(expected, params.maxRetries);
    }

    @Test
    void retryIntervalを入力できる() {
        Integer expected = 10;
        BulkMessageDownloadParams params = BulkMessageDownloadParams.newBuilder()
            .withRetryInterval(expected)
            .build();

        assertEquals(expected, params.retryInterval);
    }

    static List<String> invalidIdProvider() {
        return Arrays.asList(null, "");
    }

    @ParameterizedTest
    @MethodSource("invalidIdProvider")
    void idが空文字や未指定はエラー(String id) {
        BulkMessageDownloadParams.Builder builder = BulkMessageDownloadParams.newBuilder();
        if (id != null) {
            builder.withId(id);
        }
        InvalidParamsException e = assertThrows(InvalidParamsException.class, () -> builder.build().validate());

        assertInstanceOf(List.class, e.error.getErrors().getProperty("id"));
    }

    @Test
    void directoryPathが存在しない値の場合はエラー() {
        String expected = "path";
        BulkMessageDownloadParams.Builder builder = BulkMessageDownloadParams.newBuilder()
            .withDirectoryPath(expected);

        InvalidParamsException e = assertThrows(InvalidParamsException.class, () -> builder.build().validate());

        assertInstanceOf(List.class, e.error.getErrors().getProperty("directoryPath"));
    }

    @Test
    void directoryPathがファイルを指定している場合はエラー() throws IOException {
        Path tmpDir = Files.createTempDirectory("tmpDir_");
        Path tmpFile = Files.createTempFile(tmpDir, "tmpFile_", ".txt");
        try {
            BulkMessageDownloadParams.Builder builder = BulkMessageDownloadParams.newBuilder()
                .withDirectoryPath(tmpFile.toString());

            InvalidParamsException e = assertThrows(InvalidParamsException.class, () -> builder.build().validate());

            assertInstanceOf(List.class, e.error.getErrors().getProperty("directoryPath"));
        } finally {
            Files.delete(tmpFile);
            Files.delete(tmpDir);
        }
    }

    @Test
    void 指定されたdirectoryPathに読み取り権限がない場合はエラー() throws IOException {
        Path tmpDir = Files.createTempDirectory("tempDir_");
        try {
            Files.setPosixFilePermissions(tmpDir, PosixFilePermissions.fromString("-wxrwxrwx"));
            BulkMessageDownloadParams.Builder builder = BulkMessageDownloadParams.newBuilder()
                .withDirectoryPath(tmpDir.toString());

            InvalidParamsException e = assertThrows(InvalidParamsException.class, () -> builder.build().validate());

            assertInstanceOf(List.class, e.error.getErrors().getProperty("directoryPath"));
        } finally {
            Files.setPosixFilePermissions(tmpDir, PosixFilePermissions.fromString("rwxrwxrwx"));
            Files.delete(tmpDir);
        }
    }

    @Test
    void 指定されたdirectoryPathに書き込み権限がない場合はエラー() throws IOException {
        Path tmpDir = Files.createTempDirectory("tempDir_");
        try {
            Files.setPosixFilePermissions(tmpDir, PosixFilePermissions.fromString("r-xrwxrwx"));
            BulkMessageDownloadParams.Builder builder = BulkMessageDownloadParams.newBuilder()
                .withDirectoryPath(tmpDir.toString());

            InvalidParamsException e = assertThrows(InvalidParamsException.class, () -> builder.build().validate());

            assertInstanceOf(List.class, e.error.getErrors().getProperty("directoryPath"));
        } finally {
            Files.setPosixFilePermissions(tmpDir, PosixFilePermissions.fromString("rwxrwxrwx"));
            Files.delete(tmpDir);
        }
    }

    static List<Integer> invalidMaxRetriesProvider() {
        return Arrays.asList(0, 6, -1);
    }

    @ParameterizedTest
    @MethodSource("invalidMaxRetriesProvider")
    void maxRetriesが0以下または6以上はエラー(Integer maxRetries) {
        BulkMessageDownloadParams.Builder builder = BulkMessageDownloadParams.newBuilder();
        if (maxRetries != null) {
            builder.withMaxRetries(maxRetries);
        }
        InvalidParamsException e = assertThrows(InvalidParamsException.class, () -> builder.build().validate());

        assertInstanceOf(List.class, e.error.getErrors().getProperty("maxRetries"));
    }

    static List<Integer> invalidRetryInterval() {
        return Arrays.asList( 9, 61, -1);
    }

    @ParameterizedTest
    @MethodSource("invalidRetryInterval")
    void retryIntervalが9以下または61以上はエラー(Integer retryInterval) {
        BulkMessageDownloadParams.Builder builder = BulkMessageDownloadParams.newBuilder();
        if (retryInterval != null) {
            builder.withRetryInterval(retryInterval);
        }
        InvalidParamsException e = assertThrows(InvalidParamsException.class, () -> builder.build().validate());

        assertInstanceOf(List.class, e.error.getErrors().getProperty("retryInterval"));
    }
}
