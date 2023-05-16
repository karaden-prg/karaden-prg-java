package jp.karaden;

import java.net.Proxy;

public class Config {
    public final static String VERSION = "1.0.1";
    protected final static String DEFAULT_API_BASE = "https://prg.karaden.jp/api";
    protected final static String DEFALUT_API_VERSION = "2023-01-01";

    public static boolean isLogging = false;

    public static String apiVersion = Config.DEFALUT_API_VERSION;
    public static String apiKey;
    public static String tenantId;
    public static String userAgent;
    public static String apiBase = Config.DEFAULT_API_BASE;
    public static int connectionTimeout = 0;
    public static int readTimeout = 0;
    public static Proxy proxy;

    private Config() {
    }

    public static RequestOptions asRequestOptions() {
        return RequestOptions.newBuilder()
            .withApiVersion(Config.apiVersion)
            .withApiKey(Config.apiKey)
            .withTenantId(Config.tenantId)
            .withUserAgent(Config.userAgent)
            .withApiBase(Config.apiBase)
            .withConnectionTimeout(Config.connectionTimeout)
            .withReadTimeout(Config.readTimeout)
            .withProxy(Config.proxy)
            .build();
    }
}
