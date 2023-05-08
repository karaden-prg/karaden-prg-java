package jp.karaden.param.message;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.karaden.exception.InvalidParamsException;
import jp.karaden.model.KaradenObject;
import jp.karaden.model.Error;


public class MessageCreateParams extends MessageParams implements Cloneable {
    public Integer serviceId;
    public String to;
    public String body;
    public List<String> tags;
    public Boolean isShorten;
    public OffsetDateTime scheduledAt;
    public OffsetDateTime limitedAt;

    public MessageCreateParams(Integer serviceId, String to, String body, List<String> tags, Boolean isShorten, OffsetDateTime scheduledAt, OffsetDateTime limitedAt) {
        this.serviceId = serviceId;
        this.to = to;
        this.body = body;
        this.tags = tags;
        this.isShorten = isShorten;
        this.scheduledAt = scheduledAt;
        this.limitedAt = limitedAt;
    }

    public Map<String, ?> toData() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("service_id", this.serviceId);
        payload.put("to", this.to);
        payload.put("body", this.body);
        if (this.tags != null) {
            payload.put("tags", this.tags);
        }
        if (this.isShorten != null) {
            payload.put("is_shorten", this.isShorten);
        }
        if (this.scheduledAt != null) {
            payload.put("scheduled_at", this.scheduledAt.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }
        if (this.limitedAt != null) {
            payload.put("limited_at", this.limitedAt.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }
        return payload;
    }

    public String toPath() {
        return MessageCreateParams.CONTEXT_PATH;
    }

    protected List<String> validateServiceId() {
        List<String> messages = new ArrayList<>();

        if (this.serviceId == null || this.serviceId <= 0) {
            messages.add("serviceIdは必須です。");
            messages.add("数字を入力してください。");
        }

        return messages;
    }

    protected List<String> validateTo() {
        List<String> messages = new ArrayList<>();

        if (this.to == null || this.to.length() == 0) {
            messages.add("toは必須です。");
            messages.add("文字列を入力してください。");
        }

        return messages;
    }

    protected List<String> validateBody() {
        List<String> messages = new ArrayList<>();

        if (this.body == null || this.body.length() == 0) {
            messages.add("bodyは必須です。");
            messages.add("文字列を入力してください。");
        }

        return messages;
    }

    public void validate() throws InvalidParamsException {
        KaradenObject errors = new KaradenObject();
        boolean hasError = false;

        List<String> messages = this.validateServiceId();
        if (! messages.isEmpty()) {
            errors.setProperty("serviceId", messages);
            hasError = true;
        }

        messages = this.validateTo();
        if (! messages.isEmpty()) {
            errors.setProperty("to", messages);
            hasError = true;
        }

        messages = this.validateBody();
        if (! messages.isEmpty()) {
            errors.setProperty("body", messages);
            hasError = true;
        }

        if (hasError) {
            Error error = new Error();
            error.setProperty("errors", errors);
            throw new InvalidParamsException(error);
        }
    }

    @Override
    public MessageCreateParams clone() {
        MessageCreateParams params = null;
        try {
            params = (MessageCreateParams)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
        return params;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        protected MessageCreateParams params;

        public Builder() {
            this.params = new MessageCreateParams(null, null, null, null, null, null, null);
        }

        public Builder withServiceId(Integer serviceId) {
            this.params.serviceId = serviceId;
            return this;
        }

        public Builder withTo(String to) {
            this.params.to = to;
            return this;
        }

        public Builder withBody(String body) {
            this.params.body = body;
            return this;
        }

        public Builder withTags(List<String> tags) {
            this.params.tags = tags;
            return this;
        }

        public Builder withIsShorten(Boolean isShorten) {
            this.params.isShorten = isShorten;
            return this;
        }

        public Builder withScheduledAt(OffsetDateTime scheduledAt) {
            this.params.scheduledAt = scheduledAt;
            return this;
        }

        public Builder withLimitedAt(OffsetDateTime limitedAt) {
            this.params.limitedAt = limitedAt;
            return this;
        }

        public MessageCreateParams build() {
            return this.params.clone();
        }
    }
}
