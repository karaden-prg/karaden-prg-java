package jp.karaden;


import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import jp.karaden.exception.InvalidRequestOptionsException;
import jp.karaden.model.Error;
import jp.karaden.model.KaradenObject;
import okhttp3.HttpUrl;

public class RequestOptions implements Cloneable {
    public String apiVersion;
    public String apiKey;
    public String tenantId;
    public String userAgent;
    public String apiBase;
    public Integer connectionTimeout;
    public Integer readTimeout;
    public Proxy proxy;

    public RequestOptions() {
    }

    public RequestOptions merge(RequestOptions source) {
        RequestOptions destination = this.clone();
        if (source != null) {
            if (source.apiVersion != null) {
                destination.apiVersion = source.apiVersion;
            }
            if (source.apiKey != null) {
                destination.apiKey = source.apiKey;
            }
            if (source.tenantId != null) {
                destination.tenantId = source.tenantId;
            }
            if (source.userAgent != null) {
                destination.userAgent = source.userAgent;
            }
            if (source.apiBase != null) {
                destination.apiBase = source.apiBase;
            }
            if (source.connectionTimeout != null) {
                destination.connectionTimeout = source.connectionTimeout;
            }
            if (source.readTimeout != null) {
                destination.readTimeout = source.readTimeout;
            }
            if (source.proxy != null) {
                destination.proxy = source.proxy;
            }
        }
        
        return destination;
    }

    public String getBaseUri() {
        return HttpUrl.parse(this.apiBase).newBuilder().addPathSegment(this.tenantId).build().toString();
    }

    protected List<String> validateApiVersion() {
        List<String> messages = new ArrayList<>();

        if (this.apiVersion == null || this.apiVersion.length() == 0) {
            messages.add("apiVersionは必須です。");
            messages.add("文字列を入力してください。");
        }

        return messages;
    }

    protected List<String> validateApiKey() {
        List<String> messages = new ArrayList<>();

        if (this.apiKey == null || this.apiKey.length() == 0) {
            messages.add("apiKeyは必須です。");
            messages.add("文字列を入力してください。");
        }

        return messages;
    }

    protected List<String> validateTenantId() {
        List<String> messages = new ArrayList<>();

        if (this.tenantId == null || this.tenantId.length() == 0) {
            messages.add("tenantIdは必須です。");
            messages.add("文字列を入力してください。");
        }

        return messages;
    }

    protected List<String> validateApiBase() {
        List<String> messages = new ArrayList<>();

        if (this.apiBase == null || this.apiBase.length() == 0) {
            messages.add("apiBaseは必須です。");
            messages.add("文字列を入力してください。");
        }

        return messages;
    }

    public RequestOptions validate() throws InvalidRequestOptionsException {
        KaradenObject errors = new KaradenObject();
        boolean hasError = false;

        List<String> messages = this.validateApiVersion();
        if (! messages.isEmpty()) {
            errors.setProperty("apiVersion", messages);
            hasError = true;
        }

        messages = this.validateApiKey();
        if (! messages.isEmpty()) {
            errors.setProperty("apiKey", messages);
            hasError = true;
        }

        messages = this.validateTenantId();
        if (! messages.isEmpty()) {
            errors.setProperty("tenantId", messages);
            hasError = true;
        }

        messages = this.validateApiBase();
        if (! messages.isEmpty()) {
            errors.setProperty("apiBase", messages);
            hasError = true;
        }

        if (hasError) {
            Error error = new Error();
            error.setProperty("errors", errors);
            throw new InvalidRequestOptionsException(error);
        }

        return this;
    }

    @Override
    public RequestOptions clone() {
        RequestOptions requestOptions = null;
        try {
            requestOptions = (RequestOptions)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
        return requestOptions;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        protected RequestOptions requestOptions;

        public Builder() {
            this.requestOptions = new RequestOptions();
        }

        public Builder withApiVersion(String apiVersion) {
            this.requestOptions.apiVersion = apiVersion;
            return this;
        }

        public Builder withApiKey(String apiKey) {
            this.requestOptions.apiKey = apiKey;
            return this;
        }

        public Builder withTenantId(String tenantId) {
            this.requestOptions.tenantId = tenantId;
            return this;
        }

        public Builder withUserAgent(String userAgent) {
            this.requestOptions.userAgent = userAgent;
            return this;
        }

        public Builder withApiBase(String apiBase) {
            this.requestOptions.apiBase = apiBase;
            return this;
        }

        public Builder withConnectionTimeout(int connectionTimeout) {
            this.requestOptions.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder withReadTimeout(int readTimeout) {
            this.requestOptions.readTimeout = readTimeout;
            return this;
        }

        public Builder withProxy(Proxy proxy) {
            this.requestOptions.proxy = proxy;
            return this;
        }

        public RequestOptions build() {
            return this.requestOptions.clone();
        }
    }
}
