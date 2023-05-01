package jp.karaden.model;

import java.util.List;

public interface KaradenObjectInterface {
    Object getId();
    String getObject();
    List<String> getPropertyKeys();
    Object getProperty(String key);
    void setProperty(String key, Object value);
}
