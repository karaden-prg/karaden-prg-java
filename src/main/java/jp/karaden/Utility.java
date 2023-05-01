package jp.karaden;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.karaden.model.Collection;
import jp.karaden.model.KaradenObject;
import jp.karaden.model.Error;
import jp.karaden.model.Message;

public class Utility {
    protected final static Map<String, Class<?>> objectTypes = new HashMap<>();

    static {
        Utility.objectTypes.put(Error.OBJECT_NAME, Error.class);
        Utility.objectTypes.put(Collection.OBJECT_NAME, Collection.class);
        Utility.objectTypes.put(Message.OBJECT_NAME, Message.class);
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
}
