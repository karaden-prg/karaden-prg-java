package jp.karaden;

import java.io.IOException;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.karaden.exception.FileUploadFailedException;
import jp.karaden.model.BulkFile;
import jp.karaden.model.BulkMessage;
import jp.karaden.model.Collection;
import jp.karaden.model.KaradenObject;
import jp.karaden.model.Error;
import jp.karaden.model.Message;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utility {
    protected final static Map<String, Class<?>> objectTypes = new HashMap<>();
    protected static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
    protected static final int DEFAULT_READ_TIMEOUT = 30000;

    static {
        Utility.objectTypes.put(Error.OBJECT_NAME, Error.class);
        Utility.objectTypes.put(Collection.OBJECT_NAME, Collection.class);
        Utility.objectTypes.put(Message.OBJECT_NAME, Message.class);
        Utility.objectTypes.put(BulkFile.OBJECT_NAME, BulkFile.class);
        Utility.objectTypes.put(BulkMessage.OBJECT_NAME, BulkMessage.class);
    }

    public static KaradenObject convertToKaradenObject(JSONObject contents, RequestOptions requestOptions) {
        Class<?> cls = contents.has("object") ? Utility.objectTypes.get(contents.get("object")) : null;
        cls = cls == null ? KaradenObject.class : cls;
        return Utility.constructFrom(cls, contents, requestOptions);
    }

    protected static KaradenObject constructFrom(Class<?> cls, JSONObject contents, RequestOptions requestOptions) {
        Object id = contents.has("id") ? contents.get("id") : null;
        KaradenObject object = null;
        try {
            Constructor<?> constructor = cls.getConstructor(Object.class, RequestOptions.class);
            object = (KaradenObject)constructor.newInstance(id, null);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        for (String key : contents.keySet()) {
            Object value = null;
            if (! contents.isNull(key)) {
                value = Utility.convertToObject(contents.get(key), requestOptions);
            }
            object.setProperty(key, value);
        }

        return object;
    }

    protected static List<?> convertToList(JSONArray contents, RequestOptions requestOptions) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < contents.length(); i++) {
            Object value = null;
            if (! contents.isNull(i)) {
                value = Utility.convertToObject(contents.get(i), requestOptions);
            }
            list.add(value);
        }

        return list;
    }

    protected static Object convertToObject(Object value, RequestOptions requestOptions) {
        if (value instanceof JSONArray) {
            value = Utility.convertToList((JSONArray)value, requestOptions);
        } else if (value instanceof JSONObject) {
            value = Utility.convertToKaradenObject((JSONObject)value, requestOptions);
        }
        return value;
    }

    public static void putSignedUrl(String signedUrl, String filename) throws FileUploadFailedException {
        putSignedUrl(signedUrl, filename, "application/octet-stream", new RequestOptions());
    }

    public static void putSignedUrl(String signedUrl, String filename, String contentType) throws FileUploadFailedException {
        putSignedUrl(signedUrl, filename, contentType, new RequestOptions());
    }

    public static void putSignedUrl(String signedUrl, String filename, String contentType, RequestOptions requestOptions) throws FileUploadFailedException {
        HashMap<String, Integer> timeout = Utility.getTimeout(requestOptions);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(timeout.get("connectionTimeout"), TimeUnit.MILLISECONDS)
                .readTimeout(timeout.get("readTimeout"), TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(signedUrl)
                .put(RequestBody.create(new File(filename), MediaType.parse(contentType)))
                .build();

        try {
            Response response = httpClient.newCall(request).execute();

            if (response.code() != 200) {
                throw new FileUploadFailedException();
            }
        } catch (FileUploadFailedException e1) {
            throw e1;
        } catch (IOException e2) {
            throw new FileUploadFailedException(e2);
        }
    }

    public static HashMap<String, Integer> getTimeout(RequestOptions requestOptions) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        result.put("connectionTimeout", (requestOptions.connectionTimeout != null ? requestOptions.connectionTimeout : Utility.DEFAULT_CONNECTION_TIMEOUT));
        result.put("readTimeout", (requestOptions.readTimeout != null ? requestOptions.readTimeout : Utility.DEFAULT_READ_TIMEOUT));
        return result;
    }
}
