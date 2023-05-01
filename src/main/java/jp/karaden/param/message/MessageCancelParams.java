package jp.karaden.param.message;

import java.util.ArrayList;
import java.util.List;

import jp.karaden.exception.InvalidParamsException;
import jp.karaden.model.KaradenObject;
import jp.karaden.model.Error;

public class MessageCancelParams extends MessageParams implements Cloneable {
    public String id;

    public MessageCancelParams(String id) {
        this.id = id;
    }

    public String toPath() {
        return String.format("%s/%s/cancel", MessageCancelParams.CONTEXT_PATH, this.id);
    }

    protected List<String> validateId() {
        List<String> messages = new ArrayList<>();

        if (this.id == null || this.id == "") {
            messages.add("idは必須です。");
            messages.add("文字列（UUID）を入力してください。");
        }

        return messages;
    }

    public void validate() throws InvalidParamsException {
        KaradenObject errors = new KaradenObject();
        boolean hasError = false;

        List<String> messages = validateId();
        if (! messages.isEmpty()) {
            errors.setProperty("id", messages);
            hasError = true;
        }

        if (hasError) {
            Error error = new Error();
            error.setProperty("errors", errors);
            throw new InvalidParamsException(error);
        }
    }

    @Override
    public MessageCancelParams clone() {
        MessageCancelParams params = null;
        try {
            params = (MessageCancelParams)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
        return params;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        protected MessageCancelParams params;

        public Builder() {
            this.params = new MessageCancelParams(null);
        }

        public Builder withId(String id) {
            this.params.id = id;
            return this;
        }

        public MessageCancelParams build() {
            return this.params.clone();
        }
    }
}
