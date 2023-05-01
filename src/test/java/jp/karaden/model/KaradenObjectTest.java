package jp.karaden.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class KaradenObjectTest {
    static List<?> primitiveValueProvider() {
        return Arrays.asList("string", "", 123, 0, true, false, null);
    }

    static List<?> idValueProvider() {
        return Arrays.asList("string", "", 123, 0, true, false, null);
    }

    @ParameterizedTest
    @MethodSource("primitiveValueProvider")
    void プロパティに入出力できる(Object value) {
        String key = "test";
        KaradenObject object = new KaradenObject();

        object.setProperty(key, value);

        assertEquals(value, object.getProperty(key));
    }

    @Test
    void プロパティのキーを列挙できる() {
        List<String> expected = Arrays.asList("key1", "key2");
        KaradenObject object = new KaradenObject();
        expected.forEach((String value) -> object.setProperty(value, value));

        List<String> keys = object.getPropertyKeys();
        assertInstanceOf(List.class, keys);
        expected.forEach((String value) -> assertTrue(keys.contains(value)));
    }

    @ParameterizedTest
    @MethodSource("idValueProvider")
    void idを出力できる(Object expected) {
        KaradenObject object = new KaradenObject();
        object.setProperty("id", expected);
        assertEquals(expected, object.getId());
    }

    @Test
    void objectを出力できる() {
        String expected = "test";
        KaradenObject object = new KaradenObject();
        object.setProperty("object", expected);
        assertEquals(expected, object.getObject());
    }
}