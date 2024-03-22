package jp.karaden.model;

import java.time.OffsetDateTime;

import jp.karaden.RequestOptions;
import jp.karaden.exception.KaradenException;
import jp.karaden.param.message.bulk.BulkMessageCreateParams;
import jp.karaden.param.message.bulk.BulkMessageShowParams;
import jp.karaden.param.message.bulk.BulkMessageListMessageParams;
import jp.karaden.net.ResponseInterface;

public class BulkMessage extends Requestable {
    public final static String OBJECT_NAME = "bulk_message";
    public final static String STATUS_DONE = "done";
    public final static String STATUS_WAITING = "waiting";
    public final static String STATUS_PROCESSING = "processing";
    public final static String STATUS_ERROR = "error";

    public BulkMessage() {
        super();
    }

    public BulkMessage(Object id, RequestOptions requestOptions) {
        super(id, requestOptions);
    }

    public String getStatus() {
        return (String)this.getProperty("status");
    }

    public Error getError() {
        return (Error)this.getProperty("error");
    }

    public OffsetDateTime getCreatedAt() {
        String createdAt = (String)this.getProperty("created_at");
        return OffsetDateTime.parse(createdAt);
    }

    public OffsetDateTime getUpdatedAt() {
        String updatedAt = (String)this.getProperty("updated_at");
        return OffsetDateTime.parse(updatedAt);
    }

    public final static BulkMessage create(BulkMessageCreateParams params) throws KaradenException {
        params.validate();
        return (BulkMessage)BulkMessage
            .request(
                "POST",
                params.toPath(), 
                null,
                params.toData(),
                new RequestOptions()
            );
    }

    public final static BulkMessage create(BulkMessageCreateParams params, RequestOptions requestOptions) throws KaradenException {
        params.validate();
        return (BulkMessage)BulkMessage
            .request(
                "POST",
                params.toPath(), 
                null,
                params.toData(),
                requestOptions
            );
    }

    public final static BulkMessage show(BulkMessageShowParams params) throws KaradenException {
        params.validate();
        return (BulkMessage)BulkMessage
            .request(
                "GET",
                params.toPath(),
                null,
                null,
                new RequestOptions()
            );
    }

    public final static BulkMessage show(BulkMessageShowParams params, RequestOptions requestOptions) throws KaradenException {
        params.validate();
        return (BulkMessage)BulkMessage
            .request(
                "GET",
                params.toPath(),
                null,
                null,
                requestOptions
            );
    }

    public final static String listMessage(BulkMessageListMessageParams params) throws KaradenException {
        params.validate();
        ResponseInterface response = BulkMessage
            .requestAndReturnResponseInterface(
                "GET",
                params.toPath(),
                null,
                null,
                new RequestOptions()
            );
        return response.getStatusCode() == 302 ? response.getHeaders().get("Location") : null;
    }

    public final static String listMessage(BulkMessageListMessageParams params, RequestOptions requestOptions) throws KaradenException {
        params.validate();
        ResponseInterface response = BulkMessage
            .requestAndReturnResponseInterface(
                "GET",
                params.toPath(),
                null,
                null,
                requestOptions
            );
        return response.getStatusCode() == 302 ? response.getHeaders().get("Location") : null;
    }
}
