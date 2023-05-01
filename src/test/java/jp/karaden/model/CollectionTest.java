package jp.karaden.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;


class CollectionTest {
    @Test
    void dataを出力できる() {
        List<String> value = new ArrayList<>();
        Collection collection = new Collection();
        collection.setProperty("data", value);
        assertInstanceOf(List.class, collection.getData());
    }

    @Test
    void hasMoreを出力できる() {
        boolean value = true;
        Collection collection = new Collection();
        collection.setProperty("has_more", value);
        assertTrue(collection.hasMore());
    }
}