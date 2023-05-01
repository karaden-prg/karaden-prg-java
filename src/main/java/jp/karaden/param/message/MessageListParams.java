package jp.karaden.param.message;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;


public class MessageListParams extends MessageParams implements Cloneable {
    public Integer serviceId;
    public String to;
    public String status;
    public String result;
    public String sentResult;
    public String tag;
    public OffsetDateTime startAt;
    public OffsetDateTime endAt;
    public Integer page;
    public Integer perPage;

    public MessageListParams(Integer serviceId, String to, String status, String result, String sentResult, String tag, OffsetDateTime startAt, OffsetDateTime endAt, Integer page, Integer perPage) {
        this.serviceId = serviceId;
        this.to = to;
        this.status = status;
        this.result = result;
        this.sentResult = sentResult;
        this.tag = tag;
        this.startAt = startAt;
        this.endAt = endAt;
        this.page = page;
        this.perPage = perPage;
    }

    public Map<String, ?> toParams() {
        Map<String, Object> payload = new HashMap<>();
        if (this.serviceId != null) {
            payload.put("service_id", this.serviceId);
        }
        if (this.to != null) {
            payload.put("to", this.to);
        }
        if (this.status != null) {
            payload.put("status", this.status);
        }
        if (this.result != null) {
            payload.put("result", this.result);
        }
        if (this.sentResult != null) {
            payload.put("sent_result", this.sentResult);
        }
        if (this.tag != null) {
            payload.put("tag", this.tag);
        }
        if (this.startAt != null) {
            payload.put("start_at", this.startAt.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }
        if (this.endAt != null) {
            payload.put("end_at", this.endAt.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }
        if (this.page != null) {
            payload.put("page", this.page);
        }
        if (this.perPage != null) {
            payload.put("per_page", this.perPage);
        }
        return payload;
    }

    public String toPath() {
        return MessageListParams.CONTEXT_PATH;
    }

    @Override
    public MessageListParams clone() {
        MessageListParams params = null;
        try {
            params = (MessageListParams)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
        return params;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        protected MessageListParams params;

        public Builder() {
            this.params = new MessageListParams(null, null, null, null, null, null, null, null, null, null);
        }

        public Builder withServiceId(int serviceId) {
            this.params.serviceId = serviceId;
            return this;
        }

        public Builder withTo(String to) {
            this.params.to = to;
            return this;
        }

        public Builder withStatus(String status) {
            this.params.status = status;
            return this;
        }

        public Builder withResult(String result) {
            this.params.result = result;
            return this;
        }

        public Builder withSentResult(String sentResult) {
            this.params.sentResult = sentResult;
            return this;
        }

        public Builder withTag(String tag) {
            this.params.tag = tag;
            return this;
        }

        public Builder withStartAt(OffsetDateTime startAt) {
            this.params.startAt = startAt;
            return this;
        }

        public Builder withEndAt(OffsetDateTime endAt) {
            this.params.endAt = endAt;
            return this;
        }

        public Builder withPage(Integer page) {
            this.params.page = page;
            return this;
        }

        public Builder withPerPage(Integer perPage) {
            this.params.perPage = perPage;
            return this;
        }

        public MessageListParams build() {
            return this.params.clone();
        }
    }
}
