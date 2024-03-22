package jp.karaden.param.message.bulk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.karaden.exception.InvalidParamsException;
import jp.karaden.model.Error;
import jp.karaden.model.KaradenObject;

public class BulkMessageCreateParams extends BulkMessageParams implements Cloneable {
    public String bulkFileId;

    public BulkMessageCreateParams(String bulkFileId) {
        this.bulkFileId = bulkFileId;
    }

    public Map<String, ?> toData() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("bulk_file_id", this.bulkFileId);
        return payload;
    }

    public String toPath() {
        return BulkMessageParams.CONTEXT_PATH;
    }

    protected List<String> validateFile() {
        List<String> messages = new ArrayList<>();

        if (this.bulkFileId == null || this.bulkFileId.length() == 0) {
            messages.add("bulkFileIdは必須です。");
            messages.add("文字列（UUID）を入力してください。");
        }

        return messages;
    }

    public BulkMessageCreateParams validate() throws InvalidParamsException {
        KaradenObject errors = new KaradenObject();
        boolean hasError = false;

        List<String> messages = this.validateFile();
        if (! messages.isEmpty()) {
            errors.setProperty("bulkFileId", messages);
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
    public BulkMessageCreateParams clone() {
        BulkMessageCreateParams params = null;
        try {
            params = (BulkMessageCreateParams)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
        return params;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        protected BulkMessageCreateParams params;

        public Builder() {
            this.params = new BulkMessageCreateParams(null);
        }

        public Builder withBulkFileId(String bulkFileId) {
            this.params.bulkFileId = bulkFileId;
            return this;
        }

        public BulkMessageCreateParams build() {
            return this.params.clone();
        }
    }
}
