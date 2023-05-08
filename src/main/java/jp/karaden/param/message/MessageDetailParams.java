package jp.karaden.param.message;

import java.util.ArrayList;
import java.util.List;

import jp.karaden.exception.InvalidParamsException;
import jp.karaden.model.Error;
import jp.karaden.model.KaradenObject;

public class MessageDetailParams extends MessageParams implements Cloneable {
    public String id;

    public MessageDetailParams(String id) {
        this.id = id;
    }

    public String toPath() {
        return String.format("%s/%s", MessageDetailParams.CONTEXT_PATH, this.id);
    }

    protected List<String> validateId() {
        List<String> errors = new ArrayList<>();

        if (this.id == null || this.id == "") {
            errors.add("idは必須です。");
            errors.add("文字列（UUID）を入力してください。");
        }

        return errors;
    }

    public MessageParams validate() throws InvalidParamsException {
        KaradenObject errors = new KaradenObject();
        boolean hasError = false;

        List<String> id = validateId();
        if (! id.isEmpty()) {
            errors.setProperty("id", id);
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
    public MessageDetailParams clone() {
        MessageDetailParams params = null;
        try {
            params = (MessageDetailParams)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
        return params;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        protected MessageDetailParams params;

        public Builder() {
            this.params = new MessageDetailParams(null);
        }

        public Builder withId(String id) {
            this.params.id = id;
            return this;
        }

        public MessageDetailParams build() {
            return this.params.clone();
        }
    }
}
