package jp.karaden.model;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

import jp.karaden.RequestOptions;
import jp.karaden.exception.KaradenException;
import jp.karaden.param.message.MessageCancelParams;
import jp.karaden.param.message.MessageCreateParams;
import jp.karaden.param.message.MessageDetailParams;
import jp.karaden.param.message.MessageListParams;


public class Message extends Requestable {
    public final static String OBJECT_NAME = "message";

    public Message() {
        super();
    }

    public Message(Object id, RequestOptions requestOptions) {
        super(id, requestOptions);
    }

    public Integer getServiceId() {
        return (Integer)this.getProperty("service_id");
    }

    public Integer getBillingAddressId() {
        return (Integer)this.getProperty("billing_address_id");
    }

    public String getTo() {
        return (String)this.getProperty("to");
    }

    public String getBody() {
        return (String)this.getProperty("body");
    }

    public List<String> getTags() {
        return (List<String>)this.getProperty("tags");
    }

    public boolean isShorten() {
        return (boolean)this.getProperty("is_shorten");
    }

    public Boolean isShortenClicked() {
        return (Boolean)this.getProperty("is_shorten_clicked");
    }

    public String getResult() {
        return (String)this.getProperty("result");
    }

    public String getStatus() {
        return (String)this.getProperty("status");
    }

    public String getSentResult() {
        return (String)this.getProperty("sent_result");
    }

    public String getCarrier() {
        return (String)this.getProperty("carrier");
    }

    public int getChargedCountPerSent() {
        return (int)this.getProperty("charged_count_per_sent");
    }

    public OffsetDateTime getScheduledAt() {
        String scheduledAt = (String)this.getProperty("scheduled_at");
        return OffsetDateTime.parse(scheduledAt);
    }

    public OffsetDateTime getLimitedAt() {
        String limitedAt = (String)this.getProperty("limited_at");
        return OffsetDateTime.parse(limitedAt);
    }

    public OffsetDateTime getSentAt() {
        String sentAt = (String)this.getProperty("sent_at");
        return OffsetDateTime.parse(sentAt);
    }

    public OffsetDateTime getReceivedAt() {
        String receivedAt = (String)this.getProperty("received_at");
        return OffsetDateTime.parse(receivedAt);
    }

    public OffsetDateTime getChargedAt() {
        String chargedAt = (String)this.getProperty("charged_at");
        return OffsetDateTime.parse(chargedAt);
    }

    public OffsetDateTime getCreatedAt() {
        String createdAt = (String)this.getProperty("created_at");
        return OffsetDateTime.parse(createdAt);
    }

    public OffsetDateTime getUpdatedAt() {
        String updatedAt = (String)this.getProperty("updated_at");
        return OffsetDateTime.parse(updatedAt);
    }

    public final static Message create(MessageCreateParams params) throws KaradenException {
        params.validate();
        return (Message)Message
            .request(
                "POST",
                params.toPath(), 
                null,
                params.toData(),
                new RequestOptions()
            );
    }

    public final static Message create(MessageCreateParams params, RequestOptions requestOptions) throws KaradenException {
        params.validate();
        return (Message)Message
            .request(
                "POST",
                params.toPath(), 
                null,
                params.toData(),
                requestOptions
            );
    }

    public final static Message detail(MessageDetailParams params) throws KaradenException {
        params.validate();
        return (Message)Message
            .request(
                "GET",
                params.toPath(), 
                null,
                null,
                new RequestOptions()
            );
    }

    public final static Message detail(MessageDetailParams params, RequestOptions requestOptions) throws KaradenException {
        params.validate();
        return (Message)Message
            .request(
                "GET",
                params.toPath(), 
                null,
                null,
                requestOptions
            );
    }

    public final static Collection list(MessageListParams params) throws KaradenException {
        params.validate();
        return (Collection)Message
            .request(
                "GET",
                params.toPath(), 
                params.toParams(),
                null,
                new RequestOptions()
            );
    }

    public final static Collection list(MessageListParams params, RequestOptions requestOptions) throws KaradenException {
        params.validate();
        return (Collection)Message
            .request(
                "GET",
                params.toPath(), 
                params.toParams(),
                null,
                requestOptions
            );
    }

    public final static Message cancel(MessageCancelParams params) throws KaradenException {
        params.validate();
        return (Message)Message
            .request(
                "POST",
                params.toPath(), 
                null,
                new HashMap<>(),
                new RequestOptions()
            );
    }

    public final static Message cancel(MessageCancelParams params, RequestOptions requestOptions) throws KaradenException {
        params.validate();
        return (Message)Message
            .request(
                "POST",
                params.toPath(), 
                null,
                new HashMap<>(),
                requestOptions
            );
    }
}
