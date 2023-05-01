package jp.karaden.model;

import java.util.List;

import jp.karaden.RequestOptions;


public class Collection extends KaradenObject {
    public final static String OBJECT_NAME = "list";

    public Collection() {
        super();
    }

    public Collection(Object id, RequestOptions requestOptions) {
        super(id, requestOptions);
    }

    public List<Message> getData() {
        return (List<Message>)this.getProperty("data");
    }

    public boolean hasMore() {
        return (Boolean)this.getProperty("has_more");
    }
}
