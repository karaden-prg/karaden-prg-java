package jp.karaden.param.message.bulk;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.karaden.exception.InvalidParamsException;
import jp.karaden.model.Error;
import jp.karaden.model.KaradenObject;

public class BulkMessageDownloadParams extends BulkMessageParams implements Cloneable {
    public final static Integer DEFAULT_MAX_RETRIES = 2;
    public final static Integer MAX_MAX_RETRIES = 5;
    public final static Integer MIN_MAX_RETRIES = 1;
    public final static Integer DEFAULT_RETRY_INTERVAL = 20;
    public final static Integer MAX_RETRY_INTERVAL = 60;
    public final static Integer MIN_RETRY_INTERVAL = 10;

    public String id;
    public String directoryPath;
    public Integer maxRetries;
    public Integer retryInterval;

    public BulkMessageDownloadParams(String id, String directoryPath, Integer maxRetries, Integer retryInterval) {
        this.id = id;
        this.directoryPath = directoryPath;
        this.maxRetries = Objects.nonNull(maxRetries) ? maxRetries : BulkMessageDownloadParams.DEFAULT_MAX_RETRIES;
        this.retryInterval = Objects.nonNull(retryInterval) ? retryInterval : BulkMessageDownloadParams.DEFAULT_RETRY_INTERVAL;
    }

    protected List<String> validateId() {
        List<String> messages = new ArrayList<>();

        if (this.id == null || this.id.length() == 0) {
            messages.add("idは必須です。");
            messages.add("文字列（UUID）を入力してください。");
        }

        return messages;
    }

    protected List<String> validateDirectoryPath() {
        List<String> messages = new ArrayList<>();

        if (this.directoryPath == null || this.directoryPath.length() == 0) {
            messages.add("directoryPathは必須です。");
            messages.add("文字列を入力してください。");
        } else {
            Path path = Paths.get(this.directoryPath);
            if (Files.notExists(path)) {
                messages.add("指定されたディレクトリパスが存在しません。");
            }

            if (!Files.isDirectory(path)) {
                messages.add("指定されたパスはディレクトリではありません。");
            }

            if (!Files.isReadable(path)) {
                messages.add("指定されたディレクトリには読み取り権限がありません。");
            }

            if (!Files.isWritable(path)) {
                messages.add("指定されたディレクトリには書き込み権限がありません。");
            }
        }

        return messages;
    }

    protected List<String> validateMaxRetries() {
        List<String> messages = new ArrayList<>();

        if (this.maxRetries == null || this.maxRetries < BulkMessageDownloadParams.MIN_MAX_RETRIES) {
            messages.add("maxRetriesには" + BulkMessageDownloadParams.MIN_MAX_RETRIES + "以上の整数を入力してください。");
        }

        if (this.maxRetries == null || this.maxRetries >  BulkMessageDownloadParams.MAX_MAX_RETRIES) {
            messages.add("maxRetriesには" + BulkMessageDownloadParams.MAX_MAX_RETRIES + "以下の整数を入力してください。");
        }

        return messages;
    }

    protected List<String> validateRetryInterval() {
        List<String> messages = new ArrayList<>();

        if (this.retryInterval == null || this.retryInterval <  BulkMessageDownloadParams.MIN_RETRY_INTERVAL) {
            messages.add("retryIntervalには" + BulkMessageDownloadParams.MIN_RETRY_INTERVAL + "以上の整数を入力してください。");
        }

        if (this.retryInterval == null || this.retryInterval >  BulkMessageDownloadParams.MAX_RETRY_INTERVAL) {
            messages.add("retryIntervalには" + BulkMessageDownloadParams.MAX_RETRY_INTERVAL + "以下の整数を入力してください。");
        }

        return messages;
    }

    public BulkMessageDownloadParams validate() throws InvalidParamsException {
        KaradenObject errors = new KaradenObject();
        boolean hasError = false;

        List<String> messages = this.validateId();
        if (! messages.isEmpty()) {
            errors.setProperty("id", messages);
            hasError = true;
        }

        messages = this.validateDirectoryPath();
        if (! messages.isEmpty()) {
            errors.setProperty("directoryPath", messages);
            hasError = true;
        }

        messages = this.validateMaxRetries();
        if (! messages.isEmpty()) {
            errors.setProperty("maxRetries", messages);
            hasError = true;
        }

        messages = this.validateRetryInterval();
        if (! messages.isEmpty()) {
            errors.setProperty("retryInterval", messages);
            hasError = true;
        }

        if (hasError) {
            Error error = new Error();
            error.setProperty("errors", errors);
            throw new InvalidParamsException(error);
        }

        return this;
    }

    @Override
    public BulkMessageDownloadParams clone() {
        BulkMessageDownloadParams params = null;
        try {
            params = (BulkMessageDownloadParams)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
        return params;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        protected BulkMessageDownloadParams params;

        public Builder() {
            this.params = new BulkMessageDownloadParams(null, null, null, null);
        }

        public Builder withId(String id) {
            this.params.id = id;
            return this;
        }

        public Builder withDirectoryPath(String directoryPath) {
            this.params.directoryPath = directoryPath;
            return this;
        }

        public Builder withMaxRetries(Integer maxRetries) {
            this.params.maxRetries = maxRetries;
            return this;
        }

        public Builder withRetryInterval(Integer retryInterval) {
            this.params.retryInterval = retryInterval;
            return this;
        }

        public BulkMessageDownloadParams build() {
            return this.params.clone();
        }
    }

}
