package jp.karaden;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;

class ConfigTest {
    @AfterAll
    static void tearDown() {
        Config.apiBase = Config.DEFAULT_API_BASE;
        Config.apiKey = null;
        Config.apiVersion = Config.DEFALUT_API_VERSION;
        Config.tenantId = null;
        Config.userAgent = null;
        Config.connectionTimeout = 0;
        Config.readTimeout = 0;
    }

    @Test
    void 入力したapiBaseが取得したRequestOptionsに入力されること() {
        String expected = TestHelper.API_BASE;
        Config.apiBase = expected;
        RequestOptions requestOptions = Config.asRequestOptions();

        assertEquals(expected, requestOptions.apiBase);
    }

    @Test
    void 入力したapiKeyが取得したRequestOptionsに入力されること() {
        String expected = TestHelper.API_KEY;
        Config.apiKey = expected;
        RequestOptions requestOptions = Config.asRequestOptions();

        assertEquals(expected, requestOptions.apiKey);
    }

    @Test
    void 入力したtenantIdが取得したRequestOptionsに入力されること() {
        String expected = TestHelper.TENANT_ID;
        Config.tenantId = expected;
        RequestOptions requestOptions = Config.asRequestOptions();

        assertEquals(expected, requestOptions.tenantId);
    }

    @Test
    void 入力したconnectionTimeoutが取得したRequestOptionsに入力されること() {
        int expected = 1;
        Config.connectionTimeout = expected;
        RequestOptions requestOptions = Config.asRequestOptions();

        assertEquals(expected, requestOptions.connectionTimeout);
    }

    @Test
    void 入力したreadTimeoutが取得したRequestOptionsに入力されること() {
        int expected = 1;
        Config.readTimeout = expected;
        RequestOptions requestOptions = Config.asRequestOptions();

        assertEquals(expected, requestOptions.readTimeout);
    }

    @Test
    void 入力したapiVersionが取得したRequestOptionsに入力されること() {
        String expected = "2023-01-01";
        Config.apiVersion = expected;
        RequestOptions requestOptions = Config.asRequestOptions();

        assertEquals(expected, requestOptions.apiVersion);
    }

    @Test
    void 入力したuserAgentが取得したRequestOptionsに入力されること() {
        String expected = "userAgent";
        Config.userAgent = expected;
        RequestOptions requestOptions = Config.asRequestOptions();

        assertEquals(expected, requestOptions.userAgent);
    }
}
