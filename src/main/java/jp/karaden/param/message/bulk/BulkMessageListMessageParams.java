package jp.karaden.param.message.bulk;

import java.util.ArrayList;
import java.util.List;

import jp.karaden.exception.InvalidParamsException;
import jp.karaden.model.Error;
import jp.karaden.model.KaradenObject;

public class BulkMessageListMessageParams extends BulkMessageParams implements Cloneable {
    public String id;

    public BulkMessageListMessageParams(String id) {
        this.id = id;
    }

    public String toPath() {
        return String.format("%s/%s/messages", BulkMessageParams.CONTEXT_PATH, this.id);
    }

    protected List<String> validateId() {
        List<String> messages = new ArrayList<>();

        if (this.id == null || this.id.length() == 0) {
            messages.add("idは必須です。");
            messages.add("文字列（UUID）を入力してください。");
        }

        return messages;
    }

    public BulkMessageListMessageParams validate() throws InvalidParamsException {
        KaradenObject errors = new KaradenObject();
        boolean hasError = false;

        List<String> messages = this.validateId();
        if (! messages.isEmpty()) {
            errors.setProperty("id", messages);
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
    public BulkMessageListMessageParams clone() {
        BulkMessageListMessageParams params = null;
        try {
            params = (BulkMessageListMessageParams)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
        return params;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        protected BulkMessageListMessageParams params;

        public Builder() {
            this.params = new BulkMessageListMessageParams(null);
        }

        public Builder withId(String id) {
            this.params.id = id;
            return this;
        }

        public BulkMessageListMessageParams build() {
            return this.params.clone();
        }
    }
}
