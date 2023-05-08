package jp.karaden;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import jp.karaden.exception.InvalidRequestOptionsException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class RequestOptionsTest {
    @Test
    void getBaseUriはapiBaseとtenantIdを半角スラッシュで結合した値() {
        String apiBase = TestHelper.API_BASE;
        String tenantId = TestHelper.TENANT_ID;

        RequestOptions requestOptions = RequestOptions.newBuilder()
            .withApiBase(apiBase)
            .withTenantId(tenantId)
            .build();

        assertEquals(String.format("%s/%s", apiBase, tenantId), requestOptions.getBaseUri());
    }

    @Test
    void マージ元がnullならばマージ先を上書きしない() {
        String apiKey = TestHelper.API_KEY;
        List<RequestOptions> requestOptions = new ArrayList<RequestOptions>();
        requestOptions.add(RequestOptions.newBuilder().withApiKey(apiKey).build());
        requestOptions.add(RequestOptions.newBuilder().build());

        RequestOptions merged = requestOptions.get(0).merge(requestOptions.get(1));

        assertEquals(apiKey, merged.apiKey);
    }

    @Test
    void マージ元がnullでなければマージ先を上書きする() {
        String[] apiKeys = new String[] { "a", "b" };
        List<RequestOptions> requestOptions = new ArrayList<RequestOptions>();
        requestOptions.add(RequestOptions.newBuilder().withApiKey(apiKeys[0]).build());
        requestOptions.add(RequestOptions.newBuilder().withApiKey(apiKeys[1]).build());

        RequestOptions merged = requestOptions.get(0).merge(requestOptions.get(1));

        assertEquals(apiKeys[1], merged.apiKey);
    }

    @Test
    void apiVersionを入力できる() {
        String expected = "2023-01-01";
        RequestOptions requestOptions = (new RequestOptions.Builder())
            .withApiVersion(expected)
            .build();

        assertEquals(expected, requestOptions.apiVersion);
    }

    @Test
    void apiBaseを入力できる() {
        String expected = TestHelper.API_BASE;
        RequestOptions requestOptions = (new RequestOptions.Builder())
            .withApiBase(expected)
            .build();

        assertEquals(expected, requestOptions.apiBase);
    }

    @Test
    void apiKeyを入力できる() {
        String expected = TestHelper.API_KEY;
        RequestOptions requestOptions = (new RequestOptions.Builder())
            .withApiKey(expected)
            .build();

        assertEquals(expected, requestOptions.apiKey);
    }

    @Test
    void tenantIdを入力できる() {
        String expected = TestHelper.TENANT_ID;
        RequestOptions requestOptions = (new RequestOptions.Builder())
            .withTenantId(expected)
            .build();

        assertEquals(expected, requestOptions.tenantId);
    }

    @Test
    void userAgentを入力できる() {
        String expected = "userAgent";
        RequestOptions requestOptions = (new RequestOptions.Builder())
            .withUserAgent(expected)
            .build();

        assertEquals(expected, requestOptions.userAgent);
    }

    @Test
    void connectionTimeoutを入力できる() {
        int expected = 500;
        RequestOptions requestOptions = (new RequestOptions.Builder())
            .withConnectionTimeout(expected)
            .build();

        assertEquals(expected, requestOptions.connectionTimeout);
    }

    @Test
    void readTimeoutを入力できる() {
        int expected = 500;
        RequestOptions requestOptions = (new RequestOptions.Builder())
            .withReadTimeout(expected)
            .build();

        assertEquals(expected, requestOptions.readTimeout);
    }

    /**
def test_proxyを入力できる():
    expected = 'proxy'
    request_options = RequestOptionsBuilder().with_proxy(expected).build()

    assert expected == request_options.proxy

    
     */

    static List<String> invalidApiKeyProvider() {
        return Arrays.asList(null, "");
    }

    @ParameterizedTest
    @MethodSource("invalidApiKeyProvider")
    void apiKeyが空文字や未指定はエラー(String apiKey) {
        RequestOptions.Builder builder = RequestOptions.newBuilder();
        if (apiKey == null) {
            builder.withApiKey(apiKey);
        }

        assertThrows(InvalidRequestOptionsException.class, () -> builder.build().validate());
    }

    static List<String> invalidApiVersionProvider() {
        return Arrays.asList(null, "");
    }

    @ParameterizedTest
    @MethodSource("invalidApiVersionProvider")
    void apiVersionが空文字や未指定はエラー(String apiVersion) {
        RequestOptions.Builder builder = RequestOptions.newBuilder();
        if (apiVersion == null) {
            builder.withApiVersion(apiVersion);
        }

        assertThrows(InvalidRequestOptionsException.class, () -> builder.build().validate());
    }

    static List<String> invalidTenantIdProvider() {
        return Arrays.asList(null, "");
    }

    @ParameterizedTest
    @MethodSource("invalidTenantIdProvider")
    void tenantIdが空文字や未指定はエラー(String tenantId) {
        RequestOptions.Builder builder = RequestOptions.newBuilder();
        if (tenantId == null) {
            builder.withTenantId(tenantId);
        }

        assertThrows(InvalidRequestOptionsException.class, () -> builder.build().validate());
    }

    static List<String> invalidApiBaseProvider() {
        return Arrays.asList(null, "");
    }

    @ParameterizedTest
    @MethodSource("invalidApiBaseProvider")
    void apiBaseが空文字や未指定はエラー(String apiBase) {
        RequestOptions.Builder builder = RequestOptions.newBuilder();
        if (apiBase == null) {
            builder.withApiBase(apiBase);
        }

        assertThrows(InvalidRequestOptionsException.class, () -> builder.build().validate());
    }
}
