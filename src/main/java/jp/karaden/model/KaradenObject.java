package jp.karaden.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.karaden.RequestOptions;


public class KaradenObject implements KaradenObjectInterface {
    protected Map<String, Object> properties;    
    protected RequestOptions requestOptions;

    public KaradenObject() {
        this(null, null);
    }

    public KaradenObject(Object id, RequestOptions requestOptions) {
        this.properties = new HashMap<>();
        this.requestOptions = requestOptions;

        this.setProperty("id", id);
    }

    public Object getId() {
        return this.getProperty("id");
    }

    public String getObject() {
        return (String)this.getProperty("object");
    }

    public List<String> getPropertyKeys() {
        return this.properties.keySet().stream().collect(Collectors.toList());
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }
}
