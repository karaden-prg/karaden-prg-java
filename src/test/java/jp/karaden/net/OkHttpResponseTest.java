package jp.karaden.net;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import jp.karaden.RequestOptions;
import jp.karaden.exception.BadRequestException;
import jp.karaden.exception.ForbiddenException;
import jp.karaden.exception.NotFoundException;
import jp.karaden.exception.TooManyRequestsException;
import jp.karaden.exception.UnauthorizedException;
import jp.karaden.exception.UnexpectedValueException;
import jp.karaden.exception.UnknownErrorException;
import jp.karaden.exception.UnprocessableEntityException;
import jp.karaden.model.KaradenObject;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class OkHttpResponseTest {
    static List<?> statusCodeProvider() {
        return Arrays.asList(100, 200, 300, 400, 500);
    }

    static List<?> objectProvider() {
        return Arrays.asList("message", "", null);
    }

    static List<?> specialErrorStatusCodeProvider() {
        return Arrays.asList(
            UnauthorizedException.class,
            BadRequestException.class,
            NotFoundException.class,
            ForbiddenException.class,
            UnprocessableEntityException.class,
            TooManyRequestsException.class
        );
    }

    static Stream<Integer> errorStatusCodeProvider() {
        List<?> excluded = Arrays.asList(
            UnauthorizedException.STATUS_CODE,
            BadRequestException.STATUS_CODE,
            NotFoundException.STATUS_CODE,
            ForbiddenException.STATUS_CODE,
            UnprocessableEntityException.STATUS_CODE,
            TooManyRequestsException.STATUS_CODE
        );
        return IntStream
            .concat(
                IntStream.range(100, 199),
                IntStream.range(400, 599)
            )
            .boxed()
            .filter((statusCode) -> ! excluded.contains(statusCode));
    }

    @Test
    void 正常系のステータスコードで本文がJSONならばオブジェクトが返る() throws IOException {
        int statusCode = 200;
        JSONObject json = new JSONObject();
        json.put("test", "test");
        String body = json.toString();
        RequestOptions requestOptions = new RequestOptions();

        
        OkHttpResponse response = new OkHttpResponse(
            (new okhttp3.Response.Builder())
                .request((new Request.Builder()).url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .code(statusCode)
                .body(ResponseBody.create(body, MediaType.parse("application/json")))
                .message("")
                .build(),
            requestOptions
        );

        assertFalse(response.isError());
        assertInstanceOf(KaradenObject.class, response.object);
    }

    @ParameterizedTest
    @MethodSource("statusCodeProvider")
    public void ステータスコードによらず本文がJSONでなければUnexpectedValueException(int statusCode) throws IOException {
        String body = "";
        RequestOptions requestOptions = new RequestOptions();
            
        OkHttpResponse response = new OkHttpResponse(
            (new okhttp3.Response.Builder())
                .request((new Request.Builder()).url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .code(statusCode)
                .body(ResponseBody.create(body, MediaType.parse("application/json")))
                .message("")
                .build(),
            requestOptions
        );

        assertTrue(response.isError());
        assertInstanceOf(UnexpectedValueException.class, response.error);
        assertEquals(statusCode, ((UnexpectedValueException)response.error).statusCode);
    }

    @Test
    void エラー系のステータスコードで本文にobjectのプロパティがなければerror以外はUnexpectedValueException() throws IOException {
        int statusCode = 400;
        JSONObject json = new JSONObject();
        json.put("test", "test");
        String body = json.toString();
        RequestOptions requestOptions = new RequestOptions();

        OkHttpResponse response = new OkHttpResponse(
            (new okhttp3.Response.Builder())
                .request((new Request.Builder()).url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .code(statusCode)
                .body(ResponseBody.create(body, MediaType.parse("application/json")))
                .message("")
                .build(),
            requestOptions
        );

        assertTrue(response.isError());
        assertInstanceOf(UnexpectedValueException.class, response.error);
        assertEquals(statusCode, ((UnexpectedValueException)response.error).statusCode);
    }

    @ParameterizedTest
    @MethodSource("objectProvider")
    void エラー系のステータスコードで本文にobjectのプロパティの値がerror以外はUnexpectedValueException(Object object) throws IOException {
        int statusCode = 400;
        JSONObject json = new JSONObject();
        json.put("object", object);
        String body = json.toString();
        RequestOptions requestOptions = new RequestOptions();

        OkHttpResponse response = new OkHttpResponse(
            (new okhttp3.Response.Builder())
                .request((new Request.Builder()).url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .code(statusCode)
                .body(ResponseBody.create(body, MediaType.parse("application/json")))
                .message("")
                .build(),
            requestOptions
        );

        assertTrue(response.isError());
        assertInstanceOf(UnexpectedValueException.class, response.error);
        assertEquals(statusCode, ((UnexpectedValueException)response.error).statusCode);
    }

    @ParameterizedTest
    @MethodSource("errorStatusCodeProvider")
    void エラー系のステータスコードで特殊例外以外はUnknownErrorException(int statusCode) throws IOException {
        JSONObject json = new JSONObject();
        json.put("object", "error");
        json.put("test", "test");
        String body = json.toString();
        RequestOptions requestOptions = new RequestOptions();

        OkHttpResponse response = new OkHttpResponse(
            (new okhttp3.Response.Builder())
                .request((new Request.Builder()).url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .code(statusCode)
                .body(ResponseBody.create(body, MediaType.parse("application/json")))
                .message("")
                .build(),
            requestOptions
        );

        assertTrue(response.isError());
        assertInstanceOf(UnknownErrorException.class, response.error);
        assertEquals(statusCode, ((UnknownErrorException)response.error).statusCode);
    }

    @ParameterizedTest
    @MethodSource("specialErrorStatusCodeProvider")
    void 特殊例外のステータスコード(Class<?> cls) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, IOException {
        int statusCode = cls.getDeclaredField("STATUS_CODE").getInt(null);
        JSONObject json = new JSONObject();
        json.put("object", "error");
        json.put("test", "test");
        String body = json.toString();
        RequestOptions requestOptions = new RequestOptions();

        OkHttpResponse response = new OkHttpResponse(
            (new okhttp3.Response.Builder())
                .request((new Request.Builder()).url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .code(statusCode)
                .body(ResponseBody.create(body, MediaType.parse("application/json")))
                .message("")
                .build(),
            requestOptions
        );

        assertTrue(response.isError());
        assertInstanceOf(cls, response.error);
    }
}
