package jp.karaden;

public class TestHelper {
    public static final String API_BASE = "http://localhost:4010";
    public static final String API_KEY = "123";
    public static final String TENANT_ID = "159bfd33-b9b7-f424-4755-c119b324591d";

    public static RequestOptions.Builder getDefaultRequestOptionsBuilder() {
        return RequestOptions.newBuilder()
            .withApiBase(TestHelper.API_BASE)
            .withApiKey(TestHelper.API_KEY)
            .withTenantId(TestHelper.TENANT_ID);
    }
}
