package jp.karaden.model;

import java.time.OffsetDateTime;
import java.util.HashMap;

import jp.karaden.RequestOptions;
import jp.karaden.exception.KaradenException;
import jp.karaden.param.message.bulk.BulkMessageParams;

public class BulkFile extends Requestable {
    public final static String OBJECT_NAME = "bulk_file";

    public BulkFile() {
        super();
    }

    public BulkFile(Object id, RequestOptions requestOptions) {
        super(id, requestOptions);
    }

    public String getUrl() {
        return (String)this.getProperty("url");
    }

    public OffsetDateTime getCreatedAt() {
        String createdAt = (String)this.getProperty("created_at");
        return OffsetDateTime.parse(createdAt);
    }

    public OffsetDateTime getExpiresAt() {
        String expiresAt = (String)this.getProperty("expires_at");
        return OffsetDateTime.parse(expiresAt);
    }

    public final static BulkFile create() throws KaradenException {
        String path = String.format("%s/files", BulkMessageParams.CONTEXT_PATH);
        return (BulkFile)BulkFile
            .request(
                "POST",
                path, 
                null,
                new HashMap<>(),
                new RequestOptions()
            );
    }

    public final static BulkFile create(RequestOptions requestOptions) throws KaradenException {
        String path = String.format("%s/files", BulkMessageParams.CONTEXT_PATH);
        return (BulkFile)BulkFile
            .request(
                "POST",
                path, 
                null,
                new HashMap<>(),
                requestOptions
            );
    }
}
